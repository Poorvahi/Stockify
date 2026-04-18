package org.example.stockifyims.controller.warehouse;

import org.example.stockifyims.apiresponse.ApiResponse;
import org.example.stockifyims.dto.warehouse.WarehouseDTO;
import org.example.stockifyims.repository.warehouse.StockMovementRepository;
import org.example.stockifyims.service.warehouse.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/warehouse")
@RestController
public class WarehouseController {
    @Autowired
    private WarehouseService warehouseService;
    @Autowired
    private StockMovementRepository stockMovementRepository;

    @PostMapping("/save")
    public ResponseEntity<ApiResponse> saveWarehouse(@RequestBody WarehouseDTO warehouseDTO) {
        return warehouseService.saveWarehouse(warehouseDTO);
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getWarehouseList() {
        return warehouseService.getWarehouseList();
    }

    @PostMapping("/delete/{warehouseId}")
    public ResponseEntity<ApiResponse> deleteWarehouse(@PathVariable long warehouseId) {
        return warehouseService.deleteWarehouse(warehouseId);
    }

    @GetMapping("/stock-overview")
    public ResponseEntity<ApiResponse> warehouseStockOverview() {
        return ResponseEntity.ok(new ApiResponse(true, 200, "Warehouse stock fetched", stockMovementRepository.warehouseProductStock()));
    }

    @GetMapping("/movements")
    public ResponseEntity<ApiResponse> stockMovements() {
        return ResponseEntity.ok(new ApiResponse(true, 200, "Stock movements fetched", stockMovementRepository.findTop50ByOrderByCreatedOnDesc()));
    }

}
