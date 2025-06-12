package com.capstone.warehousesvc.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_history")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_id", nullable = false)
    private Inventory inventory;

    @Column(name = "transaction_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private InventoryTransactionType transactionType;

    @Column(name = "quantity_change", nullable = false)
    private Integer quantityChange; // positive for additions, negative for reductions

    @Column(name = "quantity_before", nullable = false)
    private Integer quantityBefore;

    @Column(name = "quantity_after", nullable = false)
    private Integer quantityAfter;

    @Column(name = "unit_cost")
    private BigDecimal unitCost;

    @Column(name = "reason")
    private String reason;

    @Column(name = "reference_number")
    private String referenceNumber;

    @Column(name = "notes")
    private String notes;

    @Column(name = "performed_by", nullable = false)
    private String performedBy; // User ID who performed the action

    @Column(name = "performed_by_name")
    private String performedByName; // User name for display

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Enum for inventory transaction types
    public enum InventoryTransactionType {
        INITIAL_STOCK,
        PURCHASE,
        SALE,
        ADJUSTMENT_ADD,
        ADJUSTMENT_SUBTRACT,
        ADJUSTMENT_SET,
        TRANSFER_IN,
        TRANSFER_OUT,
        RESERVATION,
        RELEASE_RESERVATION,
        PHYSICAL_COUNT,
        DAMAGED,
        EXPIRED,
        RETURNED_TO_SUPPLIER,
        RETURNED_FROM_CUSTOMER,
        MANUFACTURING_CONSUMPTION,
        MANUFACTURING_OUTPUT
    }

    @Override
    public String toString() {
        return "InventoryHistory{" +
                "id='" + id + '\'' +
                ", transactionType=" + transactionType +
                ", quantityChange=" + quantityChange +
                ", quantityBefore=" + quantityBefore +
                ", quantityAfter=" + quantityAfter +
                ", reason='" + reason + '\'' +
                ", performedBy='" + performedBy + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
