package org.example.stockifyims.repository.warehouse;

import org.example.stockifyims.dto.warehouse.WarehouseDTO;
import org.example.stockifyims.entity.WarehouseVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface WarehouseRepository extends JpaRepository<WarehouseVo, Long> {

    //Check duplicate name
    @Query(value = """
                SELECT COUNT(1)
                FROM warehouse
                    WHERE LOWER(warehouse_name) = LOWER(:warehouseName)
                AND warehouse_id <> :warehouseId
                AND is_deleted = :isDeleted
            """, nativeQuery = true)
    int countWarehouseByName(long warehouseId, String warehouseName, boolean isDeleted);

    //Check duplicate code
    @Query(value = """
                SELECT COUNT(1)
                FROM warehouse
                    WHERE LOWER(warehouse_code) = LOWER(:warehouseCode)
                AND warehouse_id <> :warehouseId
                AND is_deleted = :isDeleted
            """, nativeQuery = true)
    int countWarehouseByCode(long warehouseId, String warehouseCode, boolean isDeleted);

    // Show List of warehouse

    @Query("""
                SELECT new org.example.stockifyims.dto.warehouse.WarehouseDTO(
                w.warehouseId,
                w.warehouseName,
                w.warehouseCode
            )
                FROM WarehouseVo w WHERE w.isDeleted=false
            """)
    List<WarehouseDTO> findByIsDeletedFalseOrderByModifiedOnDesc();

    //Soft Delete
    @Modifying
    @Transactional
    @Query(value = """
                   UPDATE warehouse set is_deleted= :isDeleted
                   WHERE warehouse_id= :warehouseId
                   AND is_deleted= false
            """, nativeQuery = true)
    int setIsDeleted(long warehouseId, boolean isDeleted);
}
