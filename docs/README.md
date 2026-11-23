# Outage Management and Restoration System (OMRS) — J16
### Enterprise Energy & Utilities Distribution Incident Response Platform

![Tech Stack](https://img.shields.io/badge/Stack-Java%2017%20%7C%20Spring%20Boot%203.2%20%7C%20Angular%2017%20%7C%20Oracle%2021c%20XE-0284c7)
![Build](https://img.shields.io/badge/Build-Maven%20%7C%20npm-green)
![Security](https://img.shields.io/badge/Security-Spring%20Security%206%20%7C%20JWT%20%7C%20BCrypt-red)

---

## 1. Project Overview
The **Outage Management and Restoration System (OMRS)** is a mission-critical full-stack platform built for the **Energy & Utilities** sector. It modernizes electrical grid control operations through automated incident reporting, smart duplicate ticket consolidation, conflict-free crew dispatching, field restoration progress milestone tracking, and multi-channel customer communications.

### Core Modules:
1. **Authentication & Role-Based Navigation**: Stateless JWT authentication, role guards (`ADMIN`, `OPERATOR`, `FIELD_ENGINEER`, `CUSTOMER`, `CREW_MEMBER`), and live operational dashboard metrics.
2. **Outage Detection and Reporting**: Ingestion of customer/sensor reports, automatic duplicate grouping by feeder/area, multi-parameter search/filtering, and automatic state progression (`REPORTED` $\to$ `VERIFIED` $\to$ `IN_PROGRESS` $\to$ `RESTORED`).
3. **Crew Dispatch and Work Order Management**: Fleet status tracking, assignment collision prevention (blocking dispatch of busy crews), priority classification, and work order lifecycle management.
4. **Restoration Progress Tracking**: Field engineers submit milestone percentages ($0-100\%$), remarks, and updated ETR. Reaching $100\%$ automatically closes the work order and outage while calculating restoration duration in minutes.
5. **Customer Notification and Communication**: Automated template-driven alerts (`OUTAGE_REPORTED`, `CREW_ASSIGNED`, `ETR_UPDATED`, `POWER_RESTORED`) via simulated SMS/Email and broadcast notices for entire distribution zones.

---

## 2. Technology Stack

| Layer | Technology | Details |
| :--- | :--- | :--- |
| **Backend** | Java 17, Spring Boot 3.2.4 | Spring Web, Spring Data JPA, Spring Security 6, Hibernate 6, Bean Validation |
| **Security** | JWT (jjwt 0.12.5), BCrypt | Stateless bearer tokens, per-request filter, role-based authorization |
| **API Docs** | Springdoc OpenAPI 3.0 | Swagger UI interactive documentation at `/swagger-ui.html` |
| **Frontend** | Angular 17.3+ | Standalone components, Angular Router, HttpClient with functional interceptors |
| **Database** | Oracle Database 21c XE | Normalized 3NF schema, identity columns, composite indexes, constraints |
| **Testing** | JUnit 5 + Mockito, Jasmine/Karma | 18+ service layer unit tests, mock repository verifications |
| **Build Tools** | Apache Maven 3.9+, Node.js v24+, npm | Maven build wrapper, npm package manager |

---

## 3. Database Setup (Oracle Database 21c XE)

### Step 3.1: Connect as SYSDBA and Create User
Connect to your local Oracle 21c XE Pluggable Database (`XEPDB1`) using `sqlplus` or Oracle SQL Developer:

```sql
-- Connect to pluggable database container
ALTER SESSION SET CONTAINER = XEPDB1;

-- Create application schema user
CREATE USER omrs_user IDENTIFIED BY omrs_password DEFAULT TABLESPACE USERS TEMPORARY TABLESPACE TEMP;

-- Grant required enterprise privileges
GRANT CONNECT, RESOURCE, CREATE SESSION, CREATE TABLE, CREATE VIEW, CREATE SEQUENCE, UNLIMITED TABLESPACE TO omrs_user;
```

### Step 3.2: Run DDL & Seed Scripts
Execute the normalized schema and seed scripts under `database/`:

```powershell
# Using SQL*Plus:
sqlplus omrs_user/omrs_password@localhost:1521/XEPDB1 @database/schema.sql
sqlplus omrs_user/omrs_password@localhost:1521/XEPDB1 @database/seed_data.sql
```

*Or open [database/schema.sql](database/schema.sql) and [database/seed_data.sql](database/seed_data.sql) in Oracle SQL Developer and click "Run Script (F5)".*

---

## 4. Default Login Credentials

All seeded user accounts use the default password: **`password`** (BCrypt hashed).

| Username | Role | Full Name | Domain Function |
| :--- | :--- | :--- | :--- |
| **`admin`** | `ROLE_ADMIN`, `ROLE_OPERATOR` | Alex Morgan | Chief Grid Controller / System Administrator |
| **`operator1`** | `ROLE_OPERATOR` | Samantha Carter | Distribution Grid Operator (Triage & Dispatch) |
| **`engineer1`** | `ROLE_FIELD_ENGINEER` | Marcus Vance | Lead Field Restoration Engineer |
| **`crew1_lead`** | `ROLE_FIELD_ENGINEER`, `ROLE_CREW_MEMBER` | Jake Miller | Foreman (Alpha Rapid Response Crew) |
| **`cust_john`** | `ROLE_CUSTOMER` | John Doe | Residential Customer (Green Valley Suburbs) |
| **`cust_sarah`** | `ROLE_CUSTOMER` | Sarah Connor | Residential Customer (Green Valley Suburbs) |

---

## 5. Running the Backend

### Prerequisites:
- Java 17 JDK installed and `JAVA_HOME` configured.
- Maven 3.8+ installed.

### Execution Commands:
```powershell
cd backend

# Build and run tests
mvn clean package

# Start Spring Boot application
mvn spring-boot:run
```
The backend starts at `http://localhost:8080/api`.
Interactive Swagger UI documentation is available at:
👉 **`http://localhost:8080/api/swagger-ui.html`**

---

## 6. Running the Frontend

### Option A: Instant Interactive Demo (Zero Setup - Mentor Ready)
Double-click [`frontend-demo/index.html`](frontend-demo/index.html) or run a lightweight server:
```powershell
python -m http.server 4200 --directory frontend-demo
```
Open **`http://localhost:4200`** in your browser. This UI includes live role-switching (Admin/Engineer/Customer), duplicate report grouping alerts, crew dispatch with collision blocking, and an interactive 0-100% restoration progress slider.

### Option B: Angular 17+ Application (Full Build)
```powershell
cd frontend

# Install dependencies
npm install

# Run Angular Development Server
npm start
# OR: ng serve --port 4200
```

---

## 7. Running Unit Tests

### Backend (JUnit 5 + Mockito):
```powershell
cd backend
mvn test
```
*Executes 18+ service layer unit tests across Auth, Outages, Crews, Work Orders, Progress Tracking, and Notifications.*

### Frontend (Karma / Jasmine):
```powershell
cd frontend
npm test -- --watch=false --browsers=ChromeHeadless
```

---

## 8. Git Branching Strategy & Release Tags

The repository follows standard Git-Flow for enterprise utility development:

```text
main (Production Releases)
 │
 ├── tag: v1.0.0 (Final Complete Release)
 ├── tag: v0.1.0 (MVP Sprint Baseline)
 │
 └── develop (Integration Branch)
      │
      ├── feature/J16-US01-auth-jwt
      ├── feature/J16-US02-outage-reporting-duplicate-grouping
      ├── feature/J16-US03-crew-dispatch-workorders
      ├── feature/J16-US04-restoration-progress-tracking
      └── feature/J16-US05-customer-notifications-broadcast
```

### Git CLI Setup Commands:
```bash
# Initialize and branch setup
git init
git checkout -b develop

# Create feature branches
git checkout -b feature/J16-US01-auth-jwt
git commit -m "feat(auth): JWT stateless security and RBAC navigation"

git checkout develop
git merge --no-ff feature/J16-US01-auth-jwt
git tag -a v0.1.0 -m "Release v0.1.0: MVP Outage Detection and Auth baseline"

# Final release tag
git checkout main
git merge --no-ff develop
git tag -a v1.0.0 -m "Release v1.0.0: Production-ready OMRS platform"
```
