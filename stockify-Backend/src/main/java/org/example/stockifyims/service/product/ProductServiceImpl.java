package org.example.stockifyims.service.product;

import org.example.stockifyims.apiresponse.ApiResponse;
import org.example.stockifyims.dto.product.ProductDTO;
import org.example.stockifyims.entity.ProductVo;
import org.example.stockifyims.entity.WarehouseVo;
import org.example.stockifyims.repository.product.ProductRepository;
import org.example.stockifyims.repository.purchase.PurchaseRepository;
import org.example.stockifyims.repository.sales.SalesRepository;
import org.example.stockifyims.repository.warehouse.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private SalesRepository salesRepository;
    @Autowired
    private WarehouseRepository warehouseRepository;

    public ResponseEntity<ApiResponse> saveProduct(ProductDTO dto) {

        long id = dto.getProductId();
        String name = dto.getProductName() != null
                ? dto.getProductName().trim()
                : "";
        boolean isCreate = (id == 0);

        int nameCount = isCreate
                ? productRepository.countByProductNameIgnoreCaseAndIsDeletedFalse(name)
                : productRepository.countByProductNameIgnoreCaseAndProductIdNotAndIsDeletedFalse(name, id);

        if (nameCount > 0) {
            // Keep HTTP 200 for JSP clients so they can show toastr from response payload.
            return ResponseEntity.ok(
                    new ApiResponse(false, 409, "Product already exists.", null)
            );
        }

        if (dto.getProductPrice() < 0) {
            return ResponseEntity.ok(
                    new ApiResponse(false, 400, "Product price cannot be negative.", null)
            );
        }

        if (dto.getQuantity() < 0) {
            return ResponseEntity.ok(
                    new ApiResponse(false, 400, "Product stock cannot be negative.", null)
            );
        }

        ProductVo product;
        String message;

        if (!isCreate) {
            product = productRepository.findById(id)
                    .orElse(null);

            if (product == null) {
                return ResponseEntity.ok(
                        new ApiResponse(false, 404, "Product not found", null)
                );
            }
            message = "Updated successfully";
        } else {
            product = new ProductVo();
            message = "Saved successfully";
        }

        product.setProductName(name);
        product.setProductDescription(dto.getProductDescription());
        product.setProductPrice(dto.getProductPrice());
        product.setQuantity(dto.getQuantity());
        // Keep warehouse optional for backward compatibility.
        if (dto.getWarehouseId() != null && dto.getWarehouseId() > 0) {
            WarehouseVo warehouse = warehouseRepository.findById(dto.getWarehouseId()).orElse(null);
            product.setWarehouse(warehouse);
        }

        productRepository.save(product);

        return ResponseEntity.ok(new ApiResponse(true, 200, message, product));
    }

    @Override
    public ResponseEntity<ApiResponse> getProductList(long productId) {
        ProductVo product = productRepository.findById(productId).orElse(null);

        if (product == null) {
            return ResponseEntity.ok(
                    new ApiResponse(false, 404, "Product not found", null)
            );
        }

        return ResponseEntity.ok(
                new ApiResponse(true, 200, "Product found", product)
        );
    }

    @Override
    public ResponseEntity<ApiResponse> getAllProducts() {

        List<ProductVo> products = productRepository.findByIsDeletedFalseOrderByModifiedOnDesc();

        List<ProductDTO> dtoList = products.stream().map(p -> {
            ProductDTO dto = new ProductDTO();
            dto.setProductId(p.getProductId());
            dto.setProductName(p.getProductName());
            dto.setProductDescription(p.getProductDescription());
            dto.setProductPrice(p.getProductPrice());
            dto.setQuantity(p.getQuantity());
            if (p.getWarehouse() != null) {
                dto.setWarehouseId(p.getWarehouse().getWarehouseId());
                dto.setWarehouseName(p.getWarehouse().getWarehouseName());
                dto.setWarehouseCode(p.getWarehouse().getWarehouseCode());
            }
            return dto;
        }).toList();

        return ResponseEntity.ok(
                new ApiResponse(true, 200, "Products fetched successfully", dtoList)
        );
    }

    @Override
    public ResponseEntity<ApiResponse> deleteProduct(long productId) {

        ProductVo product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product Not Found"));

        // Check in Purchase
        boolean existsInPurchase = purchaseRepository
                .existsByProduct_ProductIdAndIsDeletedFalse(productId);

        // Check in Sales
        boolean existsInSales = salesRepository
                .existsByProduct_ProductIdAndIsDeletedFalse(productId);

        if (existsInPurchase || existsInSales) {
            return ResponseEntity.ok(
                    new ApiResponse(false, 400,
                            "Product cannot be deleted. It is used in Purchase/Sales.",
                            null)
            );
        }

        product.setDeleted(true);
        productRepository.save(product);

        return ResponseEntity.ok(
                new ApiResponse(true, 200, "Product deleted successfully", null)
        );
    }

    @Override
    public long countAllProducts() {
        return productRepository.countByIsDeletedFalse();
    }

    @Override
    public long countLowStock() {
        long count = productRepository. countLowStock();
        System.out.println("LOW STOCK COUNT = " + count);
        return count;
    }

    //Download
    @Override
    public Map<String, Object> saveFromExcel(List<ProductVo> products) {
        List<String> errors = new ArrayList<>();

        for (int i = 0; i < products.size(); i++) {
            ProductVo product = products.get(i);
            int row = i + 2;

            if (product.getProductName() == null || product.getProductName().isEmpty()) {
                errors.add("Row " + (i + 1) + ": Name is empty");
                continue;
            }
            if (product.getProductDescription().isEmpty()) {
                errors.add("Row " + (i + 1) + ": Description is empty");
                continue;
            }

            if (product.getProductPrice() <= 0)
                errors.add("Row " + row + ": Price must be greater than 0");

            if (product.getQuantity() <= 0)
                errors.add("Row " + row + ": Quantity must be greater than 0");
        }

        if (!errors.isEmpty()) {
            return Map.of("status", false, "data", errors);
        }

        productRepository.saveAll(products);
        return Map.of("status", true, "data", List.of());

    }

}
