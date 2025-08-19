-- Insert test categories
INSERT INTO category (name, category_type, created, updated) VALUES 
('Food', 'EXPENSE', NOW(), NOW()),
('Transport', 'EXPENSE', NOW(), NOW()),
('Shopping', 'EXPENSE', NOW(), NOW()),
('Utilities', 'EXPENSE', NOW(), NOW());

-- Insert test items
INSERT INTO item (name, category_id, created, updated) VALUES 
('Rice', 1, NOW(), NOW()),
('Vegetables', 1, NOW(), NOW()),
('Fish', 1, NOW(), NOW()),
('Bus Fare', 2, NOW(), NOW()),
('Taxi', 2, NOW(), NOW()),
('Clothes', 3, NOW(), NOW()),
('Electronics', 3, NOW(), NOW()),
('Electricity Bill', 4, NOW(), NOW()),
('Water Bill', 4, NOW(), NOW());
