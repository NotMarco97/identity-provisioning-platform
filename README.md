# Identity Provisioning & Cloud Automation Platform

## Overview

The Identity Provisioning Platform is a Spring Boot application designed to automate employee identity provisioning within Microsoft Entra ID using the Microsoft Graph API.

The long-term goal of this project is to implement an onboarding workflow where new employees can be provisioned with user accounts, group memberships, Microsoft 365 licenses, and audit logs through a centralized backend application.

This project is being developed incrementally using versioned releases to demonstrate the design, implementation, and backend system.

---

### High-Level Architecture
<img width="882" height="745" alt="highLevel drawio" src="https://github.com/user-attachments/assets/cc65d749-6334-4500-9c53-e221b20b22b1" />


---

## The problem

As organizations grow, manually provisioning user accounts becomes increasingly time-consuming and error-prone. Creating accounts, assigning group memberships, configuring licenses, and maintaining consistency across every employee can significantly slow onboarding and reduce time available for higher-value IT work.

---

## Solution

This platform is designed to automate the identity provisioning process by receiving onboarding requests, validating employee information, applying business rules, assigning Microsoft 365 resources through Microsoft Graph, and recording provisioning activity for auditing purposes. Automating these repetitive tasks reduces manual effort while improving consistency, scalability, and reliability.

---

## Project Objectives

* Build a production Spring Boot backend application.
* Design scalable backend architecture.
* Learn Microsoft Graph API integration.
* Automate employee identity provisioning workflows.
* Develop hands-on experience with Azure services and cloud authentication.
* Document architectural decisions and project evolution throughout development.

---

## Technologies

Current technologies:

* Java 21
* Spring Boot
* Maven
* PostgreSQL
* Git
* GitHub
* Microsoft Graph API
* Microsoft Entra ID

Planned technologies:

* Azure App Service
* Azure Key Vault

---

## Current Version

**Version:** v1.0

Completed in this version:

* The platform creates an Entra user through Graph.

### What works today

* Authenticates to Microsoft Graph with OAuth 2.0 using a registered Entra ID application.
* `POST /employees` creates an employee, runs the provisioning orchestrator, and creates the user in Entra ID through Microsoft Graph.
* A user principal name is generated and checked for duplicates in Entra before the user is created.
* An `Idempotency-Key` header prevents duplicate employees on retries.
* Provisioning requests move through tracked state transitions, protected by optimistic locking.
* Every provisioning request, state transition, and business-rule exception is recorded as an audit event in PostgreSQL.
* The Entra object ID is stored on the employee and the provisioning request.
* Unit tests cover the orchestrator, the Graph service, and the Graph user request service.

Group membership and license assignment (the provisioning workflow) and Azure deployment are on the roadmap below.

---

## Roadmap

This project is developed using incremental versioned releases.

Upcoming milestones include:
* Provisioning workflow
* Azure deployment
* Production readiness review

---

## Project Structure

```text
Identity-Provisioning-Platform/

├── src/
├── docs/
├── README.md
├── CHANGELOG.md
└── .gitignore
```

---

## Additional Documentation

Detailed documentation for this project can be found inside the '/docs' directory.

* Architecture
* Decisions
* Security
* Workflow

---

## Getting Started

### Prerequisites

* Java 21
* Maven
* PostgreSQL
* Git
* Postman 

---

### Running the Application

1. Clone the repository.
2. Configure the PostgreSQL database.
3. Register the application in a tenant.
4. Update the application configuration.
5. Run the Spring Boot application.
6. Verify the application starts successfully.
7. Add Idempotency key in Postman header.
8. Use Postman to create an Entra user using /employees.

---

### Configuration

The application reads its settings from environment variables, so no credentials are stored in the repository. Set these before starting the application (the values below are placeholders):

| Variable | Purpose | Example |
| --- | --- | --- |
| `DB_URL` | PostgreSQL JDBC connection string | `jdbc:postgresql://localhost:5432/provisioning` |
| `DB_USERNAME` | Database user | `provisioning_user` |
| `DB_PASSWORD` | Database password | `change-me` |
| `GRAPH_TENANT_ID` | Entra ID tenant the app is registered in | `00000000-0000-0000-0000-000000000000` |
| `GRAPH_CLIENT_ID` | Application (client) ID of the app registration | `00000000-0000-0000-0000-000000000000` |
| `GRAPH_CLIENT_SECRET` | Client secret of the app registration | `your-client-secret` |

The app registration needs an application permission that allows it to create users in the tenant, with admin consent granted. Never commit real values. The schema is created by Hibernate on first start.

Start the application with the Maven wrapper:

```bash
./mvnw spring-boot:run
```

---

### Example Request

Creating an employee provisions the matching Entra ID user. The `Idempotency-Key` header is required, and sending the same key again does not create a duplicate.

```http
POST /employees
Idempotency-Key: 7f3c1d2e-demo-0001
Content-Type: application/json

{
  "firstName": "Jane",
  "lastName": "Doe",
  "department": "Engineering",
  "jobTitle": "Software Engineer",
  "salary": 85000
}
```

The response is a snapshot of the created employee.

---
