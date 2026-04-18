package org.example.stockifyims.dto.product;

import lombok.Data;

@Data
public class ProductDTO {

    private long productId;
    private String productName;
    private String productDescription;
    private double productPrice;
    private int quantity;
    private Long warehouseId;
    private String warehouseName;
    private String warehouseCode;

}
