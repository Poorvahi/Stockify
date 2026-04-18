package org.example.stockifyims.dto.purchase;

import lombok.Data;

@Data
public class PurchaseDTO {

    private long purchaseId;
    private Long productId;
    private double quantity;
    private double price;
    private double total;
    private Long warehouseId;

}
