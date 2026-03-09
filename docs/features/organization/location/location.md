# Location API Reference

## Overview

This document provides a technical reference for the Location module endpoints. It details the available operations for creating and managing physical or remote operating locations specific to a company.

## Prerequisites

- Base URL: `{{base_url}}/vietrecruit/v1/locations`
- All endpoints require a valid JWT `Bearer` token with `COMPANY_ADMIN` or `HR` authority.
- All operations are bounded by the authenticated user's `companyId`.
- Locations enforce **hard deletion**, subject to strict database referential integrity constraint checks.

## Endpoints

### 1. Create Location

Initializes a new operating location for the company.

- **Method:** `POST`
- **Path:** `/`
- **Rate Limit:** `mediumTraffic` (5 req / 30s)
- **Request Body:** `LocationRequest`
    - `name` (string, required, max 255)
    - `address` (string, optional)
- **Success:** `201 Created` (`ApiSuccessCode.LOCATION_CREATE_SUCCESS`) -> `LocationResponse`
- **Errors:** `409 CONFLICT` if a location with the exact `name` already exists within the company.

### 2. List Locations

Retrieves a paginated catalogue of all locations associated with the company.

- **Method:** `GET`
- **Path:** `/`
- **Rate Limit:** `mediumTraffic` (25 req / 30s)
- **Query Parameters:**
    - `page` (integer, default: 0)
    - `size` (integer, default: 20)
    - `sort` (string, default: `name,asc`)
- **Success:** `200 OK` (`ApiSuccessCode.LOCATION_LIST_SUCCESS`) -> `Page<LocationResponse>`

### 3. Get Location Detail

Retrieves the specifics of a targeted location.

- **Method:** `GET`
- **Path:** `/{id}`
- **Rate Limit:** `mediumTraffic`
- **Path Variable:** `id` (UUID)
- **Success:** `200 OK` (`ApiSuccessCode.LOCATION_FETCH_SUCCESS`) -> `LocationResponse`
- **Errors:** `404 NOT_FOUND` if missing or assigned to an unauthorized company.

### 4. Update Location

Applies modifications to an existing location record.

- **Method:** `PUT`
- **Path:** `/{id}`
- **Rate Limit:** `mediumTraffic`
- **Request Body:** `LocationRequest` (Same constraints as create)
- **Success:** `200 OK` (`ApiSuccessCode.LOCATION_UPDATE_SUCCESS`) -> `LocationResponse`
- **Errors:** `409 CONFLICT` if the prospective `name` conflicts with a sister location inside the company context.

### 5. Delete Location

Executes a **hard delete** on the targeted location.

- **Method:** `DELETE`
- **Path:** `/{id}`
- **Rate Limit:** `mediumTraffic`
- **Success:** `200 OK` (`ApiSuccessCode.LOCATION_DELETE_SUCCESS`) -> `null`
- **Errors:** `400 BAD_REQUEST` if the database throws a foreign key `DataIntegrityViolationException`, explicitly dictating that the location cannot be destroyed because it is bound to existing job listings.
