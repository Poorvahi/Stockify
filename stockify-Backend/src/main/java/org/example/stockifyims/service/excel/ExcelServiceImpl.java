package org.example.stockifyims.service.excel;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.stockifyims.entity.ProductVo;
import org.example.stockifyims.repository.product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

@Service
public class ExcelServiceImpl implements ExcelService {
    @Autowired
    private ProductRepository productRepository;

    public ByteArrayInputStream exportProductsToExcel() throws IOException {
        List<ProductVo> products = productRepository.findByIsDeletedFalseOrderByModifiedOnDesc(); // fetch all non-deleted products

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Products");

            // Header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Sr. No.");
            headerRow.createCell(1).setCellValue("Name");
            headerRow.createCell(2).setCellValue("Description");
            headerRow.createCell(3).setCellValue("Price");
            headerRow.createCell(4).setCellValue("Stock");

            // Data rows
            int rowIdx = 1;
            int serialNo = 1;
            for (ProductVo product : products) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(serialNo++);
                row.createCell(1).setCellValue(product.getProductName());
                row.createCell(2).setCellValue(product.getProductDescription());
                row.createCell(3).setCellValue(product.getProductPrice());
                row.createCell(4).setCellValue(product.getQuantity());
            }

            // Auto-size columns
            for (int i = 0; i < 5; i++) sheet.autoSizeColumn(i);

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    @Override
    public List<String> uploadExcel(MultipartFile file) throws Exception {
        List<String> errors = new ArrayList<>();
        Map<String, ProductVo> stagedByName = new HashMap<>();

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String name = row.getCell(1) != null ? row.getCell(1).toString().trim() : "";
                String description = row.getCell(2) != null ? row.getCell(2).toString().trim() : "";

                double price = 0;
                int quantity = 0;

                if (row.getCell(3) != null && row.getCell(3).getCellType() == CellType.NUMERIC) {
                    price = row.getCell(3).getNumericCellValue();
                }

                if (row.getCell(4) != null && row.getCell(4).getCellType() == CellType.NUMERIC) {
                    quantity = (int) row.getCell(4).getNumericCellValue();
                }

                if (name.isEmpty()) {
                    errors.add("Row " + (i + 1) + ": Product name is empty, skipping.");
                    continue;
                }

                if (price < 0) {
                    errors.add("Row " + (i + 1) + ": Price cannot be negative.");
                    continue;
                }

                if (quantity < 0) {
                    errors.add("Row " + (i + 1) + ": Stock cannot be negative.");
                    continue;
                }

                String normalizedName = name.toLowerCase(Locale.ROOT);

                ProductVo product = stagedByName.get(normalizedName);
                if (product == null) {
                    product = productRepository
                            .findByProductNameIgnoreCaseAndIsDeletedFalse(name)
                            .orElse(new ProductVo()); // if not found, create new
                }

                product.setProductName(name);
                product.setProductDescription(description);
                product.setProductPrice(price);
                product.setQuantity(quantity);

                stagedByName.put(normalizedName, product);
            }
        }

        if (!errors.isEmpty()) {
            return errors; // return row errors if any
        }

        // Save only the final state per product name to avoid duplicate inserts in same upload.
        productRepository.saveAll(new ArrayList<>(stagedByName.values()));

        return errors; // empty = success
    }
}