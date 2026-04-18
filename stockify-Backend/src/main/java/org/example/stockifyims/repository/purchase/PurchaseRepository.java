package org.example.stockifyims.repository.purchase;

import org.example.stockifyims.entity.PurchaseVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PurchaseRepository extends JpaRepository<PurchaseVo,Long> {
    boolean existsByProduct_ProductIdAndIsDeletedFalse(long id);

    @Query("SELECT p FROM PurchaseVo p WHERE p.isDeleted = false ORDER BY p.createdOn DESC ")
    List<PurchaseVo> showActivePurchase();
}
