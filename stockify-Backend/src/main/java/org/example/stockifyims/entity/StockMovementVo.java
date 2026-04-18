package org.example.stockifyims.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "stock_movement")
@Data
public class StockMovementVo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductVo product;

    @ManyToOne
    @JoinColumn(name = "warehouse_id", nullable = false)
    private WarehouseVo warehouse;

    @Column(name = "movement_type", length = 10, nullable = false)
    private String movementType; // IN / OUT

    @Column(nullable = false)
    private double quantity;

    @Column(name = "reference_type", length = 20)
    private String referenceType;

    @Column(name = "reference_id")
    private long referenceId;

    @CreationTimestamp
    @Column(name = "created_on", updatable = false)
    private LocalDateTime createdOn;
}
