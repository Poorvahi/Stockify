package org.example.stockifyims.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Data
@Table(name = "purchase")
@Entity
public class PurchaseVo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "purchase_id")
    private long purchaseId;

    @Column(columnDefinition = "DOUBLE PRECISION DEFAULT 0.0")
    private double quantity;

    @Column(columnDefinition = "DOUBLE PRECISION DEFAULT 0.0")
    private double price;

    @Column(columnDefinition = "DOUBLE PRECISION DEFAULT 0.0")
    private double total;

    @Column(name = "is_deleted", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isDeleted;

    @CreationTimestamp
    @Column(name = "created_on", updatable = false)
    private LocalDateTime createdOn;

    @UpdateTimestamp
    @Column(name = "modified_on")
    private LocalDateTime modifiedOn;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductVo product;

}
