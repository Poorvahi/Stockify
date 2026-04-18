package org.example.stockifyims.controller.purchase;

import org.example.stockifyims.apiresponse.ApiResponse;
import org.example.stockifyims.dto.purchase.PurchaseDTO;
import org.example.stockifyims.entity.ProductVo;
import org.example.stockifyims.entity.PurchaseVo;
import org.example.stockifyims.repository.product.ProductRepository;
import org.example.stockifyims.service.purchase.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/purchase")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("")
    public ModelAndView purchasePage() {
        ModelAndView view = new ModelAndView("purchase");
        view.addObject("productList", productRepository.findAll());
        return view;
    }

    // Fetch all products
    @GetMapping("/get-products")
    @ResponseBody
    public List<ProductVo> getProducts() {
        return productRepository.findAllByOrderByProductIdDesc();
    }

    // Save a new purchase
    @PostMapping("/save")
    @ResponseBody
    public ResponseEntity<ApiResponse> savePurchase(@RequestBody PurchaseDTO purchaseDTO) {
        return purchaseService.savePurchase(purchaseDTO);
    }

    // Show purchase by ID
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<ApiResponse> showPurchaseById(@PathVariable long id) {
        return purchaseService.showPurchaseById(id);
    }

    // Show all purchases
    @GetMapping("/allpurchase")
    @ResponseBody
    public ResponseEntity<ApiResponse> getAllPurchase() {
        try {
            return purchaseService.showAllPurchase();
        } catch (Exception e) {
            e.printStackTrace(); // VERY IMPORTANT
            return ResponseEntity.ok(new ApiResponse(true, 400, "Purchase Not Found", null));
        }
    }

    @PostMapping("/process")
    @ResponseBody
    public ResponseEntity<ApiResponse> processPurchase(@RequestBody PurchaseVo purchaseVo) {
        PurchaseVo processed = purchaseService.processPurchase(purchaseVo);
        return ResponseEntity.ok(new ApiResponse(true, 200, "Purchase processed", processed));
    }

    // Get total purchase count
    @GetMapping("/count")
    @ResponseBody
    public long countAllPurchase() {
        return purchaseService.countAllPurchase();
    }
}