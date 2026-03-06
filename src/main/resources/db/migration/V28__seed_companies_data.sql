-- Seed default companies for testing and development

INSERT INTO companies (id, name, domain, website)
VALUES
    ('17f1e620-e7eb-49e5-a692-84f9c4af5b1e', 'VietRecruit HQ', 'vietrecruit.com', 'https://vietrecruit.com'),
    ('f7b7bfe5-7221-4b8f-b839-912281a650f5', 'Alpha Tech', 'alpha.tech', 'https://alpha.tech'),
    ('6970e198-65d9-44aa-95a1-98b560f1fb08', 'Beta Solutions', 'beta.solutions', 'https://beta.solutions')
ON CONFLICT (id) DO NOTHING;
