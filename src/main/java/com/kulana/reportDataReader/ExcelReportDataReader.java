package com.kulana.reportDataReader;

/**
 * Given an Microsoft Excel file this interface is going to provide the way to extract and generate
 * different kind of report files in Excel format
 */
public interface ExcelReportDataReader {
    void readExcelFile(String fileName);
    void extractDataForRequestType();
    void generateReport(RequestType requestType);
}
