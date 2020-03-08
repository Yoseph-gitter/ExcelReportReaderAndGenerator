package com.kulana.reportDataReader;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelHelper {

    public static void gnerateReport(List<ReportRun> reportRunForARequestType, String outputFileName) {
        Workbook outPutWorkbook = new XSSFWorkbook();
        XSSFSheet sheet = (XSSFSheet) outPutWorkbook.createSheet("Daily_Perf_Run");
        int columnCount = 0;
        int rowCount = 0;
        for (ReportRun reportRun : reportRunForARequestType) {
            //creae each row
            Row row = sheet.createRow(rowCount++);
            //cell for starting date and time
            Cell firstCell = row.createCell(columnCount++);
            firstCell.setCellValue(reportRun.getStartedDateAndTime());
            //create a  cell for teport type
            Cell reportTypeCell = row.createCell(columnCount++);
            reportTypeCell.setCellValue(reportRun.getRequestType());
            //create a cell for time elapsed for a request
            Cell responseTimeCell = row.createCell(columnCount);
            responseTimeCell.setCellValue(reportRun.getTotalProcessingTime());
            columnCount = 0;
        }
        try {
            FileOutputStream outputStream = new FileOutputStream(new File(outputFileName));
            outPutWorkbook.write(outputStream);
            outputStream.close();
            System.out.print("\nDone Writing to XSLS file ");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
