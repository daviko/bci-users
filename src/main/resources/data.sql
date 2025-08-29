-- Insert user
INSERT INTO users (id, name, email, password, created, last_login, token, is_active)
VALUES (
    '716ae77a-e3b9-4a88-9903-b67ba67763db',
    'John Doe',
    'john.doe@example.com',
    '$2a$10$abcdefghijklmnopqrstuvwxyz123456', -- Encrypted version of "a1Bcdefg23"
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqb2huLmRvZUBleGFtcGxlLmNvbSIsImlhdCI6MTc1NjMwNTA5NSwiZXhwIjoxNzU2MzA4Njk1fQ.oUEgTwaYvZqjnAe1YaRBeAMkER2IWGEolM_4v0ZHUj8aaooQEuR3e7WXkGOkjVRfTqRu1JnowOsh7Upw0o5liQ',
    true
);

-- Insert phone
INSERT INTO phones (id, number, city_code, country_code, user_id)
VALUES (
    1,
    1234567890,
    1,
    '57',
    '716ae77a-e3b9-4a88-9903-b67ba67763db'
);
