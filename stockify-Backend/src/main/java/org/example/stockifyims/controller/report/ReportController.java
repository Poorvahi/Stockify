package org.example.stockifyims.controller.report;

import org.example.stockifyims.repository.product.ProductRepository;
import org.example.stockifyims.service.report.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private ReportService reportService;
    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/sales/{id}")
    public ResponseEntity<byte[]> generateSalesReport(@PathVariable long id) throws Exception {
        byte[] pdf = reportService.generateSalesReport(id);
        return ResponseEntity.ok()
                .header("Content-Disposition", "inline; filename=sales_Report.pdf")
                .contentType(MediaType.APPLICATION_PDF).body(pdf);
    }

    @GetMapping("/purchase/{id}")
    public ResponseEntity<byte[]> generatePurchaseReport(@PathVariable long id) throws Exception {
        byte[] pdfpurchase = reportService.generatePurchaseReport(id);
        return ResponseEntity.ok()
                .header("Content-Disposition", "inline; filename=purchase_Report.pdf")
                .contentType(MediaType.APPLICATION_PDF).body(pdfpurchase);
    }


    @GetMapping("/barcode/{id}")
    public ResponseEntity<byte[]> generateReport(@PathVariable long id) throws Exception {
        byte[] pdf = reportService.generateBarCodePdf(id);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=barcode.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

}
