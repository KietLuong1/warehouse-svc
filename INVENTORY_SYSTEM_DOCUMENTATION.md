# Warehouse Inventory Management System

## Overview

The Inventory Management System is a comprehensive solution for tracking, managing, and controlling warehouse inventory. It provides CRUD operations, advanced analytics, and seamless integration with existing warehouse functions including Products, Warehouses, Suppliers, Categories, and Transactions.

## System Architecture

### Core Components

1. **Inventory Entity** - Central inventory tracking per product per warehouse
2. **Inventory History** - Audit trail for all inventory movements
3. **Inventory Service** - Business logic layer
4. **Inventory Controller** - REST API endpoints
5. **Integration Layer** - Connections with existing warehouse functions

## Key Features

### 1. Core Inventory Management (CRUD)
- Create inventory records for products in specific warehouses
- Update inventory quantities, locations, and settings
- Retrieve inventory by various filters
- Delete inventory records (with proper controls)

### 2. Advanced Inventory Operations
- **Quantity Adjustments**: Add, subtract, or set specific quantities
- **Inventory Transfers**: Move stock between warehouses
- **Reservations**: Reserve inventory for orders
- **Physical Counts**: Record and reconcile physical inventory counts

### 3. Analytics & Reporting
- Inventory summary with key metrics
- Low stock alerts and reorder notifications
- Overstock identification
- Expiring item tracking
- Top inventory items by value

### 4. Location & Batch Management
- Track items by specific warehouse locations (aisle-row-shelf)
- Batch/lot number tracking for traceability
- Location-based inventory queries

### 5. Audit & History Tracking
- Complete audit trail of all inventory movements
- Track who made changes and when
- Reference numbers for transaction traceability

## API Endpoints

### Basic CRUD Operations

#### Create Inventory
```
POST /api/v1/inventory/create
Content-Type: application/json

{
  "productId": "product-uuid",
  "warehouseId": "warehouse-uuid",
  "quantityOnHand": 100,
  "reorderLevel": 20,
  "maxStockLevel": 500,
  "unitCost": 25.50,
  "locationCode": "A1-B2-C3"
}
```

#### Update Inventory
```
PUT /api/v1/inventory/update/{id}
Content-Type: application/json

{
  "quantityOnHand": 150,
  "reorderLevel": 25,
  "locationCode": "A1-B2-C4"
}
```

#### Get Inventory
```
GET /api/v1/inventory/{id}
GET /api/v1/inventory/all
GET /api/v1/inventory/product/{productId}
GET /api/v1/inventory/warehouse/{warehouseId}
GET /api/v1/inventory/product/{productId}/warehouse/{warehouseId}
```

### Inventory Operations

#### Adjust Inventory Quantity
```
POST /api/v1/inventory/adjust
Content-Type: application/json

{
  "inventoryId": "inventory-uuid",
  "adjustmentType": "ADD", // ADD, SUBTRACT, SET
  "quantity": 50,
  "reason": "Damaged goods removal",
  "referenceNumber": "ADJ-2024-001"
}
```

#### Transfer Between Warehouses
```
POST /api/v1/inventory/move
Content-Type: application/json

{
  "fromWarehouseId": "warehouse1-uuid",
  "toWarehouseId": "warehouse2-uuid",
  "productId": "product-uuid",
  "quantity": 25,
  "reason": "Rebalancing stock levels"
}
```

#### Reserve/Release Inventory
```
POST /api/v1/inventory/{id}/reserve?quantity=10
POST /api/v1/inventory/{id}/release?quantity=5
```

### Analytics & Reports

#### Get Inventory Summary
```
GET /api/v1/inventory/summary

Response:
{
  "status": 200,
  "message": "Success",
  "data": {
    "summary": {
      "totalInventoryItems": 1250,
      "totalInventoryValue": 125000.00,
      "lowStockCount": 15,
      "overstockCount": 8,
      "outOfStockCount": 3,
      "expiringSoonCount": 5
    }
  }
}
```

#### Get Alerts
```
GET /api/v1/inventory/alerts/low-stock
GET /api/v1/inventory/alerts/overstock
GET /api/v1/inventory/alerts/out-of-stock
GET /api/v1/inventory/alerts/expiring?daysAhead=30
```

### Location & Batch Management

```
GET /api/v1/inventory/location/{locationCode}
GET /api/v1/inventory/batch/{batchNumber}
PUT /api/v1/inventory/{id}/location?locationCode=A2-B3-C1
```

### Inventory Counting & Audit

```
GET /api/v1/inventory/count/needed?daysSinceLastCount=90
POST /api/v1/inventory/{id}/count/mark
POST /api/v1/inventory/{id}/count?countedQuantity=95&countedBy=user123
```

## Integration with Existing Systems

### 1. Product Integration
- Inventory records link to existing Product entities
- Automatic validation that products exist before creating inventory
- Product details included in inventory responses

### 2. Warehouse Integration
- Each inventory record is tied to a specific warehouse
- Supports multi-warehouse inventory tracking
- Warehouse capacity and location management

### 3. Supplier Integration
- Track which supplier provided inventory items
- Support for supplier returns and exchanges
- Supplier performance analytics based on inventory data

### 4. Transaction Integration
- All inventory changes can generate transaction records
- Purchase transactions increase inventory
- Sale transactions decrease inventory
- Complete audit trail between transactions and inventory changes

### 5. Category Integration
- Inventory analytics by product category
- Category-based reorder rules and alerts
- Bulk operations by category

## Data Model

### Inventory Entity
```sql
CREATE TABLE inventory (
    id VARCHAR(36) PRIMARY KEY,
    product_id VARCHAR(36) NOT NULL,
    warehouse_id VARCHAR(36) NOT NULL,
    quantity_on_hand INTEGER NOT NULL DEFAULT 0,
    reserved_quantity INTEGER DEFAULT 0,
    reorder_level INTEGER,
    max_stock_level INTEGER,
    unit_cost DECIMAL(10,2),
    location_code VARCHAR(50),
    batch_number VARCHAR(100),
    expiry_date DATETIME,
    last_counted_date DATETIME,
    last_updated DATETIME NOT NULL,
    created_at DATETIME NOT NULL,
    updated_by VARCHAR(36),
    UNIQUE KEY unique_product_warehouse (product_id, warehouse_id),
    FOREIGN KEY (product_id) REFERENCES products(id),
    FOREIGN KEY (warehouse_id) REFERENCES warehouses(id)
);
```

### Inventory History Entity
```sql
CREATE TABLE inventory_history (
    id VARCHAR(36) PRIMARY KEY,
    inventory_id VARCHAR(36) NOT NULL,
    transaction_type VARCHAR(50) NOT NULL,
    quantity_change INTEGER NOT NULL,
    quantity_before INTEGER NOT NULL,
    quantity_after INTEGER NOT NULL,
    unit_cost DECIMAL(10,2),
    reason VARCHAR(255),
    reference_number VARCHAR(100),
    notes TEXT,
    performed_by VARCHAR(36) NOT NULL,
    performed_by_name VARCHAR(100),
    created_at DATETIME NOT NULL,
    FOREIGN KEY (inventory_id) REFERENCES inventory(id)
);
```

## Business Rules

### 1. Inventory Creation
- Each product can have only one inventory record per warehouse
- Initial quantity must be non-negative
- Product and warehouse must exist before creating inventory

### 2. Quantity Management
- Quantity on hand cannot go below zero
- Reserved quantity cannot exceed quantity on hand
- Available quantity = quantity on hand - reserved quantity

### 3. Alerts and Notifications
- Low stock alert when quantity ≤ reorder level
- Overstock alert when quantity > max stock level
- Expiry alerts based on configurable days ahead

### 4. Transfers
- Source warehouse must have sufficient available quantity
- Destination inventory is created if it doesn't exist
- Both warehouses must be active and accessible

### 5. Audit Requirements
- All quantity changes must be logged in inventory history
- User identification required for all modifications
- Reference numbers encouraged for traceability

## Security & Permissions

### Admin-Only Operations
- Create inventory records
- Delete inventory records
- Adjust inventory quantities
- Transfer inventory between warehouses
- Reserve/release inventory
- Update inventory locations
- Perform inventory counts

### Read-Only Operations
- View inventory details
- Search inventory
- View analytics and reports
- View alerts and notifications

## Integration Examples

### 1. Purchase Order Processing
When a purchase order is received:
```java
// Update inventory quantity
inventoryService.adjustInventory(InventoryAdjustmentRequest.builder()
    .inventoryId(inventoryId)
    .adjustmentType("ADD")
    .quantity(receivedQuantity)
    .reason("Purchase Order Receipt")
    .referenceNumber(purchaseOrderNumber)
    .build());

// Create transaction record
transactionService.purchase(transactionRequest);
```

### 2. Sales Order Fulfillment
When a sales order is processed:
```java
// Reserve inventory
inventoryService.reserveInventory(inventoryId, orderQuantity);

// When order ships, reduce inventory
inventoryService.adjustInventory(InventoryAdjustmentRequest.builder()
    .inventoryId(inventoryId)
    .adjustmentType("SUBTRACT")
    .quantity(shippedQuantity)
    .reason("Sales Order Fulfillment")
    .referenceNumber(salesOrderNumber)
    .build());

// Create transaction record
transactionService.sell(transactionRequest);
```

### 3. Automatic Reorder Processing
```java
// Check for low stock items
Response lowStockResponse = inventoryService.getLowStockItems();
List<InventoryDTO> lowStockItems = extractInventoryList(lowStockResponse);

// Generate purchase orders for low stock items
for (InventoryDTO item : lowStockItems) {
    PurchaseOrder po = createPurchaseOrder(item);
    purchaseOrderService.createPurchaseOrder(po);
}
```

## Best Practices

### 1. Data Consistency
- Always use transactions for multi-step operations
- Validate data integrity before making changes
- Use database constraints to enforce business rules

### 2. Performance Optimization
- Index frequently queried fields (product_id, warehouse_id, location_code)
- Use pagination for large result sets
- Cache frequently accessed data

### 3. Error Handling
- Provide meaningful error messages
- Log all errors for debugging
- Implement retry mechanisms for transient failures

### 4. Monitoring & Alerting
- Set up automated alerts for critical inventory levels
- Monitor system performance and response times
- Track inventory accuracy and variance metrics

## Future Enhancements

### 1. Advanced Analytics
- Demand forecasting based on historical data
- Seasonal trend analysis
- ABC analysis for inventory classification

### 2. Integration Expansions
- EDI integration for automated supplier communications
- Barcode/QR code scanning for mobile operations
- IoT sensor integration for automated counting

### 3. Workflow Automation
- Automated reorder point calculations
- Smart location suggestions based on product velocity
- Automated expiry date management and alerts

### 4. Mobile Operations
- Mobile app for warehouse staff
- Barcode scanning for inventory operations
- Offline capability for remote warehouses
