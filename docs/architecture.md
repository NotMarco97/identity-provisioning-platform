# How the system is built

## Purpose

The responsibility of this platform is to automate employee identity provisioning by validating onboarding requests, applying business rules, provisioning identities, and recording provisioning states.The platform is designed to be scalable, maintainable, and resilient while providing auditing and error handling.

#### The platform is not responsible for:

- Managing HR employee records
- Replacing Microsoft Entra ID
- Managing employee lifecycle outside provisioning

---

## System Context

### This application interacts with:

- HR Information System
- Microsoft Graph
- PostgreSQL

---

## High-Level Architecture
![High-level architecture.png](diagrams/High-level%20architecture.png)


---

### Scope
The model illustrates the interaction between the identity platform and externals.

### Key Interactions
- HR/Caller interacts with the platform's endpoints
- PostgreSQL stores the platform's internals
- Microsoft Graph receives the platform's requests

---

## Provisioning Lifecycle
![Provisioning Lifecycle.png](diagrams/Provisioning%20Lifecycle.png)

