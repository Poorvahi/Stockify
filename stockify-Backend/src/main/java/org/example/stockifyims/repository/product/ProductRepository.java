package org.example.stockifyims.repository.product;

import org.example.stockifyims.entity.ProductVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductVo, Long> {

    // Count products with the same name for NEW products
    @Query(value = """
                SELECT COUNT(1)
                FROM product
                WHERE LOWER(product_name) = LOWER(?1)
                AND is_deleted = false
            """, nativeQuery = true)
    int countByProductNameIgnoreCaseAndIsDeletedFalse(String productName);

    // Count products with the same name for UPDATE (ignore current product)
    @Query(value = """
                SELECT COUNT(1)
                FROM product
                WHERE LOWER(product_name) = LOWER(?1)
                AND product_id <> ?2
                AND is_deleted = false
            """, nativeQuery = true)
    int countByProductNameIgnoreCaseAndProductIdNotAndIsDeletedFalse(String productName, long productId);

    // Show Product List (OrderByDesc)
    @Query(value = """
                SELECT *
                FROM product
                WHERE is_deleted = false
                ORDER BY modified_on DESC
            """, nativeQuery = true)
    List<ProductVo> findByIsDeletedFalseOrderByModifiedOnDesc();

    // Find All Products OrderBy ID Desc
    @Query(value = """
                SELECT *
                FROM product
                ORDER BY product_id DESC
            """, nativeQuery = true)
    List<ProductVo> findAllByOrderByProductIdDesc();

    // Total active products
    @Query(value = """
                SELECT COUNT(1)
                FROM product
                WHERE is_deleted = false
            """, nativeQuery = true)
    long countByIsDeletedFalse();

    // Count Low Stock for dashboard
    @Query(value = """
                SELECT COUNT(1)
                FROM product
                WHERE is_deleted = false
                AND quantity < 10
            """, nativeQuery = true)
    long countLowStock();

    // Find by name for excel upload
    @Query(value = """
                SELECT *
                FROM product
                WHERE LOWER(product_name) = LOWER(?1)
                AND is_deleted = false
                LIMIT 1
            """, nativeQuery = true)
    Optional<ProductVo> findByProductNameIgnoreCaseAndIsDeletedFalse(String productName);
}