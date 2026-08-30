# Changelog

All notable changes to this project will be documented in this file.

---
### v0.9 - OAuth Authentication & Read-Only Graph Connectivity
### Added
* Added Read-Only employee endpoint
* Added OAuth2.0 implementation
* Added logger for acquiring an access token

### Changed
* none

### Fixed
* none

---


### v0.8 - Application Registration In Entra ID

### Added
* Registered the application platform in Entra ID
* Added a client secret to the application
* Added the client secret to local development configuration

### Changed
* none

### Fixed
* none

### v0.7 - Provisioning Orchestration & Mocked Graph Simulation

### Added
* Added optimistic locking to the transitionTo mechanisms
* Added idempotency key in createEmployee mapping
* Added idempotency entity
* Added idempotency business logic
* Added mocked graph provider business logic
* Added orchestrator business logic
* Wired provisioning requests in the orchestrator
* Wired employee service in the orchestrator 
* Wired provisioning plan resolver in the orchestrator
* Wired graph service in the orchestrator
* Wired the orchestrator trigger in createEmployee mapping
* Added multiple exceptions that the mocked graph triggers
* Added unit testing for each exception state in the orchestrator 
* Added unit testing for the happy path in the orchestrator

### Changed
* none

### Fixed
* Classes use the interface instead of the implementation
* Fixed a race condition where provisioning requests overwrite state transitions

---

### v0.6

### Added
* Added audit event entity
* Added audit event interface
* Added audit event business logic
* Added audit event repository
* Wired audit events in provisioning requests
* Wired audit events in request state transitions
* Wired audit events in provisioning business logic exceptions
* Added unit testing for any audit event

### Changed
* none

### Fixed
* Minor documentation details

## v0.5

### Added

* Added Provisioning request entity
* Added Provisioning request business logic
* Added provisioning request interface
* Added provisioning request repository
* Added provisioning request unit tests

### Changed
* Minor documentation fixes

### Fixed
* none

## v0.4

### Added

* Added access template
* Added provisioning plan resolver
* Added provisioning plan
* Added unit tests for provisioning plan

### Changed
* none

### Fixed
* none

---

## v0.3

### Added
* Added Global Exception handler
* Added error exception DTO
* Added unit tests for business logic
* Added unit tests for employee controller

### Changed
* none

### Fixed
* Service implementation was replaced for the interface in controller

---

## v0.2

### Added

* Added Identities
* Added Service Interface
* Added Service Implementation
* Added DTOs
* Added Controller
* Added Enum for Employee status
* Implemented persistence
* Added CRUD endpoints

### Changed

* None

### Fixed

* Inconsistent documentation
* Inconsistent datatypes with Employee and DTOs
* updatedAt setter assigned the wrong field

---

## v0.1

### Added

* Created Spring Boot project
* Initialized local Git repository
* Created GitHub repository
* Configured PostgreSQL database connection
* Added application health check endpoint
* Verified successful application startup
* Added initial project documentation
* Added Git ignore configuration.
### Changed

* None

### Fixed

* None
---





