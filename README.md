# WebStore API

![Java](https://img.shields.io/badge/Java-17-blue)
![OSGi](https://img.shields.io/badge/OSGi-iDempiere-green)
![API](https://img.shields.io/badge/API-OpenAPI%203-orange)
![License](https://img.shields.io/badge/license-GPL%20v2-lightgrey)

REST API plugin for iDempiere powering the ICreated WebStore frontend.

---

## Overview

This project provides a REST API for building e-commerce frontends on top of iDempiere.

It follows an **OpenAPI-first approach**:
- endpoints defined in `openapi.yaml`
- interfaces generated automatically
- controllers implement the API
- services handle business logic

**Flow:**

openapi.yaml → API interfaces → Controllers → Services → iDempiere

---

## Tech Stack

- Java 17
- OSGi (iDempiere / Jetty)
- JAX-RS (Jersey)
- OpenAPI 3 / Swagger
- JWT authentication
- Jackson
- Hibernate Validator

---

## Getting Started

### Clone

git clone https://github.com/icreated/webstore-api.git
cd webstore-api

### Build

mvn clean package

### Deploy

Copy the generated JAR into:

<idempiere>/dropins/

Or install via OSGi console:

osgi> install file:/path/to/plugin.jar  
osgi> start <bundle-id>

---

## API Documentation

- OpenAPI spec: `openapi.yaml`
- Swagger: http://localhost:8080/services/api/openapi.json

---

## Authentication

Authorization: Bearer <token>

### Login

POST /auth/login

---

## Main Endpoints

### Public

- GET /catalog/categories
- GET /catalog/products/{categoryId}
- GET /catalog/products/search
- GET /common/countries
- GET /common/shippers

### Authenticated

- GET /account/info
- PUT /account/info
- POST /account/orders
- GET /account/orders
- POST /account/orders/{id}/payment

---

## Architecture

- Controller — REST endpoints
- Service — business logic
- Factory — dependency injection
- Security — JWT, CORS
- Utils — helpers

---

## Project Structure

src/
├── api/
├── controller/
├── service/
├── security/
├── factory/
├── mapper/
├── utils/

---

## Configuration

Uses iDempiere `W_Store` configuration.

---

## Versioning

Managed via Git tags and Maven.

---

## Related Projects

- Frontend: https://webstore.icreated.co
- Docs: https://icreated.co/projects/webstore-api/

---

## Contributing

Pull requests are welcome.

---

## License

GPL v2
