package org.example.stockifyims.service.sales;

import jakarta.transaction.Transactional;
import org.example.stockifyims.apiresponse.ApiResponse;
import org.example.stockifyims.dto.sales.SalesDTO;
import org.example.stockifyims.entity.SalesVo;
import org.springframework.http.ResponseEntity;

public interface SalesService {

    ResponseEntity<ApiResponse> saveSales(SalesDTO salesDTO);

    ResponseEntity<ApiResponse> showSalesById(long id);

    ResponseEntity<ApiResponse> showAllSales();

    @Transactional
    SalesVo processSales(SalesVo sales);

    long countAllSales();
}
