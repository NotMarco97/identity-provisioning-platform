# Security

---

## Purpose

This document describes how the platform authenticates, authorizes, stores sensitive information, and communicates with external services.

---

### Authentication

- Registered application with a client secret

### Authorization 
- Application is authorized to read-only

### Secret Management
- Local development configuration.

### Secure Communication
- Not implemented yet

### Error Handling
- The platform does not leak internal details in error responses
- Idempotency key prevents a duplicate employees

### Security Boundaries

- HR system owns employee data
- Microsoft Graph exposes identity management API
- Platform orchestrates provisioning