package org.example.stockifyims.repository.sales;

import org.example.stockifyims.entity.SalesVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SalesRepository extends JpaRepository<SalesVo,Long> {
    boolean existsByProduct_ProductIdAndIsDeletedFalse(long id);

    @Query("SELECT s FROM SalesVo s WHERE s.isDeleted = false ORDER BY s.createdOn DESC ")
    List<SalesVo> showActiveSales();

}
