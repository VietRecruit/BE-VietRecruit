# Organizational Structure: Company, Department, Location, and Category

## Overview

This document provides an architectural explanation of the core organizational structure within the VietRecruit system. The structure is composed of four primary entities: **Company**, **Department**, **Location**, and **Category**. These modules serve as foundational layers that the Job module and other business processes depend upon to enforce data isolation, hierarchical validation, and resource scoping.

This document adheres to the **Explanation** quadrant of the Diátaxis framework, detailing the design decisions, behavioral constraints, and lifecycle policies governing these entities.

## Prerequisites

- Read access to the database schema definitions (`V2`, `V4`, `V5`, `V6` migrations).
- Understanding of the global `BaseController` abstraction for multi-tenant context resolution.
- Familiarity with the `User` entity's `companyId` representation.

## Core Entities and Isolation

VietRecruit employs a company-scoped multi-tenancy model. The `Company` acts as the root boundary for employer data.

### Company

The `Company` entity is the central anchor around which all employer-related data revolves.

- **Data Ownership:** A company does not "belong" to an admin; rather, `COMPANY_ADMIN` and `HR` users belong to a `Company` via the `User.companyId` field.
- **Creation Lifecycle:** Companies are provisioned by system setups or out-of-band administrative actions. Public API endpoints do not expose direct company creation.
- **Uniqueness Check:** The `domain` field enforces uniqueness across the entire system.
- **Operations:** Authenticated users belonging to a company can query and partially update their own company profile using the `/vietrecruit/companies/me` endpoint.

### Department, Location, and Category

These entities are hard requirements for job creation and represent internal organizational structures.

- **Tenant Isolation:** Every `Department`, `Location`, and `Category` holds a strict, non-nullable `company_id` foreign key.
- **Resource Scope:** When fetching, creating, or modifying these entities, the service layer strictly enforces that operations are constrained to the current authenticated user's `companyId`. A user can never access or modify a department belonging to another company.
- **Name Resolution:** Names must be unique **within the scope of a single company**. The system allows two different companies to both have a "Marketing" department or a "Software Engineering" category.

## Multi-Tenant Context Resolution

To enforce secure data isolation without redundant code, the system utilizes an automated context resolution strategy at the controller level.

### The `BaseController` Abstraction

All endpoints for the organizational structures extend `BaseController`, which provides the `resolveCompanyId()` utility.

1. **Extraction:** The current user's UUID is extracted from the stateless JWT via `SecurityUtils.getCurrentUserId()`.
2. **Database Lookup:** The `BaseController` retrieves the `User` entity.
3. **Verification:** The controller extracts the user's `companyId`. If `null`, it throws a 403 `FORBIDDEN` exception, preventing access to employer-centric workflows.
4. **Service Delegation:** Controllers pass the resolved `companyId` down into the service limits to fulfill data fencing.

## Deletion Behaviors (Soft vs. Hard Deletes)

The system treats entity deletions differently based on their referential impact on historical data, specifically job postings.

### Soft Deletion: Departments

If a department is deleted, historical job postings that belonged to that department must still retain their structural integrity for reporting or auditing.

- **Implementation:** The `departments` table includes a `deleted_at` timestamp.
- **Lifecycle:** Calling `DELETE` acts as a soft-delete mechanism by populating `deleted_at`.
- **Query Impact:** Jobs querying for departments will still resolve them at the database level because they reference raw UUIDs. The CRUD endpoints actively filter out any entities where `deleted_at IS NOT NULL`.

### Hard Deletion: Locations and Categories

Locations and Categories are treated as strict operational dependencies.

- **Implementation:** Neither table possesses a `deleted_at` column.
- **Referential Integrity:** The database enforces foreign key constraints (`REFERENCES locations(id)`, `REFERENCES categories(id)`).
- **Lifecycle:** Deletions are hard. If a location or category is referenced by an active or historical `Job`, the database throws a foreign key constraint violation.
- **Exception Handling:** The service layer catches the `DataIntegrityViolationException` and translates it into a 400 `BAD_REQUEST` to the client, explicitly preventing the deletion of resources heavily utilized by the system context.

## Summary of Access Restrictions

- **Creation:** Restricted to authorized users possessing a valid `companyId` context.
- **Read:** Isolated to the user's explicit company. Output lists are inherently filtered.
- **Update:** Requires the entity to exist, belong to the user's company, and not collide names with other active entities in the same scope.
- **Delete:** Varied based on soft-delete capability and external referential attachments.

_If you require specifics on HTTP routes, request bodies, or success/error codes, consult the OpenAPI/Swagger definitions for the API._
