package org.example.stockifyims.controller.product;

import org.example.stockifyims.apiresponse.ApiResponse;
import org.example.stockifyims.dto.product.ProductDTO;
import org.example.stockifyims.entity.ProductVo;
import org.example.stockifyims.repository.product.ProductRepository;
import org.example.stockifyims.service.excel.ExcelService;
import org.example.stockifyims.service.product.ProductService;
import org.example.stockifyims.service.report.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private ExcelService excelService;

    @Autowired
    private ReportService reportService;


//    @GetMapping("")
//    @ResponseBody
//    public String getPage() {
//        return "product";
//    }

    @GetMapping("")
    public ModelAndView productpage() {
        ModelAndView mv = new ModelAndView("product");
        mv.addObject("products", productService.getAllProducts());
        return mv;
    }

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> saveProductJson(@RequestBody ProductDTO productDTO) {
        return productService.saveProduct(productDTO);
    }

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<ApiResponse> saveProductForm(ProductDTO productDTO) {
        return productService.saveProduct(productDTO);
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllProducts() {

        return productService.getAllProducts();
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable long id) {
        Optional<ProductVo> product = productRepository.findById(id);

        if (product.isPresent()) {
            return ResponseEntity.ok(
                    new ApiResponse(true, 200, "Product found", product.get())
            );
        } else {
            return ResponseEntity.ok(
                    new ApiResponse(false, 404, "Product not found", null)
            );
        }
    }

    @PostMapping("/delete/{id}")

    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable long id) {

        return productService.deleteProduct(id);
    }

    @GetMapping("/download")
    @ResponseBody
    public ResponseEntity<byte[]> downloadProductsExcel() throws IOException {
        ByteArrayInputStream in = excelService.exportProductsToExcel();

        if (in == null || in.available() == 0) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new byte[0]);
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=products.xlsx")
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(in.readAllBytes());
    }

    /**
     * CSV export of active products (same data scope as Excel export).
     */
    @GetMapping("/download/csv")
    @ResponseBody
    public ResponseEntity<byte[]> downloadProductsCsv() {
        List<ProductVo> list = productRepository.findByIsDeletedFalseOrderByModifiedOnDesc();
        StringBuilder sb = new StringBuilder("productId,productName,productDescription,productPrice,quantity\n");
        for (ProductVo p : list) {
            sb.append(p.getProductId()).append(',')
                    .append(csvField(p.getProductName())).append(',')
                    .append(csvField(p.getProductDescription())).append(',')
                    .append(p.getProductPrice()).append(',')
                    .append(p.getQuantity()).append('\n');
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=products.csv")
                .contentType(new MediaType("text", "csv", StandardCharsets.UTF_8))
                .body(sb.toString().getBytes(StandardCharsets.UTF_8));
    }

    private static String csvField(String value) {
        if (value == null) {
            return "";
        }
        String escaped = value.replace("\"", "\"\"");
        if (escaped.contains(",") || escaped.contains("\n") || escaped.contains("\r")) {
            return "\"" + escaped + "\"";
        }
        return escaped;
    }

    @GetMapping("/barcode/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> downloadProductBarcode(@PathVariable long id) throws Exception {
        byte[] pdf = reportService.generateBarCodePdf(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=barcode_" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @ResponseBody
    @PostMapping("/upload")
    public Map<String, Object> uploadExcel(@RequestParam("file") MultipartFile file) {
        try {
            List<String> errors = excelService.uploadExcel(file);

            if (!errors.isEmpty()) {
                return Map.of("status", false, "data", errors);
            }

            return Map.of("status", true, "data", List.of());

        } catch (Exception e) {
            return Map.of("status", false, "data", List.of("Unexpected error: " + e.getMessage()));
        }
    }
}