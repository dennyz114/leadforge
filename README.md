# 🧩 LeadForge – Multi-Tenant Lead Management Platform

LeadForge is a **B2B SaaS** platform for managing and integrating **leads** across multiple companies and CRMs (Salesforce, HubSpot, etc.).  
It’s designed to demonstrate **enterprise architecture, integration patterns, and AI-ready microservices**.

---

## 🏗️ Overview

**Goal:** Provide a backend and frontend ecosystem to collect, classify and sync leads between internal systems and external CRMs.

**Architecture:**  
- **Frontend:** Next.js 15 (TypeScript, Tailwind, Auth.js, Apollo Client)
- **Backend:** Spring Boot 3.3+, GraphQL API, Redis, JPA, Kafka/SNS, S3, Docker, AWS ECS
- **Style:** Event-Driven + Microservices
- **Language:** Java 21 / Gradle 8

---

## ⚙️ Backend (Spring Boot)

### Core Services (Phase 1 → Phase N)

| Service | Purpose | Status |
|----------|----------|--------|
| **auth-service** | Authentication & multi-tenant session management | ✅ Phase 1 |
| **lead-service** | Lead CRUD, validation, and mapping to company campaigns | 🚧 |
| **integration-service** | External CRM integration (Salesforce, HubSpot) | 🚧 |
| **notification-service** | Email & event notifications via AWS SNS/SES | 🚧 |

Each service is isolated but shares a common configuration module.

---

### Key Features

- 🔑 **OAuth 2.0 connectors** for Salesforce & HubSpot  
- 🧠 **Multi-tenant model:** one DB schema, tenant isolation via context filters  
- ⚡ **Caching layer:** Redis (ElastiCache) or Hazelcast, toggle via profile  
- 🪶 **Async event flow:** Kafka/SNS topic-based communication between services  
- 📦 **File uploads:** AWS S3 SDK integration  
- 🔐 **Security:** JWT-based auth, configurable per-tenant keys  
- 🧰 **GraphQL API:** used by Next.js frontend and external clients  