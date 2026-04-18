package org.example.stockifyims.dto.sales;

import lombok.Data;

@Data
public class SalesDTO {

    private long salesId;
    private Long productId;
    private double quantity;
    private double price;
    private double total;
    private Long warehouseId;

}
