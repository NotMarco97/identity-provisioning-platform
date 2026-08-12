
# Design Philosophy

---

## Design goals

The design principles that guided this project were how, why, and trade-offs. This approach was taken to evaluate design that satisfies its purpose with consideration to trade-offs. 

---

# Release v0.1

---

### Decision 
Separate HR from provisioning

### Why
Employee information already exist in an HR system.

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

