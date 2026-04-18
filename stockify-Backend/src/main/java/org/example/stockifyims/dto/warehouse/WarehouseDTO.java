package org.example.stockifyims.dto.warehouse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseDTO {
    private long warehouseId;
    private String warehouseName;
    private String warehouseCode;
}
