package org.example.stockifyims.service.warehouse;

import lombok.extern.slf4j.Slf4j;
import org.example.stockifyims.apiresponse.ApiResponse;
import org.example.stockifyims.dto.warehouse.WarehouseDTO;
import org.example.stockifyims.entity.WarehouseVo;
import org.example.stockifyims.repository.warehouse.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class WarehouseServiceImpl implements WarehouseService {
    @Autowired
    private WarehouseRepository warehouseRepository;

    @Override
    public ResponseEntity<ApiResponse> saveWarehouse(WarehouseDTO warehouseDTO) {

        int warehouseNameCount = warehouseRepository.countWarehouseByName(warehouseDTO.getWarehouseId(), warehouseDTO.getWarehouseName().trim(), false);
        if (warehouseNameCount != 0)
            return ResponseEntity.ok(new ApiResponse(false,400, "Warehouse name already exists.", null));

        int warehouseCodeCount = warehouseRepository.countWarehouseByCode(warehouseDTO.getWarehouseId(), warehouseDTO.getWarehouseCode().trim(), false);
        if (warehouseCodeCount != 0)
            return ResponseEntity.ok(new ApiResponse(false, 429, "Warehouse code already exists.", null));

        WarehouseVo warehouseVo = new WarehouseVo();
        warehouseVo.setWarehouseId(warehouseDTO.getWarehouseId());
        warehouseVo.setWarehouseName(warehouseDTO.getWarehouseName());
        warehouseVo.setWarehouseCode(warehouseDTO.getWarehouseCode());

        WarehouseVo saveWarehouseVo = warehouseRepository.save(warehouseVo);
        String message = warehouseDTO.getWarehouseId() == 0 ? "Warehouse created successfully." : "Warehouse updated successfully.";
        return ResponseEntity.ok(new ApiResponse(true, 200, message, saveWarehouseVo.getWarehouseId()));
    }

    @Override
    public ResponseEntity<ApiResponse> getWarehouseList() {

        List<WarehouseDTO> warehouseList = warehouseRepository.findByIsDeletedFalseOrderByModifiedOnDesc();
        if (warehouseList.isEmpty()) {
            return ResponseEntity.ok(new ApiResponse(false, 404, "No warehouse records found.", null));
        }
        return ResponseEntity.ok(new ApiResponse(true, 200, "Warehouse records fetched successfully.", warehouseList));
    }

    @Override
    public ResponseEntity<ApiResponse> deleteWarehouse(long warehouseId) {

        boolean warehouseExists=warehouseRepository.existsById(warehouseId);

        if(!warehouseExists){
            return ResponseEntity.ok(new ApiResponse(false,404,"Warehouse Doesn't exists",null));
        }

        else{
            warehouseRepository.setIsDeleted(warehouseId, true);
            return ResponseEntity.ok(new ApiResponse(true,200, "Warehouse deleted successfully.", null));
        }
    }
}
