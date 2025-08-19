# API Integration Documentation

## Overview
This frontend application integrates with a Spring Boot backend API for cost management. The main API endpoint for cost creation is implemented according to the provided Spring Boot controller.

## API Endpoints

### Cost Creation
- **Endpoint**: `POST /api/v1/user/cost`
- **Controller**: `UserController.createCost()`
- **Request Body**: `CreateCostRequest`

### Request Structure
```typescript
interface CreateCostRequest {
  itemId: number;
  quantity: number;
  perUnitCost: number;
  totalCost: number;
  weight?: number;
  perWeightCost?: number;
  purchaseType: 'CASH' | 'CARD' | 'MOBILE_BANKING' | 'BANK_TRANSFER';
  costingDate: string; // ISO date string
}
```

### Supporting Endpoints
- **Items**: `GET /api/v1/user/items`

### API Response Structure
```typescript
interface ApiResponse<T> {
  result: boolean;
  message: string;
  data: T;
}

interface Item {
  id: number;
  name: string;
  categoryId: number;
  categoryType: string;
}
```

## Environment Configuration
Create a `.env.local` file in the project root with:
```
NEXT_PUBLIC_API_URL=http://localhost:8080
```

## Features Implemented

### Dashboard Cost Entry Form
- Comprehensive form matching the API request structure
- Item selection from all available items
- Automatic total cost calculation
- Purchase type selection
- Date picker for costing date
- Form validation and error handling

### Service Layer
- `CostService`: Handles cost creation API calls
- `ItemService`: Fetches items from user endpoint

### Form Features
- Real-time total cost calculation (quantity × per unit cost)
- Item selection from complete item list
- Required field validation
- Error handling with user feedback
- Form reset after successful submission

## Usage
1. Start the Spring Boot backend on port 8080
2. Set the `NEXT_PUBLIC_API_URL` environment variable
3. Run the Next.js frontend: `npm run dev`
4. Navigate to the dashboard and click "Add Daily Cost Entry"

## Error Handling
- Network errors are caught and displayed to users
- API response validation
- Form validation for required fields
- Graceful fallback for missing data
