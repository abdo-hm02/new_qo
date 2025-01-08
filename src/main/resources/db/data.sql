
-- Insert User Groups
INSERT INTO user_group (name, description, type, created_by, created_on, modified_by, modified_on)
VALUES
    ('System Administrators', 'Full system access group', 'SYSTEM_ADMIN', 'system', NOW(), 'system', NOW()),
    ('Content Readers', 'Read-only access to content', 'READER', 'system', NOW(), 'system', NOW()),
    ('Book Club Leaders', 'Manage reading groups', 'READER', 'system', NOW(), 'system', NOW()),
    ('Literature Enthusiasts', 'Regular readers group', 'READER', 'system', NOW(), 'system', NOW()),
    ('Guest Readers', 'Limited access group', 'READER', 'system', NOW(), 'system', NOW());

-- Insert Users
INSERT INTO "user" (first_name, last_name, email, username, active, last_login, created_by, created_on, modified_by, modified_on)
VALUES
    ('Admin', 'Test', 'admin@qoraa.net', 'admin_test', true, NOW(), 'system', NOW(), 'system', NOW()),
    ('Adnane', 'Miliari', 'miliariadnane.dev@gmail.com', 'amiliari', true, NOW(), 'system', NOW(), 'system', NOW()),
    ('Robert', 'Johnson', 'robert.j@example.com', 'rjohnson', true, NOW(), 'system', NOW(), 'system', NOW()),
    ('Maria', 'Garcia', 'maria.g@example.com', 'mgarcia', true, NOW(), 'system', NOW(), 'system', NOW()),
    ('David', 'Brown', 'david.b@example.com', 'dbrown', true, NOW(), 'system', NOW(), 'system', NOW()),
    ('Sarah', 'Wilson', 'sarah.w@example.com', 'swilson', true, NOW(), 'system', NOW(), 'system', NOW()),
    ('Michael', 'Lee', 'michael.l@example.com', 'mlee', true, NOW(), 'system', NOW(), 'system', NOW()),
    ('Emma', 'Taylor', 'emma.t@example.com', 'etaylor', true, NOW(), 'system', NOW(), 'system', NOW()),
    ('James', 'Anderson', 'james.a@example.com', 'janderson', true, NOW(), 'system', NOW(), 'system', NOW()),
    ('Lisa', 'Martin', 'lisa.m@example.com', 'lmartin', true, NOW(), 'system', NOW(), 'system', NOW());

-- Link Users to Groups
INSERT INTO user_group_user (user_group_id, user_id)
VALUES
    (1, 1), -- Admin Test in System Administrators
    (1, 2), -- Adnane Miliari in System Administrators
    (2, 3), -- Robert Johnson in Content Readers
    (2, 4), -- Maria Garcia in Content Readers
    (3, 5), -- David Brown in Book Club Leaders
    (3, 6), -- Sarah Wilson in Book Club Leaders
    (4, 7), -- Michael Lee in Literature Enthusiasts
    (4, 8), -- Emma Taylor in Literature Enthusiasts
    (5, 9), -- James Anderson in Guest Readers
    (5, 10); -- Lisa Martin in Guest Readers

-- Link all permissions to System Administrators group (user_group_id = 1)
INSERT INTO user_group_permission (user_group_id, permission_id)
SELECT 1, permission_id
FROM permission;

-- Link specific permissions to Content Readers group (user_group_id = 2)
INSERT INTO user_group_permission (user_group_id, permission_id)
SELECT 2, permission_id
FROM permission
WHERE name IN (
               'ReadingProject.View',
               'ProjectBook.View',
               'ReadingProgress.View',
               'ReadingSummary.View'
    );
