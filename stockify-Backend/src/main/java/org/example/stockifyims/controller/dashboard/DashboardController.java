package org.example.stockifyims.controller.dashboard;

import org.example.stockifyims.service.product.ProductService;
import org.example.stockifyims.service.purchase.PurchaseService;
import org.example.stockifyims.service.sales.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @Autowired
    private ProductService productService;

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private SalesService salesService;

    @GetMapping("/index")
    public String showDashboard(Model model) {
        model.addAttribute("productCount", productService.countAllProducts());
        model.addAttribute("purchaseCount", purchaseService.countAllPurchase());
        model.addAttribute("salesCount", salesService.countAllSales());
        model.addAttribute("lowStockCount", productService.countLowStock());

        return "index";
    }
}