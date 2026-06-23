# TalentBoard

A recruitment management platform (ATS) built with Spring Boot 3, Thymeleaf, Spring Security, and PostgreSQL.
It supports three user roles — **Admin**, **Recruiter**, and **Candidate** — each with tailored dashboards and workflows covering vacancy posting, application tracking, and interview scheduling.

---

## Technologies

| Layer              | Technology                               |
|--------------------|------------------------------------------|
| Backend            | Java 17, Spring Boot 3.3.4               |
| Web / UI           | Spring MVC, Thymeleaf 3, Bootstrap 5.3   |
| Security           | Spring Security 6 (form login, RBAC)     |
| Persistence        | Spring Data JPA, Hibernate, PostgreSQL 15 |
| Object Mapping     | MapStruct 1.5.5                          |
| Boilerplate        | Lombok                                   |
| Containerization   | Docker (multi-stage), Docker Compose     |

---

## Architecture

```
src/main/java/com/talentboard/manager/
├── config/          SecurityConfig
├── controller/      AuthController, RegisterController, DashboardController,
│                    VacancyController, ApplicationController, InterviewController,
│                    AuthenticatedUserAdvice
├── dto/             request/ & response/ DTOs
├── mapper/          MapStruct mappers
├── model/
│   ├── entity/      User, Vacancy, Application, Interview
│   └── enums/       Role, VacancyStatus, AppStatus
├── repository/      JPA repositories
└── service/         Interfaces + impl/

src/main/resources/
├── templates/
│   ├── fragments/   navbar.html, head.html
│   ├── layout/      main.html
│   ├── public/      login.html, register.html
│   ├── private/
│   │   ├── admin/        dashboard.html
│   │   ├── recruiter/    dashboard.html
│   │   ├── candidate/    dashboard.html
│   │   ├── vacancy/      list.html, detail.html, form.html
│   │   ├── application/  list.html, detail.html, apply.html
│   │   └── interview/    list.html, detail.html, form.html
│   └── error/       400.html, 403.html, 404.html, 500.html
└── static/css/      main.css
```

---

## Environment Variables

These variables are injected at runtime and override `application.yml`.

| Variable                        | Docker Default                              | Description               |
|---------------------------------|---------------------------------------------|---------------------------|
| `SPRING_DATASOURCE_URL`         | `jdbc:postgresql://db:5432/talentboard_db`  | JDBC connection URL       |
| `SPRING_DATASOURCE_USERNAME`    | `postgres`                                  | Database username         |
| `SPRING_DATASOURCE_PASSWORD`    | `postgres`                                  | Database password         |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | `update`                                    | Schema management mode    |

---

## Test Credentials

Use the `/register` page to create accounts, or seed them directly via the database.
Passwords are stored as BCrypt hashes.

| Role      | Email                     | Suggested Password |
|-----------|---------------------------|--------------------|
| Admin     | admin@talentboard.com     | Admin1234!         |
| Recruiter | recruiter@talentboard.com | Recruit1234!       |
| Candidate | candidate@talentboard.com | Cand1234!          |

---

## Running with Docker Compose

### Prerequisites

- [Docker](https://docs.docker.com/get-docker/) 24+
- Docker Compose v2+ (included with Docker Desktop)

### Start the full stack

```bash
# 1. Clone the repository
git clone https://github.com/Jajodisant/TalentBoard.git
cd TalentBoard

# 2. Build images and start all services (PostgreSQL + Spring Boot app)
docker compose up --build

# 3. Open the application
#    http://localhost:8080
```

**What happens on first startup:**
1. The `db` service starts PostgreSQL 15 and waits until healthy.
2. The `app` service builds the Spring Boot JAR inside a Maven container (Stage 1 of Dockerfile).
3. The JAR runs inside a lightweight JRE image (Stage 2).
4. Hibernate creates the schema automatically (`ddl-auto: update`).

### Stopping

```bash
# Stop containers, keep the database volume
docker compose down

# Stop and delete the database volume (full reset)
docker compose down -v
```

### Rebuild after code changes

```bash
docker compose up --build
```

---

## Running Locally (without Docker)

### Prerequisites

- Java 17+
- Maven 3.9+
- PostgreSQL 15 running on `localhost:5432`

### Steps

```bash
# 1. Create the database
psql -U postgres -c "CREATE DATABASE talentboard_db;"

# 2. Start the application
./mvnw spring-boot:run
```

Application starts at **http://localhost:8080**.

---

## Key Endpoints

| URL                                  | Method   | Access               | Description                        |
|--------------------------------------|----------|----------------------|------------------------------------|
| `/`                                  | GET      | Public               | Redirects to `/login`              |
| `/login`                             | GET/POST | Public               | Login form                         |
| `/register`                          | GET/POST | Public               | Account registration               |
| `/dashboard`                         | GET      | Authenticated        | Role-specific dashboard            |
| `/vacancies`                         | GET      | Authenticated        | Browse open vacancies              |
| `/vacancies/create`                  | GET/POST | Admin, Recruiter     | Post a new vacancy                 |
| `/vacancies/edit/{id}`               | GET/POST | Admin, Recruiter     | Edit vacancy                       |
| `/vacancies/delete/{id}`             | POST     | Admin, Recruiter     | Delete vacancy                     |
| `/applications/apply/{vacancyId}`    | GET/POST | Candidate            | Submit an application              |
| `/applications/{id}/status`          | POST     | Admin, Recruiter     | Update application status          |
| `/interviews/schedule`               | GET/POST | Admin, Recruiter     | Schedule an interview              |
| `/interviews/delete/{id}`            | POST     | Admin, Recruiter     | Delete an interview                |

---

## License

### JAINER PABÓN BORJA -HAMILTON 2026
