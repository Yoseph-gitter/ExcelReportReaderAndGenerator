package com.kulana.reportDataReader.ReportUtils.UiReport;

import com.kulana.reportDataReader.ReportUtils.UiReportCreator;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class UiReportRenderer {

    public static void generateUiReport(List<UiReportCreator> uiReportCreators, String toOutPutFileName) {
        String absolutePathFileName = toOutPutFileName;
        Workbook outPutWorkbook = new XSSFWorkbook();
        //
        CellStyle style1 = outPutWorkbook.createCellStyle();
        style1.setFillBackgroundColor(IndexedColors.GREEN.getIndex());
        style1.setFillPattern(FillPatternType.ALT_BARS.LEAST_DOTS);
        style1.setWrapText(true);
        style1.setAlignment(HorizontalAlignment.CENTER);
        // style1.setFont(2,5,outPutWorkbook.createFont().setUnderline(HSSFFont.U_DOUBLE;

        Font hlink_font = outPutWorkbook.createFont();
        hlink_font.setUnderline(Font.U_SINGLE);
        hlink_font.setColor(IndexedColors.BLUE.getIndex());
        // hlink_font.setFontName(hlink_font);
        XSSFSheet sheet = (XSSFSheet) outPutWorkbook.createSheet("Ui_Report_Run");
        int columnCount = 0;
        int rowCount = 0;

        Row row = sheet.createRow(rowCount++);
        // //set Headers
        if (row.getRowNum() == 0) {
            Cell firstCell = row.createCell(columnCount);
            firstCell.setCellValue(reportHeader(columnCount++));
            firstCell.setCellStyle(style1);
            //update cell minimum
            firstCell = row.createCell(columnCount);
            firstCell.setCellValue(reportHeader(columnCount++));
            firstCell.setCellStyle(style1);

            //update cell max
            firstCell = row.createCell(columnCount);
            firstCell.setCellValue(reportHeader(columnCount++));
            firstCell.setCellStyle(style1);

            //update cell average
            firstCell = row.createCell(columnCount);
            firstCell.setCellValue(reportHeader(columnCount++));
            firstCell.setCellStyle(style1);

            //update cell number of requests
            firstCell = row.createCell(columnCount);
            firstCell.setCellValue(reportHeader(columnCount++));
            firstCell.setCellStyle(style1);

            //update cell number of Failures
            firstCell = row.createCell(columnCount);
            firstCell.setCellValue(reportHeader(columnCount++));
            firstCell.setCellStyle(style1);
            columnCount = 0;
        }
        //Row row = sheet.createRow(rowCount++);
        for (UiReportCreator uiReportCreator : uiReportCreators) {
            //create each row
            row = sheet.createRow(rowCount++);
            // style1.setDataFormat(creationHelper.createDataFormat().getFormat("HH:mm:ss"));

            Cell firstCell = row.createCell(columnCount++);
            firstCell.setCellValue(uiReportCreator.getRequestType());
            //update cell minimum
            firstCell = row.createCell(columnCount++);
            firstCell.getCellStyle();
            //firstCell.setCellStyle(style1);
            System.out.print("Min Is " + uiReportCreator.getMin());
            firstCell.setCellValue(formatTime(uiReportCreator.getMin()));
            //update cell max
            firstCell = row.createCell(columnCount++);
            firstCell.setCellValue(formatTime(uiReportCreator.getMax()));
            System.out.print("Min Is " + uiReportCreator.getMin());

            //update cell average
            firstCell = row.createCell(columnCount++);
            long averageTime = (long) Math.ceil(uiReportCreator.getAverage());
            firstCell.setCellValue(formatTime(averageTime));
            //update cell number of requests
            firstCell = row.createCell(columnCount++);
            firstCell.setCellValue(uiReportCreator.getNumberOfRequests());

            //update cell number of Failures
            firstCell = row.createCell(columnCount++);
            firstCell.setCellValue(uiReportCreator.getNumberOfFailures());

            columnCount = 0;
        }
        try {
            FileOutputStream outputStream = new FileOutputStream(new File(absolutePathFileName));
            outPutWorkbook.write(outputStream);
            outputStream.close();
            System.out.print("\nDone Writing Report to XSLS file ");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String reportHeader(int index) {
        String[] headers = {
                "Report Name",
                "Min Execution Time	in (HH:MM:SS)",
                "Max Execution Time in (HH:MM:SS)",
                "Avg. Execution Time in (HH:MM:SS)",
                "Execution Count Passed",
                "No. of Failed Reports",
                "Goal"
        };
        if (index < headers.length) {
            return headers[index];
        }
        return "No Header";
    }

    private static String formatTime(long timeValue) {
        long timeInSecond = Long.valueOf(timeValue);
        String hms = String.format("%02d:%02d:%02d",
                TimeUnit.SECONDS.toHours(timeInSecond),
                TimeUnit.SECONDS.toMinutes(timeInSecond) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.SECONDS.toSeconds(timeInSecond) % TimeUnit.MINUTES.toSeconds(1));
        System.out.println(hms);
        return hms;
    }

}
