package org.example.stockifyims.repository.warehouse;

import org.example.stockifyims.entity.StockMovementVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface StockMovementRepository extends JpaRepository<StockMovementVo, Long> {
    List<StockMovementVo> findTop50ByOrderByCreatedOnDesc();

    @Query(value = """
            SELECT w.warehouse_name as warehouseName,
                   p.product_name as productName,
                   p.product_quantity as quantity
            FROM product p
            JOIN warehouse w on w.warehouse_id = p.warehouse_id
            WHERE p.is_deleted = false AND w.is_deleted = false
            ORDER BY w.warehouse_name, p.product_name
            """, nativeQuery = true)
    List<Map<String, Object>> warehouseProductStock();
}
