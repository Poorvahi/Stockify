package org.example.stockifyims.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "product")
@Data
public class ProductVo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private long productId;

    @Column(name = "product_name", length = 100)
    private String productName;

    @Column(name = "product_description", length = 200)
    private String productDescription;


    @Column(name = "product_price", columnDefinition = "DOUBLE PRECISION DEFAULT 0.0")
    private double productPrice;

    @Column(name = "product_quantity")
    private int quantity;


    @Column(name = "is_deleted")
    private boolean isDeleted;

    @CreationTimestamp
    @Column(name = "created_on", updatable = false)
    private LocalDateTime createdOn;

    @UpdateTimestamp
    @Column(name = "modified_on")
    private LocalDateTime modifiedOn;

    // Optional warehouse mapping to keep legacy products valid.
    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    private WarehouseVo warehouse;

}
