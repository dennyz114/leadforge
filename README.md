# LeadForge

SaaS de gestión de leads (multiempresa). Monorepo con Next.js + Spring Boot.

## Quick start (dev)
```bash
cp .env.example .env
docker compose -f infra/docker-compose.dev.yml up --build
# frontend: http://localhost:3000  |  backend: http://localhost:8080/health
