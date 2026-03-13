-- ============================================================
-- VietRecruit | Migration V99__seed_data.sql
-- Description: Narrative seed data — platform snapshot 3 months
--              after launch. See individual sections for story.
-- Depends on:  all tables (V1–V25)
-- ============================================================

-- ============================================================
-- 1. ROLES
-- ============================================================
INSERT INTO roles (code, name) VALUES
    ('SYSTEM_ADMIN',    'System Administrator'),
    ('COMPANY_ADMIN',   'Company Administrator'),
    ('HR',              'Human Resources'),
    ('INTERVIEWER',     'Interviewer'),
    ('CANDIDATE',       'Candidate'),
    ('CUSTOMER_SERVICE','Customer Service');

-- ============================================================
-- 2. PERMISSIONS
-- ============================================================
INSERT INTO permissions (code, name, module) VALUES
    ('JOB:CREATE',          'Create Job Posting',            'JOB'),
    ('JOB:VIEW',            'View Job Posting',              'JOB'),
    ('JOB:EDIT',            'Edit Job Posting',              'JOB'),
    ('JOB:DELETE',          'Delete Job Posting',            'JOB'),
    ('JOB:PUBLISH',         'Publish Job Posting',           'JOB'),
    ('JOB:CLOSE',           'Close Job Posting',             'JOB'),
    ('JOB:VIEW_ALL',        'View All Job Postings',         'JOB'),
    ('CANDIDATE:VIEW',      'View Candidate Profile',        'CANDIDATE'),
    ('CANDIDATE:EDIT',      'Edit Candidate Profile',        'CANDIDATE'),
    ('APPLICATION:VIEW',    'View Application',              'APPLICATION'),
    ('APPLICATION:MANAGE',  'Manage Application Status',     'APPLICATION'),
    ('INTERVIEW:SCHEDULE',  'Schedule Interview',            'INTERVIEW'),
    ('INTERVIEW:VIEW',      'View Interview',                'INTERVIEW'),
    ('INTERVIEW:EVALUATE',  'Evaluate Interview',            'INTERVIEW'),
    ('OFFER:CREATE',        'Create Offer',                  'OFFER'),
    ('OFFER:VIEW',          'View Offer',                    'OFFER'),
    ('OFFER:SEND',          'Send Offer',                    'OFFER'),
    ('COMPANY:MANAGE',      'Manage Company Settings',       'COMPANY'),
    ('USER:MANAGE',         'Manage Users',                  'USER'),
    ('USER:DELETE',         'Delete Users',                  'USER'),
    ('PLAN:VIEW',           'View Plans',                    'PLAN'),
    ('PLAN:MANAGE',         'Manage Plans',                  'PLAN'),
    ('SUBSCRIPTION:VIEW',   'View Subscriptions',            'SUBSCRIPTION'),
    ('SUBSCRIPTION:MANAGE', 'Manage Subscriptions',          'SUBSCRIPTION'),
    ('TRANSACTION:VIEW_ALL','View All Transaction Records',  'PAYMENT');

-- ============================================================
-- 3. ROLE-PERMISSION ASSIGNMENTS
-- ============================================================

-- SYSTEM_ADMIN: all permissions
INSERT INTO role_permissions (role_id, permission_id)
    SELECT r.id, p.id FROM roles r, permissions p WHERE r.code = 'SYSTEM_ADMIN';

-- COMPANY_ADMIN: everything except USER:DELETE, PLAN:MANAGE
INSERT INTO role_permissions (role_id, permission_id)
    SELECT r.id, p.id FROM roles r, permissions p
    WHERE r.code = 'COMPANY_ADMIN' AND p.code NOT IN ('USER:DELETE', 'PLAN:MANAGE', 'TRANSACTION:VIEW_ALL');

-- HR: job + candidate + application + interview + offer (no user/company mgmt)
INSERT INTO role_permissions (role_id, permission_id)
    SELECT r.id, p.id FROM roles r, permissions p
    WHERE r.code = 'HR' AND p.code IN (
        'JOB:CREATE','JOB:VIEW','JOB:EDIT','JOB:DELETE','JOB:PUBLISH','JOB:CLOSE','JOB:VIEW_ALL',
        'CANDIDATE:VIEW','CANDIDATE:EDIT',
        'APPLICATION:VIEW','APPLICATION:MANAGE',
        'INTERVIEW:SCHEDULE','INTERVIEW:VIEW','INTERVIEW:EVALUATE',
        'OFFER:CREATE','OFFER:VIEW','OFFER:SEND',
        'PLAN:VIEW','SUBSCRIPTION:VIEW');

-- INTERVIEWER: view jobs, view candidates, view/evaluate interviews
INSERT INTO role_permissions (role_id, permission_id)
    SELECT r.id, p.id FROM roles r, permissions p
    WHERE r.code = 'INTERVIEWER' AND p.code IN (
        'JOB:VIEW','CANDIDATE:VIEW','APPLICATION:VIEW',
        'INTERVIEW:VIEW','INTERVIEW:EVALUATE');

-- CANDIDATE: view jobs, view own applications/interviews/offers
INSERT INTO role_permissions (role_id, permission_id)
    SELECT r.id, p.id FROM roles r, permissions p
    WHERE r.code = 'CANDIDATE' AND p.code IN (
        'JOB:VIEW','APPLICATION:VIEW','INTERVIEW:VIEW','OFFER:VIEW');

-- CUSTOMER_SERVICE: transaction viewing
INSERT INTO role_permissions (role_id, permission_id)
    SELECT r.id, p.id FROM roles r, permissions p
    WHERE r.code = 'CUSTOMER_SERVICE' AND p.code = 'TRANSACTION:VIEW_ALL';

-- ============================================================
-- 4. COMPANIES
-- ============================================================
INSERT INTO companies (id, name, domain, website) VALUES
    ('a1000000-0000-0000-0000-000000000001', 'TechViet JSC',        'techviet.com',   'https://techviet.com'),
    ('a1000000-0000-0000-0000-000000000002', 'GreenBuild Vietnam',  'greenbuild.vn',  'https://greenbuild.vn');

-- ============================================================
-- 5. SUBSCRIPTION PLANS
-- ============================================================
INSERT INTO subscription_plans (id, code, name, description, max_active_jobs, job_duration_days, resume_access, ai_matching, priority_listing, price_monthly) VALUES
    ('b1000000-0000-0000-0000-000000000001', 'FREE',       'Free',       'Bắt đầu với đăng tuyển cơ bản',                1,  15, FALSE, FALSE, FALSE, 0),
    ('b1000000-0000-0000-0000-000000000002', 'BASIC',      'Basic',      'Công cụ thiết yếu cho đội ngũ đang phát triển', 5,  30, TRUE,  FALSE, FALSE, 990000),
    ('b1000000-0000-0000-0000-000000000003', 'PREMIUM',    'Premium',    'Tính năng nâng cao cho nhà tuyển dụng',        20,  60, TRUE,  TRUE,  TRUE,  2490000),
    ('b1000000-0000-0000-0000-000000000004', 'ENTERPRISE', 'Enterprise', 'Truy cập không giới hạn cho tuyển dụng quy mô lớn', -1, 90, TRUE, TRUE, TRUE, 5990000);

-- ============================================================
-- 6. USERS (password = bcrypt of 'Abc@123456')
-- ============================================================
-- Pre-computed BCrypt hash for Abc@123456
-- $2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG

INSERT INTO users (id, company_id, email, password_hash, full_name, phone, email_verified, is_active) VALUES
    -- System Admin
    ('c1000000-0000-0000-0000-000000000001', NULL,
     'admin@vietrecruit.vn', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG',
     'Nguyễn Văn Admin', '0900000001', TRUE, TRUE),
    -- TechViet JSC staff
    ('c1000000-0000-0000-0000-000000000002', 'a1000000-0000-0000-0000-000000000001',
     'hoa.tran@techviet.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG',
     'Trần Thị Hoa', '0900000002', TRUE, TRUE),
    ('c1000000-0000-0000-0000-000000000003', 'a1000000-0000-0000-0000-000000000001',
     'khoa.le@techviet.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG',
     'Lê Minh Khoa', '0900000003', TRUE, TRUE),
    ('c1000000-0000-0000-0000-000000000004', 'a1000000-0000-0000-0000-000000000001',
     'lan.pham@techviet.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG',
     'Phạm Thị Lan', '0900000004', TRUE, TRUE),
    ('c1000000-0000-0000-0000-000000000005', 'a1000000-0000-0000-0000-000000000001',
     'anh.nguyen@techviet.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG',
     'Nguyễn Đức Anh', '0900000005', TRUE, TRUE),
    -- GreenBuild Vietnam staff
    ('c1000000-0000-0000-0000-000000000006', 'a1000000-0000-0000-0000-000000000002',
     'tung.vo@greenbuild.vn', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG',
     'Võ Thanh Tùng', '0900000006', TRUE, TRUE),
    ('c1000000-0000-0000-0000-000000000007', 'a1000000-0000-0000-0000-000000000002',
     'mai.do@greenbuild.vn', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG',
     'Đỗ Thị Mai', '0900000007', TRUE, TRUE),
    -- Candidates
    ('c1000000-0000-0000-0000-000000000008', NULL,
     'binh.hoang@gmail.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG',
     'Hoàng Văn Bình', '0900000008', TRUE, TRUE),
    ('c1000000-0000-0000-0000-000000000009', NULL,
     'cam.nguyen@gmail.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG',
     'Nguyễn Thị Cẩm', '0900000009', TRUE, TRUE),
    ('c1000000-0000-0000-0000-000000000010', NULL,
     'dung.tran@gmail.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG',
     'Trần Quốc Dũng', '0900000010', TRUE, TRUE),
    ('c1000000-0000-0000-0000-000000000011', NULL,
     'emilia.le@gmail.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG',
     'Lê Thị Emilia', '0900000011', TRUE, TRUE),
    ('c1000000-0000-0000-0000-000000000012', NULL,
     'phuc.pham@gmail.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG',
     'Phạm Văn Phúc', '0900000012', TRUE, TRUE);

-- ============================================================
-- 7. USER-ROLE ASSIGNMENTS
-- ============================================================
INSERT INTO user_roles (user_id, role_id) VALUES
    ('c1000000-0000-0000-0000-000000000001', (SELECT id FROM roles WHERE code = 'SYSTEM_ADMIN')),
    ('c1000000-0000-0000-0000-000000000002', (SELECT id FROM roles WHERE code = 'COMPANY_ADMIN')),
    ('c1000000-0000-0000-0000-000000000003', (SELECT id FROM roles WHERE code = 'HR')),
    ('c1000000-0000-0000-0000-000000000004', (SELECT id FROM roles WHERE code = 'INTERVIEWER')),
    ('c1000000-0000-0000-0000-000000000005', (SELECT id FROM roles WHERE code = 'INTERVIEWER')),
    ('c1000000-0000-0000-0000-000000000006', (SELECT id FROM roles WHERE code = 'COMPANY_ADMIN')),
    ('c1000000-0000-0000-0000-000000000007', (SELECT id FROM roles WHERE code = 'HR')),
    ('c1000000-0000-0000-0000-000000000008', (SELECT id FROM roles WHERE code = 'CANDIDATE')),
    ('c1000000-0000-0000-0000-000000000009', (SELECT id FROM roles WHERE code = 'CANDIDATE')),
    ('c1000000-0000-0000-0000-000000000010', (SELECT id FROM roles WHERE code = 'CANDIDATE')),
    ('c1000000-0000-0000-0000-000000000011', (SELECT id FROM roles WHERE code = 'CANDIDATE')),
    ('c1000000-0000-0000-0000-000000000012', (SELECT id FROM roles WHERE code = 'CANDIDATE'));

-- ============================================================
-- 8. DEPARTMENTS
-- ============================================================
INSERT INTO departments (id, company_id, name, description, created_by) VALUES
    ('d1000000-0000-0000-0000-000000000001', 'a1000000-0000-0000-0000-000000000001', 'Kỹ Thuật',  'Phòng kỹ thuật phần mềm',       'c1000000-0000-0000-0000-000000000002'),
    ('d1000000-0000-0000-0000-000000000002', 'a1000000-0000-0000-0000-000000000001', 'Sản Phẩm',  'Phòng quản lý sản phẩm',         'c1000000-0000-0000-0000-000000000002'),
    ('d1000000-0000-0000-0000-000000000003', 'a1000000-0000-0000-0000-000000000001', 'Nhân Sự',   'Phòng nhân sự và tuyển dụng',     'c1000000-0000-0000-0000-000000000002'),
    ('d1000000-0000-0000-0000-000000000004', 'a1000000-0000-0000-0000-000000000002', 'Công Nghệ', 'Phòng công nghệ và phát triển',   'c1000000-0000-0000-0000-000000000006');

-- ============================================================
-- 9. LOCATIONS
-- ============================================================
INSERT INTO locations (id, company_id, name, address, created_by) VALUES
    ('e1000000-0000-0000-0000-000000000001', 'a1000000-0000-0000-0000-000000000001', 'Văn Phòng HCM - Quận 7',        'Tầng 12, Tòa nhà Phú Mỹ Hưng, Quận 7, TP.HCM',          'c1000000-0000-0000-0000-000000000002'),
    ('e1000000-0000-0000-0000-000000000002', 'a1000000-0000-0000-0000-000000000001', 'Remote',                         'Làm việc từ xa toàn thời gian',                            'c1000000-0000-0000-0000-000000000002'),
    ('e1000000-0000-0000-0000-000000000003', 'a1000000-0000-0000-0000-000000000002', 'Văn Phòng Hà Nội - Cầu Giấy',   'Tầng 5, Tòa nhà Duy Tân, Cầu Giấy, Hà Nội',              'c1000000-0000-0000-0000-000000000006');

-- ============================================================
-- 10. CATEGORIES
-- ============================================================
INSERT INTO categories (id, company_id, name, created_by) VALUES
    ('f1000000-0000-0000-0000-000000000001', 'a1000000-0000-0000-0000-000000000001', 'Lập Trình Backend',     'c1000000-0000-0000-0000-000000000002'),
    ('f1000000-0000-0000-0000-000000000002', 'a1000000-0000-0000-0000-000000000001', 'Lập Trình Frontend',    'c1000000-0000-0000-0000-000000000002'),
    ('f1000000-0000-0000-0000-000000000003', 'a1000000-0000-0000-0000-000000000001', 'Lập Trình Full-stack',  'c1000000-0000-0000-0000-000000000002'),
    ('f1000000-0000-0000-0000-000000000004', 'a1000000-0000-0000-0000-000000000001', 'DevOps / Cloud',        'c1000000-0000-0000-0000-000000000002'),
    ('f1000000-0000-0000-0000-000000000005', 'a1000000-0000-0000-0000-000000000001', 'Mobile Development',    'c1000000-0000-0000-0000-000000000002'),
    ('f1000000-0000-0000-0000-000000000006', 'a1000000-0000-0000-0000-000000000001', 'Data Engineering',      'c1000000-0000-0000-0000-000000000002'),
    ('f1000000-0000-0000-0000-000000000007', 'a1000000-0000-0000-0000-000000000001', 'UI/UX Design',          'c1000000-0000-0000-0000-000000000002'),
    ('f1000000-0000-0000-0000-000000000008', 'a1000000-0000-0000-0000-000000000001', 'Product Management',    'c1000000-0000-0000-0000-000000000002'),
    ('f1000000-0000-0000-0000-000000000009', 'a1000000-0000-0000-0000-000000000001', 'QA / Testing',          'c1000000-0000-0000-0000-000000000002'),
    ('f1000000-0000-0000-0000-000000000010', 'a1000000-0000-0000-0000-000000000001', 'Tuyển Dụng / HR',       'c1000000-0000-0000-0000-000000000002'),
    ('f1000000-0000-0000-0000-000000000011', 'a1000000-0000-0000-0000-000000000002', 'Lập Trình Full-stack',  'c1000000-0000-0000-0000-000000000006');

-- ============================================================
-- 11. EMPLOYER SUBSCRIPTIONS
-- TechViet: Premium, activated 45 days ago, expires in 15 days
-- GreenBuild: Basic, activated 10 days ago, expires in 20 days
-- ============================================================
INSERT INTO employer_subscriptions (id, company_id, plan_id, status, started_at, expires_at, billing_cycle) VALUES
    ('11000000-0000-0000-0000-000000000001', 'a1000000-0000-0000-0000-000000000001', 'b1000000-0000-0000-0000-000000000003',
     'ACTIVE', CURRENT_TIMESTAMP - INTERVAL '45 days', CURRENT_TIMESTAMP + INTERVAL '15 days', 'MONTHLY'),
    ('11000000-0000-0000-0000-000000000002', 'a1000000-0000-0000-0000-000000000002', 'b1000000-0000-0000-0000-000000000002',
     'ACTIVE', CURRENT_TIMESTAMP - INTERVAL '10 days', CURRENT_TIMESTAMP + INTERVAL '20 days', 'MONTHLY');

-- ============================================================
-- 12. JOB POSTING QUOTAS
-- ============================================================
INSERT INTO job_posting_quotas (id, subscription_id, jobs_posted, jobs_active, cycle_start, cycle_end) VALUES
    ('12000000-0000-0000-0000-000000000001', '11000000-0000-0000-0000-000000000001', 3, 2,
     CURRENT_TIMESTAMP - INTERVAL '45 days', CURRENT_TIMESTAMP + INTERVAL '15 days'),
    ('12000000-0000-0000-0000-000000000002', '11000000-0000-0000-0000-000000000002', 1, 1,
     CURRENT_TIMESTAMP - INTERVAL '10 days', CURRENT_TIMESTAMP + INTERVAL '20 days');

-- ============================================================
-- 13. PAYMENT TRANSACTIONS
-- ============================================================
INSERT INTO payment_transactions (id, order_code, company_id, plan_id, billing_cycle, amount, status, paid_at) VALUES
    ('13000000-0000-0000-0000-000000000001', 1000001, 'a1000000-0000-0000-0000-000000000001', 'b1000000-0000-0000-0000-000000000003',
     'MONTHLY', 2490000, 'PAID', CURRENT_TIMESTAMP - INTERVAL '45 days'),
    ('13000000-0000-0000-0000-000000000002', 1000002, 'a1000000-0000-0000-0000-000000000002', 'b1000000-0000-0000-0000-000000000002',
     'MONTHLY', 990000, 'PAID', CURRENT_TIMESTAMP - INTERVAL '10 days');

-- ============================================================
-- 14. TRANSACTION RECORDS
-- ============================================================
INSERT INTO transaction_records (id, order_code, company_id, amount, description, reference, transaction_date_time, currency, payos_code, payos_desc) VALUES
    ('14000000-0000-0000-0000-000000000001', 1000001, 'a1000000-0000-0000-0000-000000000001', 2490000,
     'Thanh toán gói Premium - TechViet JSC', 'FT24001234567', CURRENT_TIMESTAMP - INTERVAL '45 days', 'VND', '00', 'Thành công'),
    ('14000000-0000-0000-0000-000000000002', 1000002, 'a1000000-0000-0000-0000-000000000002', 990000,
     'Thanh toán gói Basic - GreenBuild Vietnam', 'FT24009876543', CURRENT_TIMESTAMP - INTERVAL '10 days', 'VND', '00', 'Thành công');

-- ============================================================
-- 15. JOBS
-- ============================================================
INSERT INTO jobs (id, company_id, department_id, location_id, category_id, title, description, requirements, min_salary, max_salary, status, deadline, created_by) VALUES
    ('15000000-0000-0000-0000-000000000001', 'a1000000-0000-0000-0000-000000000001',
     'd1000000-0000-0000-0000-000000000001', 'e1000000-0000-0000-0000-000000000001', 'f1000000-0000-0000-0000-000000000001',
     'Senior Backend Engineer (Java/Spring Boot)',
     E'## Mô tả công việc\nTechViet JSC đang tìm kiếm Senior Backend Engineer để tham gia phát triển hệ thống SaaS phục vụ hơn 10,000 doanh nghiệp. Bạn sẽ thiết kế và xây dựng các microservice xử lý hàng triệu request mỗi ngày.\n\n## Trách nhiệm\n- Thiết kế và phát triển REST API với Spring Boot 3.x\n- Tối ưu hóa hiệu suất database PostgreSQL (query tuning, indexing)\n- Triển khai event-driven architecture với Apache Kafka\n- Code review và mentoring cho junior developers\n\n## Quyền lợi\n- Lương cạnh tranh: 30-45 triệu VND/tháng\n- 13 tháng lương + thưởng performance\n- Bảo hiểm sức khỏe premium cho cả gia đình\n- Flexible working hours, 2 ngày WFH/tuần',
     E'- Tối thiểu 5 năm kinh nghiệm Java/Spring Boot\n- Thành thạo PostgreSQL, Redis, Kafka\n- Kinh nghiệm với Docker, Kubernetes\n- Tiếng Anh giao tiếp tốt',
     30000000, 45000000, 'PUBLISHED', CURRENT_DATE + INTERVAL '30 days', 'c1000000-0000-0000-0000-000000000003'),

    ('15000000-0000-0000-0000-000000000002', 'a1000000-0000-0000-0000-000000000001',
     'd1000000-0000-0000-0000-000000000001', 'e1000000-0000-0000-0000-000000000002', 'f1000000-0000-0000-0000-000000000002',
     'Frontend Engineer (ReactJS)',
     E'## Mô tả công việc\nGia nhập đội ngũ Frontend của TechViet JSC để xây dựng giao diện người dùng cho nền tảng tuyển dụng thế hệ mới. Làm việc remote 100%.\n\n## Trách nhiệm\n- Phát triển UI components với React 18 + TypeScript\n- Tích hợp REST API và WebSocket real-time\n- Đảm bảo responsive design và accessibility\n\n## Quyền lợi\n- Lương: 22-35 triệu VND/tháng\n- Remote 100%, flexible hours\n- Thiết bị làm việc (MacBook Pro)\n- Team building hàng quý',
     E'- 2+ năm kinh nghiệm React/TypeScript\n- Hiểu biết về state management (Redux/Zustand)\n- Kinh nghiệm với TailwindCSS hoặc CSS-in-JS',
     22000000, 35000000, 'PUBLISHED', CURRENT_DATE + INTERVAL '25 days', 'c1000000-0000-0000-0000-000000000003'),

    ('15000000-0000-0000-0000-000000000003', 'a1000000-0000-0000-0000-000000000001',
     'd1000000-0000-0000-0000-000000000001', 'e1000000-0000-0000-0000-000000000001', 'f1000000-0000-0000-0000-000000000004',
     'DevOps Engineer',
     E'## Mô tả công việc\nQuản lý và tối ưu hóa hạ tầng cloud cho toàn bộ hệ thống TechViet.\n\n## Trách nhiệm\n- Thiết kế CI/CD pipelines với GitHub Actions\n- Quản lý Kubernetes clusters trên AWS EKS\n- Monitoring và alerting với Prometheus + Grafana\n\n## Quyền lợi\n- Lương: 28-40 triệu VND/tháng\n- AWS certification sponsorship',
     E'- 3+ năm kinh nghiệm DevOps/SRE\n- Thành thạo Docker, Kubernetes, Terraform\n- Kinh nghiệm AWS hoặc GCP',
     28000000, 40000000, 'DRAFT', CURRENT_DATE + INTERVAL '45 days', 'c1000000-0000-0000-0000-000000000003'),

    ('15000000-0000-0000-0000-000000000004', 'a1000000-0000-0000-0000-000000000002',
     'd1000000-0000-0000-0000-000000000004', 'e1000000-0000-0000-0000-000000000003', 'f1000000-0000-0000-0000-000000000011',
     'Full-stack Developer (Node.js + Vue)',
     E'## Mô tả công việc\nGreenBuild Vietnam tìm kiếm Full-stack Developer để xây dựng platform quản lý dự án xây dựng thông minh.\n\n## Trách nhiệm\n- Phát triển backend API với Node.js/Express\n- Xây dựng dashboard với Vue 3 + Vuetify\n- Tích hợp IoT sensors data\n\n## Quyền lợi\n- Lương: 18-30 triệu VND/tháng\n- Cơ hội làm việc với công nghệ IoT\n- Startup culture, equity options',
     E'- 2+ năm kinh nghiệm Node.js và Vue.js\n- Hiểu biết về database design\n- Bonus: kinh nghiệm IoT hoặc construction-tech',
     18000000, 30000000, 'PUBLISHED', CURRENT_DATE + INTERVAL '20 days', 'c1000000-0000-0000-0000-000000000007');

-- ============================================================
-- 16. CANDIDATES
-- ============================================================
INSERT INTO candidates (id, user_id, headline, summary, skills, years_of_experience, desired_position, desired_salary_min, desired_salary_max, work_type, is_open_to_work, default_cv_url) VALUES
    ('16000000-0000-0000-0000-000000000001', 'c1000000-0000-0000-0000-000000000008',
     'Senior Java Developer | 5 năm kinh nghiệm | Spring Boot, Kafka, AWS',
     'Kỹ sư phần mềm với 5 năm kinh nghiệm chuyên sâu về Java và hệ thống phân tán. Đã từng lead team 4 người tại FPT Software, xây dựng hệ thống xử lý 500K transactions/ngày.',
     ARRAY['Java', 'Spring Boot', 'PostgreSQL', 'Kafka', 'Redis', 'Docker', 'AWS'], 5,
     'Senior Backend Engineer', 30000000, 45000000, 'HYBRID', FALSE,
     'https://storage.vietrecruit.vn/cv/binh-hoang-cv.pdf'),

    ('16000000-0000-0000-0000-000000000002', 'c1000000-0000-0000-0000-000000000009',
     'Frontend Developer | React, TypeScript | 3 năm kinh nghiệm',
     'Chuyên gia frontend với đam mê xây dựng UI/UX xuất sắc. 3 năm kinh nghiệm React tại các startup công nghệ Việt Nam.',
     ARRAY['React', 'TypeScript', 'Next.js', 'TailwindCSS', 'Redux', 'Jest'], 3,
     'Frontend Engineer', 22000000, 32000000, 'REMOTE', TRUE,
     'https://storage.vietrecruit.vn/cv/cam-nguyen-cv.pdf'),

    ('16000000-0000-0000-0000-000000000003', 'c1000000-0000-0000-0000-000000000010',
     'Java Developer | 3 năm | Microservices',
     'Backend developer với kinh nghiệm Java và microservices. Đang tìm cơ hội phát triển tại công ty sản phẩm.',
     ARRAY['Java', 'Spring Boot', 'MySQL', 'Docker', 'Git'], 3,
     'Backend Engineer', 25000000, 35000000, 'ONSITE', TRUE,
     'https://storage.vietrecruit.vn/cv/dung-tran-cv.pdf'),

    ('16000000-0000-0000-0000-000000000004', 'c1000000-0000-0000-0000-000000000011',
     'Full-stack Developer | Node.js, Vue.js | 2 năm',
     'Lập trình viên full-stack với kinh nghiệm Node.js và Vue.js. Tốt nghiệp ĐH Bách Khoa Hà Nội.',
     ARRAY['Node.js', 'Vue.js', 'Express', 'MongoDB', 'PostgreSQL'], 2,
     'Full-stack Developer', 15000000, 25000000, 'ONSITE', TRUE,
     'https://storage.vietrecruit.vn/cv/emilia-le-cv.pdf'),

    ('16000000-0000-0000-0000-000000000005', 'c1000000-0000-0000-0000-000000000012',
     'Junior Frontend Developer | React | 1 năm kinh nghiệm',
     'Mới tốt nghiệp với 1 năm kinh nghiệm thực tập frontend. Nhanh nhẹn, ham học hỏi.',
     ARRAY['React', 'JavaScript', 'HTML', 'CSS', 'Git'], 1,
     'Frontend Developer', 12000000, 18000000, 'ONSITE', TRUE,
     'https://storage.vietrecruit.vn/cv/phuc-pham-cv.pdf');

-- ============================================================
-- 17. APPLICATIONS (pipeline story)
-- Bình → Senior BE → HIRED (2 months ago)
-- Cẩm → Frontend → OFFER (3 weeks ago)
-- Dũng → Senior BE → INTERVIEW (2 weeks ago)
-- Emilia → Full-stack → SCREENING (1 week ago)
-- Phúc → Frontend → REJECTED (3 weeks ago)
-- ============================================================
INSERT INTO applications (id, job_id, candidate_id, applied_cv_url, cover_letter, status, created_at) VALUES
    ('17000000-0000-0000-0000-000000000001', '15000000-0000-0000-0000-000000000001', '16000000-0000-0000-0000-000000000001',
     'https://storage.vietrecruit.vn/cv/binh-hoang-cv.pdf',
     'Tôi có 5 năm kinh nghiệm Java/Spring Boot và rất quan tâm đến vị trí này tại TechViet.',
     'HIRED', CURRENT_TIMESTAMP - INTERVAL '60 days'),

    ('17000000-0000-0000-0000-000000000002', '15000000-0000-0000-0000-000000000002', '16000000-0000-0000-0000-000000000002',
     'https://storage.vietrecruit.vn/cv/cam-nguyen-cv.pdf',
     'Với 3 năm kinh nghiệm React/TypeScript, tôi tin mình phù hợp với vị trí Frontend Engineer.',
     'OFFER', CURRENT_TIMESTAMP - INTERVAL '21 days'),

    ('17000000-0000-0000-0000-000000000003', '15000000-0000-0000-0000-000000000001', '16000000-0000-0000-0000-000000000003',
     'https://storage.vietrecruit.vn/cv/dung-tran-cv.pdf',
     'Tôi muốn ứng tuyển vị trí Senior Backend Engineer để phát triển kỹ năng microservices.',
     'INTERVIEW', CURRENT_TIMESTAMP - INTERVAL '14 days'),

    ('17000000-0000-0000-0000-000000000004', '15000000-0000-0000-0000-000000000004', '16000000-0000-0000-0000-000000000004',
     'https://storage.vietrecruit.vn/cv/emilia-le-cv.pdf',
     'Tôi rất hứng thú với construction-tech và muốn đóng góp cho GreenBuild Vietnam.',
     'SCREENING', CURRENT_TIMESTAMP - INTERVAL '7 days'),

    ('17000000-0000-0000-0000-000000000005', '15000000-0000-0000-0000-000000000002', '16000000-0000-0000-0000-000000000005',
     'https://storage.vietrecruit.vn/cv/phuc-pham-cv.pdf',
     'Tôi muốn ứng tuyển vị trí Frontend Engineer tại TechViet JSC.',
     'REJECTED', CURRENT_TIMESTAMP - INTERVAL '21 days');

-- ============================================================
-- 18. APPLICATION STATUS HISTORY
-- ============================================================
-- Bình: NEW → SCREENING → INTERVIEW → OFFER → HIRED
INSERT INTO application_status_history (application_id, old_status, new_status, notes, changed_by, changed_at) VALUES
    ('17000000-0000-0000-0000-000000000001', NULL,         'NEW',       'Ứng viên nộp hồ sơ qua hệ thống',                                      'c1000000-0000-0000-0000-000000000003', CURRENT_TIMESTAMP - INTERVAL '60 days'),
    ('17000000-0000-0000-0000-000000000001', 'NEW',        'SCREENING', 'CV ấn tượng, có kinh nghiệm với Spring Boot và Kafka. Mời phỏng vấn vòng 1.', 'c1000000-0000-0000-0000-000000000003', CURRENT_TIMESTAMP - INTERVAL '58 days'),
    ('17000000-0000-0000-0000-000000000001', 'SCREENING',  'INTERVIEW', 'Đạt vòng screening. Lên lịch phỏng vấn kỹ thuật.',                       'c1000000-0000-0000-0000-000000000003', CURRENT_TIMESTAMP - INTERVAL '55 days'),
    ('17000000-0000-0000-0000-000000000001', 'INTERVIEW',  'OFFER',     'Phỏng vấn xuất sắc cả 2 vòng. Đề xuất offer 35 triệu.',                 'c1000000-0000-0000-0000-000000000003', CURRENT_TIMESTAMP - INTERVAL '45 days'),
    ('17000000-0000-0000-0000-000000000001', 'OFFER',      'HIRED',     'Ứng viên chấp nhận offer. Ngày bắt đầu: 2 tuần sau.',                   'c1000000-0000-0000-0000-000000000003', CURRENT_TIMESTAMP - INTERVAL '40 days'),
-- Cẩm: NEW → SCREENING → INTERVIEW → OFFER
    ('17000000-0000-0000-0000-000000000002', NULL,         'NEW',       'Ứng viên nộp hồ sơ',                                                    'c1000000-0000-0000-0000-000000000003', CURRENT_TIMESTAMP - INTERVAL '21 days'),
    ('17000000-0000-0000-0000-000000000002', 'NEW',        'SCREENING', 'Profile tốt, 3 năm React. Chuyển sang screening.',                      'c1000000-0000-0000-0000-000000000003', CURRENT_TIMESTAMP - INTERVAL '19 days'),
    ('17000000-0000-0000-0000-000000000002', 'SCREENING',  'INTERVIEW', 'Đạt yêu cầu screening. Lên lịch phỏng vấn.',                            'c1000000-0000-0000-0000-000000000003', CURRENT_TIMESTAMP - INTERVAL '15 days'),
    ('17000000-0000-0000-0000-000000000002', 'INTERVIEW',  'OFFER',     'Phỏng vấn tốt. Gửi offer 28 triệu, chờ phản hồi.',                     'c1000000-0000-0000-0000-000000000003', CURRENT_TIMESTAMP - INTERVAL '5 days'),
-- Dũng: NEW → SCREENING → INTERVIEW
    ('17000000-0000-0000-0000-000000000003', NULL,         'NEW',       'Ứng viên nộp hồ sơ',                                                    'c1000000-0000-0000-0000-000000000003', CURRENT_TIMESTAMP - INTERVAL '14 days'),
    ('17000000-0000-0000-0000-000000000003', 'NEW',        'SCREENING', 'Java developer 3 năm. Cần đánh giá thêm kỹ năng microservices.',        'c1000000-0000-0000-0000-000000000003', CURRENT_TIMESTAMP - INTERVAL '12 days'),
    ('17000000-0000-0000-0000-000000000003', 'SCREENING',  'INTERVIEW', 'Đạt screening. Lên lịch phỏng vấn kỹ thuật ngày mai.',                  'c1000000-0000-0000-0000-000000000003', CURRENT_TIMESTAMP - INTERVAL '8 days'),
-- Emilia: NEW → SCREENING
    ('17000000-0000-0000-0000-000000000004', NULL,         'NEW',       'Ứng viên nộp hồ sơ cho vị trí Full-stack tại GreenBuild',               'c1000000-0000-0000-0000-000000000007', CURRENT_TIMESTAMP - INTERVAL '7 days'),
    ('17000000-0000-0000-0000-000000000004', 'NEW',        'SCREENING', 'CV phù hợp Node.js + Vue.js. Đang review chi tiết.',                    'c1000000-0000-0000-0000-000000000007', CURRENT_TIMESTAMP - INTERVAL '5 days'),
-- Phúc: NEW → SCREENING → REJECTED
    ('17000000-0000-0000-0000-000000000005', NULL,         'NEW',       'Ứng viên nộp hồ sơ',                                                    'c1000000-0000-0000-0000-000000000003', CURRENT_TIMESTAMP - INTERVAL '21 days'),
    ('17000000-0000-0000-0000-000000000005', 'NEW',        'SCREENING', 'Junior 1 năm, yêu cầu tối thiểu là 2 năm.',                             'c1000000-0000-0000-0000-000000000003', CURRENT_TIMESTAMP - INTERVAL '19 days'),
    ('17000000-0000-0000-0000-000000000005', 'SCREENING',  'REJECTED',  'Chưa đủ kinh nghiệm cho vị trí này. Khuyến khích ứng tuyển lại sau 1 năm.', 'c1000000-0000-0000-0000-000000000003', CURRENT_TIMESTAMP - INTERVAL '17 days');

-- ============================================================
-- 19. INTERVIEWS
-- Bình: 2 completed interviews
-- Cẩm: 1 completed interview
-- Dũng: 1 scheduled interview (tomorrow)
-- ============================================================
INSERT INTO interviews (id, application_id, title, scheduled_at, duration_minutes, location_or_link, interview_type, status, created_by) VALUES
    ('19000000-0000-0000-0000-000000000001', '17000000-0000-0000-0000-000000000001',
     'Phỏng vấn Kỹ thuật - Vòng 1', CURRENT_TIMESTAMP - INTERVAL '52 days', 60,
     'https://meet.google.com/abc-defg-hij', 'TECHNICAL', 'COMPLETED', 'c1000000-0000-0000-0000-000000000003'),
    ('19000000-0000-0000-0000-000000000002', '17000000-0000-0000-0000-000000000001',
     'Phỏng vấn Kỹ thuật - Vòng 2', CURRENT_TIMESTAMP - INTERVAL '48 days', 90,
     'Văn phòng TechViet, Tầng 12, Quận 7', 'TECHNICAL', 'COMPLETED', 'c1000000-0000-0000-0000-000000000003'),
    ('19000000-0000-0000-0000-000000000003', '17000000-0000-0000-0000-000000000002',
     'Phỏng vấn Frontend - Vòng 1', CURRENT_TIMESTAMP - INTERVAL '10 days', 60,
     'https://meet.google.com/klm-nopq-rst', 'TECHNICAL', 'COMPLETED', 'c1000000-0000-0000-0000-000000000003'),
    ('19000000-0000-0000-0000-000000000004', '17000000-0000-0000-0000-000000000003',
     'Phỏng vấn Kỹ thuật - Vòng 1', CURRENT_TIMESTAMP + INTERVAL '1 day', 60,
     'https://meet.google.com/uvw-xyz0-123', 'TECHNICAL', 'SCHEDULED', 'c1000000-0000-0000-0000-000000000003');

-- Interview-interviewers
INSERT INTO interview_interviewers (interview_id, user_id) VALUES
    ('19000000-0000-0000-0000-000000000001', 'c1000000-0000-0000-0000-000000000004'),
    ('19000000-0000-0000-0000-000000000002', 'c1000000-0000-0000-0000-000000000005'),
    ('19000000-0000-0000-0000-000000000003', 'c1000000-0000-0000-0000-000000000004'),
    ('19000000-0000-0000-0000-000000000004', 'c1000000-0000-0000-0000-000000000004'),
    ('19000000-0000-0000-0000-000000000004', 'c1000000-0000-0000-0000-000000000005');

-- ============================================================
-- 20. SCORECARDS
-- ============================================================
INSERT INTO scorecards (id, interview_id, interviewer_id, skill_score, attitude_score, english_score, comments, result, created_by, created_at) VALUES
    ('20000000-0000-0000-0000-000000000001', '19000000-0000-0000-0000-000000000001', 'c1000000-0000-0000-0000-000000000004',
     8.5, 9.0, 7.5,
     'Ứng viên có kiến thức sâu về Spring Boot và Kafka. Giải quyết bài toán system design tốt. Cần cải thiện thêm tiếng Anh kỹ thuật.',
     'PASS', 'c1000000-0000-0000-0000-000000000004', CURRENT_TIMESTAMP - INTERVAL '52 days'),

    ('20000000-0000-0000-0000-000000000002', '19000000-0000-0000-0000-000000000002', 'c1000000-0000-0000-0000-000000000005',
     9.0, 8.5, 8.0,
     'Vòng 2 rất ấn tượng. Code clean, hiểu rõ về distributed systems. Recommend hire ở level Senior.',
     'PASS', 'c1000000-0000-0000-0000-000000000005', CURRENT_TIMESTAMP - INTERVAL '48 days'),

    ('20000000-0000-0000-0000-000000000003', '19000000-0000-0000-0000-000000000003', 'c1000000-0000-0000-0000-000000000004',
     8.0, 9.5, 8.5,
     'Ứng viên có kiến thức React vững, code TypeScript sạch. Thái độ rất tích cực và chuyên nghiệp. Đề xuất gửi offer.',
     'PASS', 'c1000000-0000-0000-0000-000000000004', CURRENT_TIMESTAMP - INTERVAL '10 days');

-- ============================================================
-- 21. OFFERS
-- Bình: ACCEPTED, salary 35M, start date 2 weeks after acceptance
-- Cẩm: SENT, salary 28M, awaiting response
-- ============================================================
INSERT INTO offers (id, application_id, base_salary, currency, start_date, note, status, created_by, created_at) VALUES
    ('21000000-0000-0000-0000-000000000001', '17000000-0000-0000-0000-000000000001',
     35000000, 'VND', (CURRENT_DATE - INTERVAL '26 days')::DATE,
     'Offer chính thức cho vị trí Senior Backend Engineer. Lương gross 35 triệu VND/tháng. Bao gồm bảo hiểm sức khỏe và 13 tháng lương.',
     'ACCEPTED', 'c1000000-0000-0000-0000-000000000003', CURRENT_TIMESTAMP - INTERVAL '43 days'),

    ('21000000-0000-0000-0000-000000000002', '17000000-0000-0000-0000-000000000002',
     28000000, 'VND', (CURRENT_DATE + INTERVAL '14 days')::DATE,
     'Offer cho vị trí Frontend Engineer. Lương gross 28 triệu VND/tháng. Remote 100%. Thiết bị MacBook Pro được cấp.',
     'SENT', 'c1000000-0000-0000-0000-000000000003', CURRENT_TIMESTAMP - INTERVAL '4 days');
