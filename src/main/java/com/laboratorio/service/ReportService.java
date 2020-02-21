package com.laboratorio.service;

import com.laboratorio.dao.ReporteDao;
import com.laboratorio.enums.ReportTypeEnum;
import com.laboratorio.exception.ApiRequestException;
import com.laboratorio.model.Report;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.*;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.Date;
import java.util.Map;

@Slf4j
@Service
public class ReportService {

    final
    ReporteDao reportDao;

    final
    ServletContext context;

    final
    DataSource datasource;

    public ReportService(ReporteDao reportDao, ServletContext context, DataSource datasource) {
        this.reportDao = reportDao;
        this.context = context;
        this.datasource = datasource;
    }

    public void exportPdfFile(HttpServletResponse response, Map<String, Object> params) {

        if (params.get("codeReport") == null) {
            throw new ApiRequestException("The code of the report is required!");
        }

        OutputStream outstr = null;

        try (Connection conn = datasource.getConnection()) {

            String path = context.getRealPath("/");
            String folderReports = "/jasper/";
            String codeReport = (String) params.get("codeReport");
            String reportType = params.get("reportType") != null ? (String) params.get("reportType") : ReportTypeEnum.PDF.toString();
            String leafType = params.get("leafType") != null ? (String) params.get("leafType") : "A4";

            Report report = reportDao.getReport(codeReport, leafType);

            log.info("Info report {}", report.toString());

            JasperPrint jasperPrint = JasperFillManager.fillReport(path + folderReports + report.getFileName() + ".jasper", params, conn);

            response.setHeader("Date", new Date().toString());

            if (ReportTypeEnum.PDF.toString().equals(reportType)) {
                log.info("Generating report pdf");
                response.setContentType("application/pdf");
                response.setHeader("Content-Disposition", "inline; filename=\"" + report.getTitle() + ".pdf\"");

                Exporter<ExporterInput, PdfReportConfiguration, PdfExporterConfiguration, OutputStreamExporterOutput> exporter = new JRPdfExporter();
                exporter.setConfiguration(new SimplePdfReportConfiguration());

                SimplePdfExporterConfiguration pdfExporter = new SimplePdfExporterConfiguration();
                pdfExporter.setMetadataTitle(report.getTitle());
                pdfExporter.setMetadataAuthor("laboratorio");
                outstr = response.getOutputStream();

                exporter.setConfiguration(pdfExporter);
                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outstr));
                exporter.exportReport();

            } else if (ReportTypeEnum.EXCEL.toString().equals(reportType)) {
                log.info("Generating report excel");
                response.setContentType("application/vnd.ms-excel");
                response.setHeader("Content-Disposition", "inline; filename=\"" + report.getTitle() + ".xls\"");
                outstr = response.getOutputStream();

                SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
                configuration.setOnePagePerSheet(false);
                configuration.setRemoveEmptySpaceBetweenRows(true);
                configuration.setDetectCellType(true);
                configuration.setWhitePageBackground(false);

                JRXlsxExporter xlsExporter = new JRXlsxExporter();
                xlsExporter.setConfiguration(configuration);
                xlsExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                xlsExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outstr));
                xlsExporter.exportReport();
            } else {
                log.error("The Type {} of Report doesn't exists", reportType);
                throw new ApiRequestException("The Type { " + reportType + " } of Report doesn't exists");
            }

        } catch (Exception e) {

            try {
                if (outstr != null) {
                    outstr.flush();
                    outstr.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            log.error("An error has ocurred generating the report {}", e.getMessage());
            throw new ApiRequestException(e.getMessage());
        }
    }
}
