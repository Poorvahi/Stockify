package org.example.stockifyims.service.excel;

import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

public interface ExcelService {
    
    ByteArrayInputStream exportProductsToExcel() throws IOException;

    List<String> uploadExcel(MultipartFile file) throws Exception;

}
