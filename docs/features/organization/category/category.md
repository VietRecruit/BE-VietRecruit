# Category API Reference

## Overview

This document provides a technical reference for the Category module endpoints. It details the operations available for defining the role classifications (e.g., Software Engineering, Marketing) utilized by a specific company for their job postings.

## Prerequisites

- Base URL: `{{base_url}}/vietrecruit/v1/categories`
- All endpoints require a valid JWT `Bearer` token with `COMPANY_ADMIN` or `HR` authority.
- All operations are bounded tightly to the authenticated user's `companyId`.
- Categories enforce **hard deletion**, monitored via database foreign key constraints.

## Endpoints

### 1. Create Category

Establishes a new organizational category isolated to the employer's company.

- **Method:** `POST`
- **Path:** `/`
- **Rate Limit:** `mediumTraffic` (5 req / 30s)
- **Request Body:** `CategoryRequest`
    - `name` (string, required, max 255)
- **Success:** `201 Created` (`ApiSuccessCode.CATEGORY_CREATE_SUCCESS`) -> `CategoryResponse`
- **Errors:** `409 CONFLICT` if a category matching the `name` is already registered under the same company.

### 2. List Categories

Outputs a paginated list encompassing all custom categories the company has defined.

- **Method:** `GET`
- **Path:** `/`
- **Rate Limit:** `mediumTraffic` (25 req / 30s)
- **Query Parameters:**
    - `page` (integer, default: 0)
    - `size` (integer, default: 20)
    - `sort` (string, default: `name,asc`)
- **Success:** `200 OK` (`ApiSuccessCode.CATEGORY_LIST_SUCCESS`) -> `Page<CategoryResponse>`

### 3. Get Category Detail

Retrieves the metadata representing a specific category.

- **Method:** `GET`
- **Path:** `/{id}`
- **Rate Limit:** `mediumTraffic`
- **Path Variable:** `id` (UUID)
- **Success:** `200 OK` (`ApiSuccessCode.CATEGORY_FETCH_SUCCESS`) -> `CategoryResponse`
- **Errors:** `404 NOT_FOUND` if non-existent or belonging to a divergent company UUID.

### 4. Update Category

Processes modifications to an existing category record.

- **Method:** `PUT`
- **Path:** `/{id}`
- **Rate Limit:** `mediumTraffic`
- **Request Body:** `CategoryRequest`
- **Success:** `200 OK` (`ApiSuccessCode.CATEGORY_UPDATE_SUCCESS`) -> `CategoryResponse`
- **Errors:** `409 CONFLICT` if the updated `name` overlaps with an alternate category present within the company.

### 5. Delete Category

Executes a **hard delete** on the targeted category record from the database.

- **Method:** `DELETE`
- **Path:** `/{id}`
- **Rate Limit:** `mediumTraffic`
- **Success:** `200 OK` (`ApiSuccessCode.CATEGORY_DELETE_SUCCESS`) -> `null`
- **Errors:** `400 BAD_REQUEST` triggered upon a `DataIntegrityViolationException`, indicating the category is actively referenced by one or more job listings and thus cannot be safely eliminated.
