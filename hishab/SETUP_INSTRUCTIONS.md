# Setup Instructions for Hishab Expense Tracker

## Issues Fixed

1. **Item List Not Showing**: Added debugging and CORS configuration
2. **Purchase Type Mismatch**: Created PaymentType enum and updated backend to handle payment methods (CASH, CARD, MOBILE_BANKING, BANK_TRANSFER)
3. **Costing Type Feature**: Added CostingType enum to handle weight-based vs quantity-based costing

## Backend Setup (Spring Boot)

### 1. Database Setup
First, ensure your database is running and create the necessary tables. Then run these SQL scripts in order:

```sql
-- Run the test data insertion script
-- This will create categories, items, and a default account
```

### 2. Run the SQL Scripts
Execute these scripts in your database:

1. `sql/insert_test_data.sql` - Creates test categories and items
2. `sql/insert_account.sql` - Creates a default account
3. `sql/add_costing_type_column.sql` - Adds costing_type column to cost table

### 3. Start the Backend
```bash
cd hishab
./mvnw spring-boot:run
```

The backend will start on `http://localhost:8080`

## Frontend Setup (Next.js)

### 1. Install Dependencies
```bash
cd hishab-frontend
npm install
```

### 2. Environment Configuration
Create a `.env.local` file in the `hishab-frontend` directory:
```
NEXT_PUBLIC_API_URL=http://localhost:8080
```

### 3. Start the Frontend
```bash
npm run dev
```

The frontend will start on `http://localhost:3000`

## Testing the Application

### 1. Check Item List
1. Open `http://localhost:3000/dashboard`
2. Click "Add Daily Cost Entry"
3. You should see items in the dropdown (e.g., "Rice - EXPENSE", "Vegetables - EXPENSE")
4. The dropdown will show "(X items available)" where X is the number of items

### 2. Test Cost Entry
1. Select an item from the dropdown
2. Choose costing type (By Quantity or By Weight)
3. Enter the required fields based on costing type:
   - **By Quantity**: Enter quantity and per unit cost
   - **By Weight**: Enter weight and per weight cost
4. The total cost should auto-calculate
5. Select a purchase type (Cash, Card, Mobile Banking, or Bank Transfer)
6. Select a date
7. Click "Create Cost Entry"

### 3. Verify Backend Integration
- Check the browser console for debugging information
- The cost entry should be saved to the database
- The expense should appear in the dashboard list

## Troubleshooting

### Item List Not Showing
1. Check browser console for errors
2. Verify the backend is running on port 8080
3. Check if the database has items (run `SELECT * FROM item;`)
4. Verify CORS is working (check Network tab in browser dev tools)

### Purchase Type Issues
1. Ensure the backend is using the new PaymentType enum
2. Check that the frontend is sending the correct payment type values
3. Verify the database column accepts the new enum values

### Database Issues
1. Make sure your database is running
2. Check that all tables exist
3. Verify the test data was inserted correctly

## API Endpoints

- `GET /api/v1/user/items` - Get all items
- `POST /api/v1/user/cost` - Create a cost entry

## Files Modified

### Backend
- `hishab/src/main/java/com/project/hishab/Enum/PaymentType.java` - New enum
- `hishab/src/main/java/com/project/hishab/Enum/CostingType.java` - New enum for costing method
- `hishab/src/main/java/com/project/hishab/entity/Cost.java` - Updated to use PaymentType and CostingType
- `hishab/src/main/java/com/project/hishab/model/CreateCostRequest.java` - Updated to use PaymentType and CostingType
- `hishab/src/main/java/com/project/hishab/service/CostServiceImpl.java` - Fixed validation logic for both types
- `hishab/src/main/java/com/project/hishab/controller/client/UserController.java` - Added CORS

### Frontend
- `hishab-frontend/app/dashboard/page.tsx` - Added debugging and improved item display
- `hishab-frontend/app/services/costService.ts` - Fixed error response format

### Database
- `sql/insert_test_data.sql` - Test data for categories and items
- `sql/insert_account.sql` - Default account creation
- `sql/add_costing_type_column.sql` - Database schema update for costing type
