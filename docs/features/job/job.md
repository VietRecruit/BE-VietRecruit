# Job API Reference

## Overview

This document provides a technical reference for the Job module endpoints. It details the available operations, parameters, and response structures for managing employment listings, both for authenticated employers and public consumption.

## Prerequisites

- Base URL: `{{base_url}}/vietrecruit/v1/jobs`
- All employer endpoints require a valid JWT `Bearer` token with `COMPANY_ADMIN` or `HR` authority.
- Public endpoints do not require authentication.
- Responses conform to the standard `ApiResponse<T>` wrapper.

## Employer Endpoints (Requires Authentication)

### 1. Create Job

Initializes a new job listing in the `DRAFT` state. This action does not consume subscription quota.

- **Method:** `POST`
- **Path:** `/`
- **Rate Limit:** `mediumTraffic` (5 req / 30s)
- **Request Body:** `JobCreateRequest`
    - `title` (string, required, max 255)
    - `description` (string, required)
    - `requirements` (string, optional)
    - `benefits` (string, optional)
    - `salaryRange` (string, optional)
    - `departmentId` (UUID, required) - Must exist and belong to the company
    - `locationId` (UUID, required) - Must exist and belong to the company
    - `categoryId` (UUID, required) - Must exist and belong to the company
- **Success:** `201 Created` (`ApiSuccessCode.JOB_CREATE_SUCCESS`) -> `JobResponse`

### 2. Update Job

Modifies a job listing. **Note:** Only jobs in the `DRAFT` state can be updated.

- **Method:** `PUT`
- **Path:** `/{id}`
- **Rate Limit:** `mediumTraffic`
- **Path Variable:** `id` (UUID) - The ID of the job to update
- **Request Body:** `JobUpdateRequest` (All fields optional, same as Create)
- **Success:** `200 OK` (`ApiSuccessCode.JOB_UPDATE_SUCCESS`) -> `JobResponse`
- **Errors:** `400 BAD_REQUEST` if the job is not in `DRAFT` status.

### 3. Publish Job

Transitions a job from `DRAFT` to `PUBLISHED` state, making it publicly visible. This action strictly deducts from the company's active subscription quota.

- **Method:** `PUT`
- **Path:** `/{id}/publish`
- **Rate Limit:** `mediumTraffic`
- **Success:** `200 OK` (`ApiSuccessCode.JOB_PUBLISH_SUCCESS`) -> `JobResponse`
- **Errors:**
    - `400 BAD_REQUEST` if the job is not in `DRAFT` status.
    - `403 FORBIDDEN` if the company has exceeded its active job limit or has an inactive subscription.

### 4. Close Job

Transitions a job from `PUBLISHED` to `CLOSED` state. This action releases the consumed subscription slot, allowing the employer to publish a different job.

- **Method:** `PUT`
- **Path:** `/{id}/close`
- **Rate Limit:** `mediumTraffic`
- **Success:** `200 OK` (`ApiSuccessCode.JOB_CLOSE_SUCCESS`) -> `JobResponse`
- **Errors:** `400 BAD_REQUEST` if the job is not in `PUBLISHED` status.

### 5. List Jobs

Retrieves a paginated list of all active (non-soft-deleted) jobs belonging to the authenticated user's company, regardless of their status (`DRAFT`, `PUBLISHED`, `CLOSED`).

- **Method:** `GET`
- **Path:** `/`
- **Rate Limit:** `mediumTraffic` (25 req / 30s)
- **Query Parameters:**
    - `page` (integer, default: 0) - Zero-based page index.
    - `size` (integer, default: 20) - Items per page.
    - `sort` (string, default: `createdAt,desc`) - Sort instruction.
- **Success:** `200 OK` (`ApiSuccessCode.JOB_LIST_SUCCESS`) -> `PageResponse<JobSummaryResponse>`

### 6. Get Job Detail

Retrieves the full details of a specific job belonging to the authenticated user's company.

- **Method:** `GET`
- **Path:** `/{id}`
- **Rate Limit:** `mediumTraffic`
- **Success:** `200 OK` (`ApiSuccessCode.JOB_FETCH_SUCCESS`) -> `JobResponse`
- **Errors:** `404 NOT_FOUND` if the job doesn't exist or belongs to another company.

---

## Public Endpoints (No Authentication Required)

These endpoints are used by candidates or visitors to view active job listings. They are prefixed with `/public`.

### 1. List Public Jobs

Retrieves a paginated, filtered list of jobs across the entire platform that are strictly in the `PUBLISHED` state and have not been soft-deleted.

- **Method:** `GET`
- **Path:** `/public`
- **Rate Limit:** `mediumTraffic` (25 req / 30s)
- **Query Parameters:**
    - `page` (integer, default: 0)
    - `size` (integer, default: 20)
    - `sort` (string, default: `createdAt,desc`)
    - `categoryId` (UUID, optional) - Filter by exact category.
    - `locationId` (UUID, optional) - Filter by exact location.
    - `keyword` (string, optional) - Perform case-insensitive `LIKE` search on job `title` or company `name`.
- **Success:** `200 OK` (`ApiSuccessCode.JOB_LIST_SUCCESS`) -> `PageResponse<JobSummaryResponse>`

### 2. Get Public Job Detail

Retrieves the full detail of a specific public job. The system guarantees that only jobs in the `PUBLISHED` status are visible.

- **Method:** `GET`
- **Path:** `/public/{id}`
- **Rate Limit:** `mediumTraffic`
- **Success:** `200 OK` (`ApiSuccessCode.JOB_FETCH_SUCCESS`) -> `JobResponse`
- **Errors:** `404 NOT_FOUND` if the job does not exist, is soft-deleted, or is NOT in the `PUBLISHED` state.
