package org.example.stockifyims.controller.dashboard;

import org.example.stockifyims.apiresponse.ApiResponse;
import org.example.stockifyims.entity.ProductVo;
import org.example.stockifyims.repository.product.ProductRepository;
import org.example.stockifyims.repository.purchase.PurchaseRepository;
import org.example.stockifyims.repository.sales.SalesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/dashboard")
public class DashboardAnalyticsController {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private PurchaseRepository purchaseRepository;
    @Autowired
    private SalesRepository salesRepository;

    @GetMapping("/analytics")
    public ResponseEntity<ApiResponse> analytics() {
        List<ProductVo> products = productRepository.findByIsDeletedFalseOrderByModifiedOnDesc();
        Map<String, Object> data = new HashMap<>();

        data.put("totalProducts", products.size());
        data.put("lowStockItems", products.stream().filter(p -> p.getQuantity() < 10).count());
        data.put("totalSales", salesRepository.count());
        data.put("totalPurchases", purchaseRepository.count());

        // Category approximation from product description first token.
        Map<String, Long> categoryDistribution = new LinkedHashMap<>();
        for (ProductVo p : products) {
            String raw = p.getProductDescription() == null ? "" : p.getProductDescription().trim();
            String category = raw.isEmpty() ? "Uncategorized" : raw.split("\\s+")[0];
            categoryDistribution.put(category, categoryDistribution.getOrDefault(category, 0L) + 1);
        }
        data.put("categoryDistribution", categoryDistribution);

        long available = products.stream().filter(p -> p.getQuantity() >= 10).count();
        long low = products.stream().filter(p -> p.getQuantity() > 0 && p.getQuantity() < 10).count();
        long out = products.stream().filter(p -> p.getQuantity() <= 0).count();
        data.put("stockStatus", Map.of("available", available, "low", low, "out", out));

        Map<String, Double> salesOverTime = new LinkedHashMap<>();
        for (int i = 6; i >= 0; i--) {
            LocalDate day = LocalDate.now().minusDays(i);
            double total = salesRepository.findAll().stream()
                    .filter(s -> s.getCreatedOn() != null && s.getCreatedOn().toLocalDate().equals(day))
                    .mapToDouble(s -> s.getTotal())
                    .sum();
            salesOverTime.put(day.toString(), total);
        }
        data.put("salesOverTime", salesOverTime);

        return ResponseEntity.ok(new ApiResponse(true, 200, "Dashboard analytics fetched", data));
    }
}
