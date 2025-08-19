-- Create users table for authentication
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255),
    name VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'USER',
    google_id VARCHAR(255),
    email_verified BOOLEAN NOT NULL DEFAULT FALSE,
    verification_token VARCHAR(255),
    reset_token VARCHAR(255),
    reset_token_expiry DATETIME,
    created DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    last_login DATETIME
);

-- Insert a default admin user (password: admin123)
INSERT INTO users (email, password, name, role, email_verified, created) VALUES
('admin@hishab.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Admin User', 'ADMIN', TRUE, NOW());

-- Insert a default regular user (password: user123)
INSERT INTO users (email, password, name, role, email_verified, created) VALUES
('user@hishab.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Regular User', 'USER', TRUE, NOW());
