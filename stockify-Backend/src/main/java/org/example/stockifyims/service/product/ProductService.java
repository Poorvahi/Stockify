package org.example.stockifyims.service.product;

import org.example.stockifyims.apiresponse.ApiResponse;
import org.example.stockifyims.dto.product.ProductDTO;
import org.example.stockifyims.entity.ProductVo;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface ProductService {

    public ResponseEntity<ApiResponse> saveProduct(ProductDTO productDTO);

    public ResponseEntity<ApiResponse> getProductList(long productId);

    public ResponseEntity<ApiResponse> getAllProducts();

    public ResponseEntity<ApiResponse> deleteProduct(long productId);

    long countAllProducts();

    long countLowStock();

    Map<String, Object> saveFromExcel(List<ProductVo> products);

}
