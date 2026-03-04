-- Add implicit casts from varchar to all PostgreSQL native enum types.
-- Required because Hibernate 6 with @Enumerated(EnumType.STRING) sends enum values
-- as VARCHAR, but PostgreSQL has no built-in implicit cast from varchar to custom enums.

CREATE CAST (varchar AS payment_status) WITH INOUT AS IMPLICIT;
CREATE CAST (varchar AS billing_cycle) WITH INOUT AS IMPLICIT;
CREATE CAST (varchar AS subscription_status) WITH INOUT AS IMPLICIT;
CREATE CAST (varchar AS job_status) WITH INOUT AS IMPLICIT;
CREATE CAST (varchar AS application_status) WITH INOUT AS IMPLICIT;
CREATE CAST (varchar AS interview_status) WITH INOUT AS IMPLICIT;
CREATE CAST (varchar AS scorecard_result) WITH INOUT AS IMPLICIT;
CREATE CAST (varchar AS offer_status) WITH INOUT AS IMPLICIT;
