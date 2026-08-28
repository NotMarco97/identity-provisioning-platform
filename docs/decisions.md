
# Design Philosophy

---

The design principles that guided this project were how, why, and trade-offs. This approach was taken to evaluate design that satisfies its purpose with consideration to trade-offs. 

---
# Release v0.7
### Decision
transitionTo method in ProvisioningRequestService - implemented optimistic locking.

### Why
To resolve a race condition where the orchestrator and a future addition such as a watchdog, would cause overwrites in the state transition.

### Trade-offs

### Pros
- No overwrites
- The orchestrator is aware of being locked out
- No duplicate failed states when the orchestrator is locked out

### Cons
- A FAILED state transition may live in the database even if the lifecycle is completed
- Currently, human intervention is needed to resolve this rare occurence

---

### Decision 
An HR/caller generates and owns the idempotency key - IdempotencyRecord persists the provided key.

### Why
To prevent a duplicate employee and race condition from happening.

### Trade-Offs

### Pros
- Established ownership boundaries
- Prevents duplicate employees
- A persistent record of each idempotency key

### Cons
- The caller must provide a key

---

### Decision
The business logic for the persistent idempotency key, lives in its own service. 

### Why
Promotes decoupling and allows changes from other services to not impact the key's business logic. 

---

### Decision
HR/caller receives a snapshot of an employee response associated with the idempotency record.

### Why
The caller only cares about the employee's creation.

### Trade-offs

### Pros
- The caller receives a non changed response
- Any changes when the caller has not received a respone, get discarded from the response
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
Fake graph provider provides custom exceptions - does not mock the real mechanics for groups & licenses.

### Why
The platform does have ownership of assigning the access plates to an employee - Microsoft Graph owns this mechanic.

---

### Decision 
Orchestrator must be called before markCompleted state in createEmployee mapping.

### Why
Prevents a bug where the orchestrator might never be called if the idempotency record state is marked as completed. 

---

### Decision
Unit testing was only based of the orchestrator's outcomes.

### Why
Other mechanics such as transitioning through other states have been unit tested previously - orchestrator trusts those services. 


---

# Release v0.6

### Decision
Did not implement atomicity in the provisioning request business logic.

### Why
Audit events are wired in provisioning request business logic and they must live within failures.

---

### Decision
Audit event service is decoupled from provisioning request.

### Why
To keep single responsibility and have audit event retain ownership of all events.

### Trade-offs

### pros
- Decouples responsibility
- Makes maintainability easier
- Easier to modify

---

# Release v0.5

### Decision
Provisioning request is an entity.

### Why
The entity is persistent because it must keep track of its legal states.

---

### Decision
Idempotency was added to provisioning request.

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
Try/catch blocks repeated in controller methods, with inconsistent responses across endpoints.

### Trade-offs

### pros
- Decouples responsibility
- Makes maintainability easier
- Easier to modify
___


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




