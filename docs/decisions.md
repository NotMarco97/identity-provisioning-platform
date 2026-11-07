# Design Philosophy

---

The design principles that guided this project were how, why, and trade-offs. This approach was taken to evaluate design that satisfies its purpose with consideration of trade-offs.

---

# Release v1.0

---
### Decision
EmployeeService does not communicate with Graph. All Graph payload construction lives in GraphUserRequestService.

### Why
Enforces decoupling by separating business logic between each system interaction. Separates responsibility to prevent the Caller/HR from waiting for the full provisioning cycle for a response.

### Trade-offs
### Pros
* Decoupling allows easier updates
* HR/Caller does not wait on the completed lifecycle for a response
* Even if Graph fails during provisioning, HR/Caller receives an employee response

### Cons
* Adds complexity because EmployeeService does not communicate with Graph directly


---

### Decision
The Employee entity is the source of truth for the Entra object ID.

### Why
The Employee entity has ownership of the Entra object ID because it is the reference of the live object in Entra ID.

### Trade-offs
### Pros
* Clear boundary between Employee and ProvisioningRequest use of the Entra object ID.
* Decouples provisioning history from the employee to preserve it if an offboarding occurs

---
### Decision
The Entra object ID is stored in the Employee and ProvisioningRequest entities.

### Why
Both are required because the Entra object ID must correlate to a ProvisioningRequest state, while the Employee entity needs the identifier that correlates to the Entra object ID (user).

### Trade-offs
### Pros
* The Entra user has an immutable state of its provisioning lifecycle using their Entra object ID as an association
* The provisioned Entra user lives in the Employee entity via the Entra object ID

### Cons
* Added complexity

---

# Release v0.9
### Decision
Implemented OAuth 2.0 using the Microsoft Authentication Library for Java.

### Why
To take advantage of Microsoft's automated token cache.

### Trade-offs
### Pros
- Reduces complexity

---

# Release v0.8
- None

# Release v0.7
### Decision
The transitionTo method in ProvisioningRequestService implemented optimistic locking.

### Why
To resolve a race condition where the orchestrator and a future addition, such as a watchdog, would cause overwrites in the state transition.

### Trade-offs

### Pros
- No overwrites
- The orchestrator is aware of being locked out
- No duplicate failed states when the orchestrator is locked out

### Cons
- A FAILED state transition may live in the database even if the lifecycle is completed
- Currently, human intervention is needed to resolve this rare occurrence

---

### Decision
An HR/caller generates and owns the idempotency key. IdempotencyRecord persists the provided key.

### Why
To prevent a duplicate employee and a race condition from happening.

### Trade-offs

### Pros
- Established ownership boundaries
- Prevents duplicate employees
- A persistent record of each idempotency key

### Cons
- The caller must provide a key

---

### Decision
The business logic for the persistent idempotency key lives in its own service.

### Why
Promotes decoupling and allows changes from other services to not impact the key's business logic.

---

### Decision
HR/caller receives a snapshot of an employee response associated with the idempotency record.

### Why
The caller only cares about the employee's creation.

### Trade-offs

### Pros
- The caller receives an unchanged response
- Any changes when the caller has not received a response get discarded from the response
- Decoupled idempotency record to preserve a response

### Cons
- Complexity

---

### Decision
The orchestrator does not need persistence/entity.

### Why
The orchestrator is a pure coordinator to wire the platform's lifecycle.

---

### Decision
The fake Graph provider provides custom exceptions and does not mock the real mechanics for groups and licenses.

### Why
The platform does not have ownership of assigning the access plates to an employee. Microsoft Graph owns this mechanic.

---

### Decision
The orchestrator must be called before the markCompleted state in the createEmployee mapping.

### Why
Prevents a bug where the orchestrator might never be called if the idempotency record state is marked as completed.

---

### Decision
Unit testing was only based on the orchestrator's outcomes.

### Why
Other mechanics, such as transitioning through other states, have been unit tested previously. The orchestrator trusts those services.


---

# Release v0.6

### Decision
Did not implement atomicity in the provisioning request business logic.

### Why
Audit events are wired in the provisioning request business logic, and they must live within failures.

---

### Decision
The audit event service is decoupled from the provisioning request.

### Why
To keep single responsibility and have the audit event retain ownership of all events.

### Trade-offs

### Pros
- Decouples responsibility
- Makes maintainability easier
- Easier to modify

---

# Release v0.5

### Decision
The provisioning request is an entity.

### Why
The entity is persistent because it must keep track of its legal states.

---

### Decision
Idempotency was added to the provisioning request.

### Why
The provisioning request must not take another active request of the same employee in progress.

---

### Decision
A synchronous approach was taken for requests.

### Why
It ensures that each state of the provisioning request is completed.

---

# Release v0.4
### None

---

# Release v0.3

---

### Decision
CreateEmployeeRequest fields are required.

### Why
They are the needed fields to begin provisioning.

---

### Decision
GlobalExceptionHandler handles all exceptions.

### Why
Try/catch blocks were repeated in controller methods, with inconsistent responses across endpoints.

### Trade-offs

### Pros
- Decouples responsibility
- Makes maintainability easier
- Easier to modify

---


# Release v0.2

---
### Decision
HR owns employeeId and PostgreSQL owns Id.

### Why
This protects data from being leaked outside ownership boundaries.

### Trade-offs

#### Pros
- Boundaries
- Security

#### Cons
- Requires more maintenance
- Adds complexity to data sharing

---

### Decision
DTO will handle LocalDateTime formatting.

### Why
To control the formatting for HR's sake instead of outputting default LocalDateTime formatting.

---

### Decision
Email generation does not filter inappropriate name combinations.

### Why
Accepted as out of scope.

### Trade-offs

#### Pros
- Reduces complexity

#### Cons
- Manual intervention
- Momentary existence of an inappropriate email


---

# Release v0.1

---

### Decision
Separate HR from provisioning.

### Why
Employee information already exists in an HR system.

---

### Decision
The provisioning platform will focus on onboarding requests instead of managing records.

### Why
This establishes a single source of truth for employee data and keeps the platform focused solely on identity provisioning.

### Trade-offs

#### Pros
- Lower coupling
- Simpler architecture
- Easier maintenance

#### Cons
- Requires integration with an external system
---
