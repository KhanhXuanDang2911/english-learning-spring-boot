# E-Learning (Spring Boot)

A Spring Boot-based e-learning backend with PostgreSQL, containerized via Docker Compose.

## Main Features

- Authentication and authorization (RBAC: Admin, Instructor, Student)
- User profile management
- Course management: create, update, publish, archive
- Lessons/modules with file uploads and external video links
- Enrollment and access control
- Learner progress tracking and completion status
- Search, categories, and tags for content discovery
- RESTful API with optional OpenAPI/Swagger docs
- Configurable via Spring profiles; Dockerized with PostgreSQL persistence

## Prerequisites

- Docker and Docker Compose
- Java (for local runs without Docker)
- Maven or Gradle (depending on your build tool)

## Quick Start (Docker)

1. Create a `.env` file in the project root with:
   ```
   SPRING_DATASOURCE_USERNAME=your_db_username
   SPRING_DATASOURCE_PASSWORD=your_db_password
   ```
2. Start services:
   ```
   docker compose up -d
   ```
3. Access the app: http://localhost:8080  
   PostgreSQL is exposed at localhost:5433 (database: `ELearning`).

Notes:

- The app uses profile `prod` by default in Docker (`SPRING_PROFILES_ACTIVE=prod`).
- The application connects to DB host `e-learning-db` inside the Docker network.

## Environment Variables

- SPRING_DATASOURCE_USERNAME: Postgres username
- SPRING_DATASOURCE_PASSWORD: Postgres password
- DB_HOST: Database host (Docker: `e-learning-db`; Local: `localhost`)
- SPRING_PROFILES_ACTIVE: Active Spring profile (e.g., `dev`, `prod`)

## Local Development (without Docker for the app)

Option A: Use Docker for DB only

1. Start only the DB:
   ```
   docker compose up -d e-learning-db
   ```
2. Configure your app to connect to the exposed DB:
   - DB host: `localhost`
   - Port: `5433`
   - DB name: `ELearning`
   - Credentials: from your `.env`
3. Run the app:
   - Maven:
     ```
     mvn spring-boot:run
     ```
   - Gradle:
     ```
     ./gradlew bootRun
     ```

Option B: Everything local

- Run a local PostgreSQL, create database `ELearning`, and set env vars accordingly.
- Start app via Maven/Gradle as above.

## Build and Run (Jar)

- Maven:
  ```
  mvn clean package
  java -jar target/*.jar
  ```
- Gradle:
  ```
  ./gradlew clean bootJar
  java -jar build/libs/*.jar
  ```

## Troubleshooting

- Port in use:
  - Change exposed ports in `docker-compose.yml` or stop the conflicting service.
- Reset DB data:
  ```
  docker compose down -v
  ```
- Check logs:
  ```
  docker compose logs -f e-learning-app
  docker compose logs -f e-learning-db
  ```
- Verify DB connection:
  - Host: `localhost` (local) or `e-learning-db` (Docker)
  - Port: `5433` (host) / `5432` (container)
  - DB: `ELearning`

## Project Structure

- docker-compose.yml: Orchestrates Postgres and the Spring Boot app
- .env: Stores DB credentials and app env vars (not committed)
- Dockerfile: Builds the application image
