package org.example.stockifyims.controller.sales;

import org.example.stockifyims.apiresponse.ApiResponse;
import org.example.stockifyims.dto.sales.SalesDTO;
import org.example.stockifyims.entity.ProductVo;
import org.example.stockifyims.entity.SalesVo;
import org.example.stockifyims.repository.product.ProductRepository;
import org.example.stockifyims.service.sales.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/sales")
public class SalesController {

    @Autowired
    private SalesService salesService;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("")
    public ModelAndView salesPage() {
        ModelAndView view = new ModelAndView("sales");
        view.addObject("productList", productRepository.findAll());
        return view;
    }

    @GetMapping("/get-products")
    @ResponseBody
    public List<ProductVo> getProducts() {
        return productRepository.findAllByOrderByProductIdDesc();
    }

    @PostMapping("/save")
    @ResponseBody
    public ResponseEntity<ApiResponse> saveSales(@RequestBody SalesDTO salesDTO) {
        return salesService.saveSales(salesDTO);
    }

    // Show sale by ID
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<ApiResponse> showSalesById(@PathVariable long id) {
        return salesService.showSalesById(id);
    }

    // Show all sales
    @GetMapping("/allsales")
    @ResponseBody
    public ResponseEntity<ApiResponse> showAllSales() {
        try {
            return salesService.showAllSales();
        } catch (Exception e) {
            e.printStackTrace(); // VERY IMPORTANT
            return ResponseEntity.ok(new ApiResponse(true, 400, "Sales Not Found", null));
        }
    }

    @PostMapping("/process")
    @ResponseBody
    public ResponseEntity<ApiResponse> processSales(@RequestBody SalesVo salesVo) {
        SalesVo processed = salesService.processSales(salesVo);
        return ResponseEntity.ok(new ApiResponse(true, 200, "Sale processed", processed));
    }

    @GetMapping("/count")
    @ResponseBody
    public long countAllSales() {
        return salesService.countAllSales();
    }
}