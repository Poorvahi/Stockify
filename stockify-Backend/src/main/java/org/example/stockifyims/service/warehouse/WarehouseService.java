package org.example.stockifyims.service.warehouse;

import org.example.stockifyims.apiresponse.ApiResponse;
import org.example.stockifyims.dto.warehouse.WarehouseDTO;
import org.springframework.http.ResponseEntity;

public interface WarehouseService{
    ResponseEntity<ApiResponse> saveWarehouse(WarehouseDTO warehouseDTO);

    ResponseEntity<ApiResponse> getWarehouseList();

    ResponseEntity<ApiResponse> deleteWarehouse(long warehouseId);
}
