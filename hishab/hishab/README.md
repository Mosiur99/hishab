# Hishab - User Signup System

A full-stack application with Java Spring Boot backend, Next.js frontend, and PostgreSQL database.

## Project Structure

```
hishab/
├── src/                    # Spring Boot backend
│   └── main/
│       ├── java/com/project/hishab/
│       │   ├── controller/    # REST controllers
│       │   ├── service/       # Business logic
│       │   ├── repository/    # Data access layer
│       │   ├── entity/        # JPA entities
│       │   └── dto/          # Data transfer objects
│       └── resources/
│           └── application.properties
├── frontend/              # Next.js frontend
│   ├── src/
│   │   └── app/
│   │       ├── page.tsx     # Signup page
│   │       ├── layout.tsx   # Root layout
│   │       └── globals.css  # Global styles
│   ├── package.json
│   ├── tailwind.config.js
│   └── tsconfig.json
└── pom.xml
```

## Prerequisites

- Java 24
- Node.js 18+ (for frontend)
- PostgreSQL 12+
- Maven

## Setup Instructions

### 1. Database Setup

1. Install PostgreSQL if not already installed
2. Create a database:
   ```sql
   CREATE DATABASE hishab_db;
   ```
3. Update database credentials in `src/main/resources/application.properties` if needed

### 2. Backend Setup

1. Navigate to the project root:
   ```bash
   cd hishab
   ```

2. Build and run the Spring Boot application:
   ```bash
   mvn spring-boot:run
   ```

   The backend will start on `http://localhost:8080`

### 3. Frontend Setup

1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Start the development server:
   ```bash
   npm run dev
   ```

   The frontend will start on `http://localhost:3000`

## API Endpoints

### User Signup
- **POST** `/api/users/signup`
- **Body:**
  ```json
  {
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "password": "password123"
  }
  ```
- **Response:**
  ```json
  {
    "message": "User registered successfully",
    "email": "john.doe@example.com",
    "firstName": "John",
    "lastName": "Doe"
  }
  ```

## Features

### Backend
- Spring Boot 3.5.4
- Spring Data JPA
- PostgreSQL integration
- Password encryption with BCrypt
- Input validation
- CORS configuration for frontend

### Frontend
- Next.js 14 with App Router
- TypeScript
- Tailwind CSS for styling
- Axios for API calls
- Form validation
- Loading states and error handling
- Responsive design

## Database Schema

The application automatically creates the following table:

```sql
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);
```

## Security Features

- Password hashing with BCrypt
- Input validation and sanitization
- CORS configuration
- Email uniqueness validation

## Development

### Backend Development
- The application uses Hibernate for ORM
- JPA repositories for data access
- Lombok for reducing boilerplate code
- Validation annotations for input validation

### Frontend Development
- Modern React with hooks
- TypeScript for type safety
- Tailwind CSS for utility-first styling
- Responsive design for mobile and desktop

## Troubleshooting

1. **Database Connection Issues:**
   - Ensure PostgreSQL is running
   - Check database credentials in `application.properties`
   - Verify database `hishab_db` exists

2. **Frontend Build Issues:**
   - Clear node_modules and reinstall: `rm -rf node_modules && npm install`
   - Check Node.js version compatibility

3. **CORS Issues:**
   - Backend is configured to allow requests from `http://localhost:3000`
   - Update CORS configuration if using different ports

## Next Steps

- Add user authentication (JWT)
- Implement login functionality
- Add password reset functionality
- Add email verification
- Implement user profile management 