# Workflow

---

### Receive Request
- The platform receives a request from Postman

### Validate Requests
- The platform checks for an existing idempotency key associated with the new employee
- The platform creates a new employee and returns a snapshot of the employee response

### Apply business rules
- Business rules determine how the employee should be provisioned

### Provision Identity
- The orchestrator begins wiring each service to begin provisioning

### Record Provisioning
- Provisioning status and audit information are stored within PostgreSQL

### Rejection Result
- The platform returns invalid requests and an audit event

### Return Result
- The platform finishes provisioning and stores the result in PostgreSQL

### Microsoft Graph
- Call the createEmployee endpoint to provision a user.
