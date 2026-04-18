package org.example.stockifyims.service.purchase;

import org.example.stockifyims.apiresponse.ApiResponse;
import org.example.stockifyims.dto.purchase.PurchaseDTO;
import org.example.stockifyims.entity.PurchaseVo;
import org.springframework.http.ResponseEntity;

public interface PurchaseService {
    ResponseEntity<ApiResponse> savePurchase(PurchaseDTO purchaseDTO);

    ResponseEntity<ApiResponse> showPurchaseById(long id);

    ResponseEntity<ApiResponse> showAllPurchase();

    PurchaseVo processPurchase(PurchaseVo purchaseVo);

    long countAllPurchase();

}
