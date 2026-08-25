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
<img width="882" height="745" alt="highLevel drawio" src="https://github.com/user-attachments/assets/c02f3b6b-699a-451b-b4ed-f3bd31e9c492" />



---

### Scope
The model illustrates the interaction between the identity platform and externals.

### Key Interactions
- HR/Caller interacts with the platform's endpoints
- PostgreSQL stores the platform's internals
- Microsoft Graph receives the platform's requests

---

## Provisioning Lifecycle







