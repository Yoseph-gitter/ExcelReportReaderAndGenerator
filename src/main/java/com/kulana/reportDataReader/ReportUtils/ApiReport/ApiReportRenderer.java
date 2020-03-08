package com.kulana.reportDataReader.ReportUtils.ApiReport;


import com.kulana.reportDataReader.DateUtils.DateMassage;
import com.kulana.reportDataReader.EnumUtils.RequestType;
import com.kulana.reportDataReader.ExcelUtils.ExcelHelper;
import com.kulana.reportDataReader.ExcelUtils.Interfaces.IExcelRow;
import com.kulana.reportDataReader.ReportUtils.ReportRun;
import com.spec.InputSpec.specManipulator.RowEditorWithSpec;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * This class is written based on the follwoing api which is open source
 * https://howtodoinjava.com/library/readingwriting-excel-files-in-java-poi-tutorial/
 */
@Log
public class ApiReportRenderer {
    private static final String HOME_DIR = System.getProperty("user.dir");
    private static final String fileName = HOME_DIR + "\\src\\main\\resources\\InputReports\\API\\Process_Monitor.xlsx";
    private static final String outputDirectoryName = HOME_DIR + "\\src\\main\\resources\\OutputReports\\Api\\";
    private static final String specFileName = HOME_DIR + "\\src\\main\\resources\\Specs\\spec_hireReport.json";
    private static final String extension = ".xlsx";
    private static final int MAX_TIME_FOR_DAILY = 24;
    private static final int MIN_TIME_FOR_DAILY = 10000;
    private static final int FILTERING_INDEX = 3;
    @Setter
    @Getter
    private String expectedBatchSize;
    @Setter
    @Getter
    private static String pastDate;
    private static Map<String, List<IExcelRow>> runReportsTypesMap = new HashMap<>();
    private List<ReportRun> processMonitorList = new LinkedList<>();
    @Setter
    @Getter
    private String reportStartTime;
    @Setter
    @Getter
    private String reportEndTime;
    private int timeDifference;
    private static RowEditorWithSpec rowEditorWithSpec;

    public static void main(String[] args) {
        ApiReportRenderer reporter = new ApiReportRenderer();
        reporter.setReportingTime(args);
        System.out.println("Report will be generated after time   :" + reporter.getReportStartTime());
        reporter.setReportStartTime(reporter.getReportStartTime());
        reporter.runGenerateReportWith();
    }

    private void runGenerateReportWith() {
        ExcelHelper excelHelper = new ExcelHelper();
        List<IExcelRow> reportRun = excelHelper.readExcelFilteredBetweenDate(fileName, specFileName, getReportStartTime(), getReportEndTime(), getPastDate());
        runReportsTypesMap = excelHelper.filterByColumn(reportRun, specFileName, FILTERING_INDEX);
        //write to output file
        writeToOutPutReportFile(excelHelper);
        // produce the report with all metrics
        List<IExcelRow> filteredList = generateFilteredReport(RequestType.IMPORT_HIRE_EMPLOYEE.getRequestType(), false);
        ApiHvhReportCreator reportCreator = new ApiHvhReportCreator(filteredList, 75, getExpectedBatchSize());
        reportCreator.formattedOutPut();
    }

    private void writeToOutPutReportFile(ExcelHelper excelHelper) {
        this.rowEditorWithSpec = RowEditorWithSpec.getInstance(specFileName);
        for (String requestSpec : runReportsTypesMap.keySet()) {
            String reportType = requestSpec.trim().replace("-", "_").replace(" ", "_").toUpperCase();
            System.out.println("File type to be used is : " + reportType);
            List<IExcelRow> filteredReportRun = generateFilteredReport(requestSpec, true);
            excelHelper.writeToExcelFile(filteredReportRun, outputDirectoryName + reportType + extension, specFileName);
        }
    }

    private void setReportingTime(String[] args) {
        this.setReportStartTime(args[0]);
        this.setReportEndTime(args[1]);
        this.setExpectedBatchSize(args[2]);
        this.setPastDate(args[3]);
        this.timeDifference = this.getTimeDifference(this.reportStartTime, this.reportEndTime);
    }

    private int getTimeDifference(String reportStartTime, String reportEndTime) {
        return DateMassage.convertTimeToSeconds(reportEndTime) - DateMassage.convertTimeToSeconds(reportStartTime);
    }

    public List<IExcelRow> generateFilteredReport(String typePath, boolean withDaily) {
        String typePathFileName = typePath;
        //removing space and making it a file name
        typePathFileName = typePathFileName.trim().replace("-", "_").replace(" ", "_").toUpperCase();
        String outputFileName = outputDirectoryName + typePathFileName + extension;
        System.out.println("Report written to FileName  :" + outputFileName);
        List<IExcelRow> reportRunForARequestType = runReportsTypesMap.get(typePath);
        List<IExcelRow> reportRunWithDaily = filterDaily(reportRunForARequestType, withDaily);
        return reportRunWithDaily;
    }

    private List<IExcelRow> filterDaily(List<IExcelRow> reportRunForARequestType, boolean withDaily) {
        List<IExcelRow> reportRunWithDaily = new LinkedList<>();
        for (IExcelRow reportRun : reportRunForARequestType) {
            if (!isDaily(reportRun)) {
                reportRunWithDaily.add(reportRun);
            }
        }
        return reportRunWithDaily;
    }

    private boolean isDaily(IExcelRow reportRun) {
        String numberOfSeconds = ((ReportRun) reportRun).getTotalProcessingTime();
        int numSeconds = Integer.valueOf(numberOfSeconds);
        return numSeconds < MAX_TIME_FOR_DAILY || numSeconds > MIN_TIME_FOR_DAILY;
    }

    public void extractDataForRequestType(int a) {
        //handle header separately since its types are different from the actual values stored
        ReportRun header = processMonitorList.remove(0);

        //adding the header to all the Hire types
        runReportsTypesMap.get(RequestType.IMPORT_HIRE_EMPLOYEE).add(header);
        runReportsTypesMap.get(RequestType.IMPORT_HIRE_EMPLOYEE_DAILY).add(header);
        runReportsTypesMap.get(RequestType.IMPORT_CONTRACT_CONTINGENT_WORKER).add(header);
        runReportsTypesMap.get(RequestType.IMPORT_CONTRACT_CONTINGENT_WORKER_DAILY).add(header);
        //reference for all the hire types
        RequestType requestType = RequestType.IMPORT_HIRE_EMPLOYEE;
        for (ReportRun run : processMonitorList) {
            //first check if its either Import hire or contingent worker
            //then filter with or with out daily

            if (run.getRequestType().equals(RequestType.IMPORT_HIRE_EMPLOYEE.getRequestType())) {
                if (isDaily(run)) {
                    requestType = RequestType.IMPORT_HIRE_EMPLOYEE_DAILY;
                    List<IExcelRow> listOfReports = runReportsTypesMap.get(requestType);
                    listOfReports.add(run);
                } else {
                    requestType = RequestType.IMPORT_HIRE_EMPLOYEE;
                    List<IExcelRow> listOfReports = runReportsTypesMap.get(requestType);
                    listOfReports.add(run);
                }
            } else if (run.getRequestType().equals(RequestType.IMPORT_CONTRACT_CONTINGENT_WORKER.getRequestType())) {
                if (isDaily(run)) {
                    requestType = RequestType.IMPORT_CONTRACT_CONTINGENT_WORKER_DAILY;
                    List<IExcelRow> listOfReports = runReportsTypesMap.get(requestType);
                    listOfReports.add(run);
                } else {
                    requestType = RequestType.IMPORT_CONTRACT_CONTINGENT_WORKER;
                    List<IExcelRow> listOfReports = runReportsTypesMap.get(requestType);
                    listOfReports.add(run);
                }
            }
            run.setRequestTypeEnum(requestType);
        }

    }

    private RequestType getType(RequestType requestType, Boolean withDaily) {
        RequestType type = null;
        if (requestType.equals(RequestType.IMPORT_CONTRACT_CONTINGENT_WORKER)) {
            if (withDaily) {
                type = RequestType.IMPORT_CONTRACT_CONTINGENT_WORKER_DAILY;
            } else {
                type = RequestType.IMPORT_CONTRACT_CONTINGENT_WORKER;
            }
        } else if (requestType.equals(RequestType.IMPORT_HIRE_EMPLOYEE)) {
            if (withDaily) {
                type = RequestType.IMPORT_HIRE_EMPLOYEE_DAILY;
            } else {
                type = RequestType.IMPORT_HIRE_EMPLOYEE;
            }
        }
        return type;
    }
}
