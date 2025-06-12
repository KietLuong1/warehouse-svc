package com.capstone.warehousesvc.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"product_id", "warehouse_id"}))
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @Column(name = "quantity_on_hand", nullable = false)
    @Min(value = 0, message = "Quantity on hand cannot be negative")
    private Integer quantityOnHand;

    @Column(name = "reserved_quantity")
    @Min(value = 0, message = "Reserved quantity cannot be negative")
    private Integer reservedQuantity = 0;

    @Column(name = "reorder_level")
    @Min(value = 0, message = "Reorder level cannot be negative")
    private Integer reorderLevel;

    @Column(name = "max_stock_level")
    @Min(value = 0, message = "Max stock level cannot be negative")
    private Integer maxStockLevel;

    @Column(name = "unit_cost")
    private BigDecimal unitCost;

    @Column(name = "location_code")
    private String locationCode; // e.g., "A1-B2-C3" for aisle-row-shelf

    @Column(name = "batch_number")
    private String batchNumber;

    @Column(name = "expiry_date")
    private LocalDateTime expiryDate;

    @Column(name = "last_counted_date")
    private LocalDateTime lastCountedDate;

    @Column(name = "last_updated", nullable = false)
    private LocalDateTime lastUpdated;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_by")
    private String updatedBy; // User ID who last updated

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        lastUpdated = now;
    }

    @PreUpdate
    protected void onUpdate() {
        lastUpdated = LocalDateTime.now();
    }

    // Computed properties
    @Transient
    public Integer getAvailableQuantity() {
        return quantityOnHand - (reservedQuantity != null ? reservedQuantity : 0);
    }

    @Transient
    public boolean isLowStock() {
        return reorderLevel != null && quantityOnHand <= reorderLevel;
    }

    @Transient
    public boolean isOverstock() {
        return maxStockLevel != null && quantityOnHand > maxStockLevel;
    }

    @Transient
    public BigDecimal getTotalValue() {
        if (unitCost == null) return BigDecimal.ZERO;
        return unitCost.multiply(BigDecimal.valueOf(quantityOnHand));
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "id='" + id + '\'' +
                ", quantityOnHand=" + quantityOnHand +
                ", reservedQuantity=" + reservedQuantity +
                ", reorderLevel=" + reorderLevel +
                ", maxStockLevel=" + maxStockLevel +
                ", locationCode='" + locationCode + '\'' +
                ", batchNumber='" + batchNumber + '\'' +
                '}';
    }
}
