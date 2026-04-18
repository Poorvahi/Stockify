package org.example.stockifyims.service.report;

import net.sf.jasperreports.engine.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private DataSource dataSource;

    @Override
    public byte[] generatePurchaseReport(long id) throws Exception {
        InputStream reportStream = getClass().getResourceAsStream("/reports/PurchaseMaster.jrxml");

        if (reportStream == null) {
            throw new RuntimeException("File Not Found");
        }

        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
        Map<String, Object> parameters = new HashMap<>();
        // Path variable is purchase row id (matches frontend); templates filter on purchase_id.
        parameters.put("purchase_id", id);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource.getConnection());

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    @Override
    public byte[] generateSalesReport(long id) throws Exception {
        InputStream reportStream2 = getClass().getResourceAsStream("/reports/SalesMaster.jrxml");

        if (reportStream2 == null) {
            throw new RuntimeException("File Not Found");
        }

        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream2);
        Map<String, Object> parameters = new HashMap<>();
        // Path variable is sales row id; templates filter on sales_id.
        parameters.put("sales_id", id);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource.getConnection());

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    @Override
    public byte[] generateBarCodePdf(long id) throws Exception {
        InputStream reportstream = getClass().getResourceAsStream("/reports/Barcode.jrxml");
        if (reportstream == null) {
            throw new RuntimeException("File Not Found");
        }
        JasperReport jasperReport = JasperCompileManager.compileReport(reportstream);
        Map<String, Object> params = new HashMap<>();
        // Barcode.jrxml uses SQL query with $P{product_id}
        params.put("product_id", (int) id);

        JasperPrint print = JasperFillManager.fillReport(jasperReport, params, dataSource.getConnection());

        return JasperExportManager.exportReportToPdf(print);
    }
}
