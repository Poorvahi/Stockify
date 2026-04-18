package org.example.stockifyims.service.report;

import org.example.stockifyims.entity.ProductVo;

import java.util.List;

public interface ReportService {

    byte[] generatePurchaseReport(long id) throws Exception;

    byte[] generateSalesReport(long id) throws Exception;

    byte[] generateBarCodePdf(long id) throws Exception;
}
