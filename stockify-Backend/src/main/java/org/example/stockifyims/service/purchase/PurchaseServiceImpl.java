package org.example.stockifyims.service.purchase;

import jakarta.transaction.Transactional;
import org.example.stockifyims.apiresponse.ApiResponse;
import org.example.stockifyims.dto.purchase.PurchaseDTO;
import org.example.stockifyims.entity.ProductVo;
import org.example.stockifyims.entity.PurchaseVo;
import org.example.stockifyims.entity.StockMovementVo;
import org.example.stockifyims.entity.WarehouseVo;
import org.example.stockifyims.repository.product.ProductRepository;
import org.example.stockifyims.repository.purchase.PurchaseRepository;
import org.example.stockifyims.repository.warehouse.StockMovementRepository;
import org.example.stockifyims.repository.warehouse.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PurchaseServiceImpl implements PurchaseService {
    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private WarehouseRepository warehouseRepository;
    @Autowired
    private StockMovementRepository stockMovementRepository;

    @Override
    public ResponseEntity savePurchase(PurchaseDTO purchaseDTO) {

        String message = null;
        try {
            PurchaseVo purchase;
            ProductVo product = productRepository.findById(purchaseDTO.getProductId())
                    .orElse(null);

            if (product == null) {
                return ResponseEntity.ok(new ApiResponse(false, 404, "Product not found", null));
            }
            if (purchaseDTO.getPurchaseId() != 0) {
                // UPDATE
                purchase = purchaseRepository.findById(purchaseDTO.getPurchaseId())
                        .orElseThrow(() -> new RuntimeException("Purchase not found"));
            } else {
                // CREATE
                purchase = new PurchaseVo();
            }

            purchase.setProduct(product);
            purchase.setPrice(purchaseDTO.getPrice());
            purchase.setQuantity(purchaseDTO.getQuantity());

            double total = product.getProductPrice() * purchaseDTO.getQuantity();
            purchase.setTotal(total);
            product.setQuantity((int) (product.getQuantity() + purchaseDTO.getQuantity()));
            productRepository.save(product);

            PurchaseVo savedPurchase = purchaseRepository.save(purchase);
            WarehouseVo warehouse = resolveWarehouse(product, purchaseDTO.getWarehouseId());
            if (warehouse != null) {
                product.setWarehouse(warehouse);
                productRepository.save(product);
                saveMovement(product, warehouse, purchaseDTO.getQuantity(), "IN", "PURCHASE", savedPurchase.getPurchaseId());
            }

            message = (purchaseDTO.getPurchaseId() == 0)
                    ? "Purchase created successfully"
                    : "Purchase updated successfully";

            return ResponseEntity.ok(
                    new ApiResponse(true, 200, message, savedPurchase.getPurchaseId())
            );

        }  catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.ok(new ApiResponse(false, 500, e.getMessage(), null));
    }
    }

    @Override
    public ResponseEntity<ApiResponse> showPurchaseById(long id) {

        Optional<PurchaseVo> purchase = purchaseRepository.findById(id);

        if (purchase.isPresent()) {
            return ResponseEntity.ok(new ApiResponse(true, 200, "Purchase found", purchase.get()));
        } else {
            return ResponseEntity.ok(
                    new ApiResponse(false, 400, "Purchase not found", null));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> showAllPurchase() {
        return ResponseEntity.ok(new ApiResponse(true, 200, "All Purchase Fetched", purchaseRepository.showActivePurchase()));
    }

    // Process Purchase (update stock + save)
    @Transactional
    @Override
    public PurchaseVo processPurchase(PurchaseVo purchaseVo) {

        ProductVo product = productRepository.findById(
                purchaseVo.getProduct().getProductId()
        ).orElseThrow(() -> new RuntimeException("Product not found"));

        product.setQuantity((int) (product.getQuantity() + purchaseVo.getQuantity()));
        productRepository.save(product);


        purchaseVo.setTotal(purchaseVo.getPrice() * purchaseVo.getQuantity());
        PurchaseVo saved = purchaseRepository.save(purchaseVo);
        if (product.getWarehouse() != null) {
            saveMovement(product, product.getWarehouse(), purchaseVo.getQuantity(), "IN", "PURCHASE", saved.getPurchaseId());
        }
        return saved;
    }

    // Count purchase
    @Override
    public long countAllPurchase() {
        return purchaseRepository.count();
    }


    private WarehouseVo resolveWarehouse(ProductVo product, Long warehouseId) {
        if (warehouseId != null && warehouseId > 0) {
            return warehouseRepository.findById(warehouseId).orElse(null);
        }
        return product.getWarehouse();
    }

    private void saveMovement(ProductVo product, WarehouseVo warehouse, double quantity, String movementType, String refType, long refId) {
        // Dedicated stock movement audit record for warehouse-wise tracing.
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
