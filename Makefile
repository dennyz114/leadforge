# ====== Variables ======
COMPOSE := docker compose -f infra/docker-compose.dev.yml
API_DIR := backend
WEB_DIR := frontend

# ====== Ayuda ======
.PHONY: help
help:
	@echo "Comandos utiles:"
# 	@echo "  make up           -> Levanta todo el entorno (DB, Redis, API, Web)"
# 	@echo "  make down         -> Detiene y limpia los contenedores"
# 	@echo "  make ps           -> Lista servicios"
# 	@echo "  make logs         -> Logs de todos los servicios"
# 	@echo "  make rebuild      -> Rebuild de imágenes y levantar"
# 	@echo "  make clean        -> Borra volúmenes y caché (cuidado)"
# 	@echo "  make health       -> Chequea /health del backend"
# 	@echo "  make api-dev      -> Corre backend local (sin docker)"
# 	@echo "  make web-dev      -> Corre frontend local (sin docker)"
# 	@echo "  make test-backend -> Tests de backend (Gradle)"
# 	@echo "  make lint-web     -> Lint del frontend"
# 	@echo "  make build-web    -> Build del frontend"
# 	@echo "  make ci-local     -> Simula CI: test backend + build/lint frontend"

# ====== Docker ======
# .PHONY: up down ps logs rebuild clean
# up:
# 	$(COMPOSE) up --build

# down:
# 	$(COMPOSE) down

# ps:
# 	$(COMPOSE) ps

# logs:
# 	$(COMPOSE) logs -f

# rebuild:
# 	$(COMPOSE) build --no-cache
# 	$(COMPOSE) up

# # Elimina contenedores, redes y volúmenes huérfanos
# clean:
# 	$(COMPOSE) down -v --remove-orphans
# 	docker system prune -f

# ====== Healthcheck ======
# .PHONY: health
# health:
# 	@echo "Backend:"
# 	@curl -s http://localhost:8080/health || true
# 	@echo "\nFrontend:"
# 	@curl -s http://localhost:3000/api/health || true
# 	@echo

# ====== Databases in Docker =======
.PHONY: db-up-all db-down-all
db-up-all:
	@echo "Running all databases"
	$(COMPOSE) up -d mysql-auth #mysql-company

db-down-all:
	@echo "Stopping all databases"
	$(COMPOSE) down mysql-auth #mysql-company

# ====== Backend local (sin Docker) ======
.PHONY: gateway-run-local auth-run-local api-run-local
auth-run-local:
	@echo "Running Auth Service"
	cd $(API_DIR) && \
	set -a; \
	. ./.env; \
	. ./auth-service/.env \
	set +a; \
	./gradlew :auth-service:bootRun

gateway-run-local:
	@echo "Running Gateway Service"
	cd $(API_DIR) && \
	set -a; \
	. ./.env; \
	set +a; \
	./gradlew :gateway-service:bootRun

# ====== Backend local - all services (sin Docker) ======
.PHONY: api-run-all-local
api-run-all-local:
	@echo "Running all Services concurrently..." && \
	$(MAKE) auth-run-local & \
	$(MAKE) gateway-run-local & \
	wait

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
