# ====== Variables ======
COMPOSE := docker compose -f infra/docker-compose.dev.yml
API_DIR := backend
WEB_DIR := frontend

# ====== Ayuda ======
.PHONY: help
help:
	@echo "Comandos utiles:"
	@echo "  make up           -> Levanta todo el entorno (DB, Redis, API, Web)"
	@echo "  make down         -> Detiene y limpia los contenedores"
	@echo "  make ps           -> Lista servicios"
	@echo "  make logs         -> Logs de todos los servicios"
	@echo "  make rebuild      -> Rebuild de imágenes y levantar"
	@echo "  make clean        -> Borra volúmenes y caché (cuidado)"
	@echo "  make health       -> Chequea /health del backend"
	@echo "  make api-dev      -> Corre backend local (sin docker)"
	@echo "  make web-dev      -> Corre frontend local (sin docker)"
	@echo "  make test-backend -> Tests de backend (Gradle)"
	@echo "  make lint-web     -> Lint del frontend"
	@echo "  make build-web    -> Build del frontend"
	@echo "  make ci-local     -> Simula CI: test backend + build/lint frontend"

# ====== Docker ======
.PHONY: up down ps logs rebuild clean
up:
	$(COMPOSE) up --build

down:
	$(COMPOSE) down

ps:
	$(COMPOSE) ps

logs:
	$(COMPOSE) logs -f

rebuild:
	$(COMPOSE) build --no-cache
	$(COMPOSE) up

# Elimina contenedores, redes y volúmenes huérfanos
clean:
	$(COMPOSE) down -v --remove-orphans
	docker system prune -f

# ====== Healthcheck ======
.PHONY: health
health:
	@echo "Backend:"
	@curl -s http://localhost:8080/health || true
	@echo "\nFrontend:"
	@curl -s http://localhost:3000/api/health || true
	@echo

# ====== Backend local (sin Docker) ======
.PHONY: api-dev test-backend
api-dev:
	cd $(API_DIR) && ./gradlew bootRun

test-backend:
	cd $(API_DIR) && ./gradlew clean test

# ====== Frontend local (sin Docker) ======
.PHONY: web-dev lint-web build-web
web-dev:
	cd $(WEB_DIR) && npm run dev

lint-web:
	cd $(WEB_DIR) && npm run lint

build-web:
	cd $(WEB_DIR) && npm run build

# ====== CI local rápido ======
.PHONY: ci-local
ci-local: test-backend build-web lint-web
	@echo "✅ CI local OK"
