# Complete Hishab Authentication System Setup Guide

## 🎯 Overview

This guide will help you set up a complete authentication system for the Hishab expense tracking application with:

- ✅ **User Authentication** (Login/Signup)
- ✅ **Google OAuth Integration** (Email verified)
- ✅ **Role-Based Access Control** (USER/ADMIN)
- ✅ **JWT Token Authentication**
- ✅ **Protected Routes** (/hishab for users, /admin for admins)
- ✅ **Costing Type System** (Weight/Quantity based costing)

## 🏗️ System Architecture

### URL Structure
- `localhost:3000` → Redirects to login or appropriate dashboard
- `localhost:3000/login` → Login page
- `localhost:3000/signup` → Registration page
- `localhost:3000/hishab` → User dashboard (requires USER role)
- `localhost:3000/admin` → Admin dashboard (requires ADMIN role)

### Backend API Structure
- `/api/v1/auth/*` → Authentication endpoints (public)
- `/api/v1/user/*` → User endpoints (requires USER role)
- `/api/v1/admin/*` → Admin endpoints (requires ADMIN role)

## 🚀 Backend Setup (Spring Boot)

### 1. Database Setup

First, ensure your PostgreSQL database is running and create the necessary tables:

```sql
-- Run these scripts in order:

-- 1. Create users table
-- File: sql/create_users_table.sql

-- 2. Add costing type column
-- File: sql/add_costing_type_column.sql

-- 3. Insert test data
-- File: sql/insert_test_data.sql

-- 4. Insert default account
-- File: sql/insert_account.sql
```

### 2. Environment Configuration

Update `hishab/src/main/resources/application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/hishab_db
spring.datasource.username=postgres
spring.datasource.password=#12Hisab#21#
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.properties.hibernate.format_sql=true

# JWT Configuration
jwt.secret=your-secret-key-here-make-it-long-and-secure-for-production-use
jwt.expiration=86400000

# Server Configuration
server.port=8080
```

### 3. Dependencies

The following dependencies have been added to `pom.xml`:

- `spring-boot-starter-security` - Spring Security
- `jjwt-api`, `jjwt-impl`, `jjwt-jackson` - JWT support
- `spring-security-crypto` - Password encryption

### 4. Start the Backend

```bash
cd hishab
./mvnw spring-boot:run
```

The backend will start on `http://localhost:8080`

## 🎨 Frontend Setup (Next.js)

### 1. Install Dependencies

```bash
cd hishab-frontend
npm install
```

### 2. Environment Configuration

Create a `.env.local` file in the `hishab-frontend` directory:

```env
NEXT_PUBLIC_API_URL=http://localhost:8080
```

### 3. Start the Frontend

```bash
npm run dev
```

The frontend will start on `http://localhost:3000`

## 🔐 Authentication System

### Default Users

The system comes with two default users:

1. **Admin User**
   - Email: `admin@hishab.com`
   - Password: `admin123`
   - Role: `ADMIN`
   - Access: `/admin` dashboard

2. **Regular User**
   - Email: `user@hishab.com`
   - Password: `user123`
   - Role: `USER`
   - Access: `/hishab` dashboard

### Authentication Flow

1. **Login Process**:
   - User enters email/password
   - System validates credentials
   - JWT token generated and stored in localStorage
   - User redirected to appropriate dashboard based on role

2. **Google OAuth**:
   - User clicks "Sign in with Google"
   - Google authentication flow (simulated for demo)
   - User account created/linked automatically
   - Email automatically verified

3. **Route Protection**:
   - `/hishab` - Only accessible to authenticated users with USER role
   - `/admin` - Only accessible to authenticated users with ADMIN role
   - Unauthenticated users redirected to `/login`

## 🛡️ Security Features

### JWT Token Management
- **Access Token**: 24 hours validity
- **Refresh Token**: 7 days validity
- **Automatic Token Refresh**: Handled by frontend
- **Token Storage**: Secure localStorage with automatic cleanup

### Password Security
- **BCrypt Encryption**: All passwords encrypted
- **Password Validation**: Minimum 6 characters
- **Password Reset**: Token-based reset system

### Role-Based Access Control
- **USER Role**: Can access `/hishab` dashboard
- **ADMIN Role**: Can access `/admin` dashboard
- **API Protection**: Backend endpoints protected by role

## 📊 Costing Type System

### Features
- **Quantity-Based Costing**: Items priced by quantity (e.g., rice, electronics)
- **Weight-Based Costing**: Items priced by weight (e.g., vegetables, meat)
- **Dynamic Form**: Form fields change based on costing type
- **Auto-Calculation**: Total cost calculated automatically
- **Validation**: Backend validates required fields based on costing type

### Usage
1. Select an item from dropdown
2. Choose costing type (Quantity/Weight)
3. Enter required fields:
   - **Quantity**: Number of units + per unit cost
   - **Weight**: Weight in kg + per weight cost
4. Total cost auto-calculates
5. Select payment type and date
6. Submit cost entry

## 🧪 Testing the System

### 1. Test Authentication

1. **Visit** `http://localhost:3000`
2. **Should redirect** to `/login`
3. **Login as Admin**:
   - Email: `admin@hishab.com`
   - Password: `admin123`
   - **Should redirect** to `/admin`
4. **Login as User**:
   - Email: `user@hishab.com`
   - Password: `user123`
   - **Should redirect** to `/hishab`

### 2. Test Role Protection

1. **Login as User** → Try to access `/admin` → Should redirect to `/hishab`
2. **Login as Admin** → Try to access `/hishab` → Should redirect to `/admin`
3. **Logout** → Try to access any dashboard → Should redirect to `/login`

### 3. Test Cost Entry (User Dashboard)

1. **Login as User**
2. **Click "Add Daily Cost Entry"**
3. **Test Quantity-Based Costing**:
   - Select an item
   - Choose "By Quantity"
   - Enter quantity and per unit cost
   - Verify total auto-calculates
4. **Test Weight-Based Costing**:
   - Select an item
   - Choose "By Weight"
   - Enter weight and per weight cost
   - Verify total auto-calculates

### 4. Test Google OAuth

1. **Click "Sign in with Google"**
2. **Should create/link account automatically**
3. **Should redirect to appropriate dashboard**

## 🔧 API Endpoints

### Authentication Endpoints
- `POST /api/v1/auth/login` - User login
- `POST /api/v1/auth/signup` - User registration
- `POST /api/v1/auth/google` - Google OAuth
- `POST /api/v1/auth/logout` - User logout
- `POST /api/v1/auth/refresh-token` - Refresh JWT token
- `GET /api/v1/auth/verify-email` - Email verification
- `POST /api/v1/auth/forgot-password` - Password reset request
- `POST /api/v1/auth/reset-password` - Password reset

### User Endpoints (Requires USER Role)
- `GET /api/v1/user/items` - Get all items
- `POST /api/v1/user/cost` - Create cost entry

### Admin Endpoints (Requires ADMIN Role)
- `GET /api/v1/admin/items` - Get all items (admin view)
- `GET /api/v1/admin/items/category/{categoryId}` - Get items by category

## 📁 File Structure

### Backend Files
```
hishab/src/main/java/com/project/hishab/
├── Enum/
│   ├── UserRole.java
│   ├── PaymentType.java
│   └── CostingType.java
├── entity/
│   ├── User.java
│   ├── Cost.java
│   └── ...
├── model/
│   ├── AuthResponse.java
│   ├── LoginRequest.java
│   ├── SignupRequest.java
│   └── ...
├── service/
│   ├── AuthService.java
│   ├── AuthServiceImpl.java
│   └── ...
├── controller/
│   ├── AuthController.java
│   ├── client/UserController.java
│   └── admin/AdminController.java
├── config/
│   ├── SecurityConfig.java
│   └── JwtAuthenticationFilter.java
└── util/
    └── JwtUtil.java
```

### Frontend Files
```
hishab-frontend/app/
├── login/page.tsx
├── signup/page.tsx
├── hishab/page.tsx
├── admin/page.tsx
├── services/
│   ├── authService.ts
│   ├── costService.ts
│   └── itemService.ts
└── ...
```

### Database Scripts
```
sql/
├── create_users_table.sql
├── add_costing_type_column.sql
├── insert_test_data.sql
└── insert_account.sql
```

## 🚨 Troubleshooting

### Common Issues

1. **Database Connection Error**:
   - Verify PostgreSQL is running
   - Check database credentials in `application.properties`
   - Ensure database `hishab_db` exists

2. **JWT Token Issues**:
   - Check JWT secret in `application.properties`
   - Verify token expiration settings
   - Clear browser localStorage if needed

3. **CORS Issues**:
   - Backend has `@CrossOrigin` annotations
   - Frontend configured with correct API URL
   - Check browser console for CORS errors

4. **Authentication Failures**:
   - Verify user exists in database
   - Check password encryption
   - Ensure email verification status

### Debug Steps

1. **Check Backend Logs**:
   ```bash
   cd hishab
   ./mvnw spring-boot:run
   ```

2. **Check Frontend Console**:
   - Open browser dev tools
   - Check Network tab for API calls
   - Check Console for errors

3. **Verify Database**:
   ```sql
   SELECT * FROM users;
   SELECT * FROM cost;
   SELECT * FROM item;
   ```

## 🎉 Success Indicators

When everything is working correctly:

1. ✅ **Backend starts** without errors on port 8080
2. ✅ **Frontend starts** without errors on port 3000
3. ✅ **Login works** for both admin and user accounts
4. ✅ **Role-based routing** works correctly
5. ✅ **Cost entry** works with both costing types
6. ✅ **JWT tokens** are generated and stored
7. ✅ **Protected routes** redirect unauthorized users

## 🔄 Next Steps

### Production Deployment
1. **Change JWT secret** to a secure random string
2. **Configure HTTPS** for production
3. **Set up email service** for verification emails
4. **Configure Google OAuth** with real credentials
5. **Set up database** with proper security
6. **Configure environment variables** for secrets

### Feature Enhancements
1. **Real-time notifications**
2. **Advanced reporting**
3. **Data export functionality**
4. **Multi-currency support**
5. **Budget tracking**
6. **Receipt image upload**

---

**🎯 The system is now ready for use!** Users can register, login, and access role-appropriate dashboards with full authentication and authorization protection.
