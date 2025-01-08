INSERT INTO user_group
(user_group_id, name, type, description, is_default, created_by, created_on, modified_by, modified_on)
VALUES
    (1, 'test user group 1', 'SYSTEM_ADMIN', 'Test group description', false, 'test-user', NOW(), 'test-user', NOW());

INSERT INTO user_group
(user_group_id, name, type, description, is_default, created_by, created_on, modified_by, modified_on)
VALUES
    (2, 'test user group 2', 'READER', 'Another test group', false, 'test-user', NOW(), 'test-user', NOW());
