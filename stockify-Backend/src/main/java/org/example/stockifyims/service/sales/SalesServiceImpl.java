package org.example.stockifyims.service.sales;

import org.example.stockifyims.apiresponse.ApiResponse;
import org.example.stockifyims.dto.sales.SalesDTO;
import org.example.stockifyims.entity.ProductVo;
import org.example.stockifyims.entity.SalesVo;
import org.example.stockifyims.entity.StockMovementVo;
import org.example.stockifyims.entity.WarehouseVo;
import org.example.stockifyims.repository.product.ProductRepository;
import org.example.stockifyims.repository.sales.SalesRepository;
import org.example.stockifyims.repository.warehouse.StockMovementRepository;
import org.example.stockifyims.repository.warehouse.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SalesServiceImpl implements SalesService {

    @Autowired
    private SalesRepository salesRepository;

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private WarehouseRepository warehouseRepository;
    @Autowired
    private StockMovementRepository stockMovementRepository;

    @Override
    public ResponseEntity<ApiResponse> saveSales(SalesDTO salesDTO) {
        try {
            SalesVo sales;
            ProductVo product = productRepository.findById(salesDTO.getProductId())
                    .orElse(null);

            if (product == null) {
                return ResponseEntity.ok(new ApiResponse(false, 404, "Product not found", null));
            }

            if (product.getQuantity() < salesDTO.getQuantity()) {
                return ResponseEntity.ok(new ApiResponse(false, 400, "Insufficient product quantity. Available: " + product.getQuantity(), null));
            }

            if (salesDTO.getSalesId() != 0) {
                // UPDATE
                sales = salesRepository.findById(salesDTO.getSalesId())
                        .orElseThrow(() -> new RuntimeException("Purchase not found"));
            } else {
                // CREATE
                sales = new SalesVo();
            }

            sales.setProduct(product);
            sales.setPrice(salesDTO.getPrice());
            sales.setQuantity(salesDTO.getQuantity());
            double total = product.getProductPrice() * salesDTO.getQuantity();
            sales.setTotal(total);

            product.setQuantity((int) (product.getQuantity() - salesDTO.getQuantity()));
            productRepository.save(product);

            SalesVo savedSales = salesRepository.save(sales);
            WarehouseVo warehouse = resolveWarehouse(product, salesDTO.getWarehouseId());
            if (warehouse != null) {
                product.setWarehouse(warehouse);
                productRepository.save(product);
                saveMovement(product, warehouse, salesDTO.getQuantity(), "OUT", "SALES", savedSales.getSalesId());
            }

            String message = (salesDTO.getSalesId() == 0)
                    ? "Sales created successfully"
                    : "Sales updated successfully";

            return ResponseEntity.ok(new ApiResponse(true, 200, message, savedSales.getSalesId()));

        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse(false, 500, e.getMessage(), null));
        }
    }


    @Override
    public ResponseEntity<ApiResponse> showSalesById(long id) {

        Optional<SalesVo> salesVo = salesRepository.findById(id);

        if (salesVo.isPresent()) {
            return ResponseEntity.ok(new ApiResponse(true, 200, "Salesfound", salesVo.get()));
        } else {
            return ResponseEntity.ok(
                    new ApiResponse(false, 400, "Sales not found", null));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> showAllSales() {

        return ResponseEntity.ok(new ApiResponse(true, 200, "All Sales Fetched", salesRepository.showActiveSales()));

    }

    @Override
    public SalesVo processSales(SalesVo sales) {
        ProductVo product = productRepository.findById(
                sales.getProduct().getProductId()
        ).orElseThrow(() -> new RuntimeException("Product not found"));

        product.setQuantity((int) (product.getQuantity() + sales.getQuantity()));
        productRepository.save(product);


        sales.setTotal(sales.getPrice() * sales.getQuantity());
        SalesVo saved = salesRepository.save(sales);
        if (product.getWarehouse() != null) {
            saveMovement(product, product.getWarehouse(), sales.getQuantity(), "OUT", "SALES", saved.getSalesId());
        }
        return saved;

    }

    @Override
    public long countAllSales() {

        return salesRepository.count();
    }

    private WarehouseVo resolveWarehouse(ProductVo product, Long warehouseId) {
        if (warehouseId != null && warehouseId > 0) {
            return warehouseRepository.findById(warehouseId).orElse(null);
        }
        return product.getWarehouse();
    }

    private void saveMovement(ProductVo product, WarehouseVo warehouse, double quantity, String movementType, String refType, long refId) {
        // Persist stock movement for traceability per warehouse.
        StockMovementVo movement = new StockMovementVo();
        movement.setProduct(product);
        movement.setWarehouse(warehouse);
        movement.setQuantity(quantity);
        movement.setMovementType(movementType);
        movement.setReferenceType(refType);
        movement.setReferenceId(refId);
        stockMovementRepository.save(movement);
    }
}
