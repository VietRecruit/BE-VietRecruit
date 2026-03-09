# Department API Reference

## Overview

This document provides a technical reference for the Department module endpoints. It details the available operations for creating and managing company-specific organizational departments.

## Prerequisites

- Base URL: `{{base_url}}/vietrecruit/v1/departments`
- All endpoints require a valid JWT `Bearer` token with `COMPANY_ADMIN` or `HR` authority.
- All operations are strictly segregated by the authenticated user's `companyId`.
- Departments support **soft deletion**.

## Endpoints

### 1. Create Department

Creates a new department within the authenticated user's company context.

- **Method:** `POST`
- **Path:** `/`
- **Rate Limit:** `mediumTraffic` (5 req / 30s)
- **Request Body:** `DepartmentRequest`
    - `name` (string, required, max 255)
    - `description` (string, optional)
- **Success:** `201 Created` (`ApiSuccessCode.DEPARTMENT_CREATE_SUCCESS`) -> `DepartmentResponse`
- **Errors:** `409 CONFLICT` if a department with the exact `name` already exists within the current company (excluding soft-deleted ones).

### 2. List Departments

Retrieves a paginated list of all active (non-deleted) departments belonging to the user's company.

- **Method:** `GET`
- **Path:** `/`
- **Rate Limit:** `mediumTraffic` (25 req / 30s)
- **Query Parameters:**
    - `page` (integer, default: 0)
    - `size` (integer, default: 20)
    - `sort` (string, default: `name,asc`)
- **Success:** `200 OK` (`ApiSuccessCode.DEPARTMENT_LIST_SUCCESS`) -> `Page<DepartmentResponse>`

### 3. Get Department Detail

Retrieves an explicit department record.

- **Method:** `GET`
- **Path:** `/{id}`
- **Rate Limit:** `mediumTraffic`
- **Path Variable:** `id` (UUID)
- **Success:** `200 OK` (`ApiSuccessCode.DEPARTMENT_FETCH_SUCCESS`) -> `DepartmentResponse`
- **Errors:** `404 NOT_FOUND` if missing, belongs to another company, or is soft-deleted.

### 4. Update Department

Mutates an existing department's details.

- **Method:** `PUT`
- **Path:** `/{id}`
- **Rate Limit:** `mediumTraffic`
- **Request Body:** `DepartmentRequest` (Same constraints as create)
- **Success:** `200 OK` (`ApiSuccessCode.DEPARTMENT_UPDATE_SUCCESS`) -> `DepartmentResponse`
- **Errors:** `409 CONFLICT` if the updated `name` collides with another existing department in the same company.

### 5. Delete Department

Performs a **soft delete** on the department. The record remains in the database with `deleted_at` populated, preserving referential integrity for historical Job data.

- **Method:** `DELETE`
- **Path:** `/{id}`
- **Rate Limit:** `mediumTraffic`
- **Success:** `200 OK` (`ApiSuccessCode.DEPARTMENT_DELETE_SUCCESS`) -> `null`
