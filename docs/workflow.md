# Workflow

### Receive Request
- The platform receives a request from Postman

### Validate Requests
- The platform validates employee information before any provisioning begins

### Apply business rules
- Business rules determine how the employee should be provisioned

#### Examples include:
- Department
- Job Title
- Role

### Provision Identity
The platform communicates with Postman.

### Record Provisioning
Provisioning status and audit information are stored within PostgreSQL.

### Rejection Result
The platform returns invalid requests and an audit event.

### Return Result
The platform returns the provisioning result to Postman.

### Unit Test
Provisioning plan is displayed in unit tests.
