package com.kulana.reportDataReader;


import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;


/**
 * This class is written based on the follwoing api which is open source
 * https://howtodoinjava.com/library/readingwriting-excel-files-in-java-poi-tutorial/
 */
@Log
public class PerfRunExcelReportCrncher implements ExcelReportDataReader {
    private static final String fileName = "C:\\Users\\yoseph\\IdeaProjects\\compeopletechperfRunRetriever\\src\\main\\resources\\InputReports\\Process_Monitor_50And75hvh.xlsx";
    private static final String outputDirectoryName = "C:\\Users\\yoseph\\IdeaProjects\\compeopletechperfRunRetriever\\src\\main\\resources\\OutputReports\\";
    private static final String extension = ".xlsx";
    private List<ReportRun> processMonitorList = new LinkedList<>();
    private static final int MAX_TIME_FOR_DAILY = 24;
    private static final int MIN_TIME_FOR_DAILY = 10000;
    @Setter
    @Getter
    private String expectedBatchSize;
    @Setter
    @Getter
    private static String pastDate;
    private static Map<RequestType, List<ReportRun>> runReportsTypesMap = new HashMap<>();
    @Setter
    @Getter
    private String reportStartTime;
    @Setter
    @Getter
    private String reportEndTime;
    private int timeDifference;

    static {
        for (RequestType type : RequestType.values()) {
            List<ReportRun> singleReport = new LinkedList<>();
            runReportsTypesMap.put(type, singleReport);
        }
    }

    public static void main(String[] args) {
        PerfRunExcelReportCrncher reporter = new PerfRunExcelReportCrncher();
        reporter.setReportingTime(args);
        System.out.println("Report will be generated after time   :" + reporter.getReportStartTime());

        reporter.setReportStartTime(reporter.getReportStartTime());
        reporter.readExcelFile(fileName);
        reporter.runGenerateReportWith(reporter);
    }

    private void runGenerateReportWith(PerfRunExcelReportCrncher reporter) {
        reporter.extractDataForRequestType();
        reporter.generateReport(RequestType.IMPORT_HIRE_EMPLOYEE);
        // reporter.generateReport(RequestType.IMPORT_CONTRACT_CONTINGENT_WORKER);
        // produce the report with all metrics
        ReportCreator reportCreator = new ReportCreator(runReportsTypesMap.get(RequestType.IMPORT_HIRE_EMPLOYEE), 50, getExpectedBatchSize());
        reportCreator.formattedOutPut();
        //reportCreator = new ReportCreator(runReportsTypesMap.get(RequestType.IMPORT_CONTRACT_CONTINGENT_WORKER), 50, getExpectedBatchSize());
        // reportCreator.formattedOutPut();
    }

    private void setReportingTime(String[] args) {
        this.setReportStartTime(args[0]);
        this.setReportEndTime(args[1]);
        this.setExpectedBatchSize(args[2]);
        this.setPastDate(args[3]);
        this.timeDifference = this.getTimeDifference(this.reportStartTime, this.reportEndTime);
    }

    @Override
    public void readExcelFile(String fileName) {
        readExcelFile(processMonitorList, fileName);
    }


    public void readExcelFile(List<ReportRun> processMonitorList, String fileName) {
        try {
            File file = new File(fileName);
            Workbook inputWorkbook = WorkbookFactory.create(file);
            inputWorkbook.getActiveSheetIndex();
            Sheet sheet = inputWorkbook.getSheetAt(0);
            System.out.println("Grabbing data from Sheet  : " + sheet.getSheetName());
            Iterator<Row> rowIterator = sheet.rowIterator();
            rowIterator.next();
            Row row = rowIterator.next();
            String localFormat = "\n%-48s%-48s%-30s";
            ReportRun reportRun = generateReportRunForCell(row);
            //printing the title
            System.out.format(localFormat, reportRun.getStartedDateAndTime(), reportRun.getRequestType(), reportRun.getTotalProcessingTime());
            System.out.println();

            processMonitorList.add(reportRun);

            while (rowIterator.hasNext()) {
                row = rowIterator.next();
                reportRun = generateReportRunForCell(row);
                String key = "";
                //if reportRun is created push it otherwise do nothing
                if (reportRun != null) {
                    processMonitorList.add(reportRun);
                    key = reportRun.getStartedDateAndTime();
                    System.out.format(localFormat, key, String.valueOf(reportRun.getTotalProcessingTime()), reportRun.getRequestType());
                }
            }

        } catch (IOException | InvalidFormatException | IllegalStateException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void generateReport(RequestType requestType) {
        generateReport(requestType, false);
    }

    @Override
    public void extractDataForRequestType() {
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
                    List<ReportRun> listOfReports = runReportsTypesMap.get(requestType);
                    listOfReports.add(run);
                } else {
                    requestType = RequestType.IMPORT_HIRE_EMPLOYEE;
                    List<ReportRun> listOfReports = runReportsTypesMap.get(requestType);
                    listOfReports.add(run);
                }
            } else if (run.getRequestType().equals(RequestType.IMPORT_CONTRACT_CONTINGENT_WORKER.getRequestType())) {
                if (isDaily(run)) {
                    requestType = RequestType.IMPORT_CONTRACT_CONTINGENT_WORKER_DAILY;
                    List<ReportRun> listOfReports = runReportsTypesMap.get(requestType);
                    listOfReports.add(run);
                } else {
                    requestType = RequestType.IMPORT_CONTRACT_CONTINGENT_WORKER;
                    List<ReportRun> listOfReports = runReportsTypesMap.get(requestType);
                    listOfReports.add(run);
                }
            }
            run.setRequestTypeEnum(requestType);
        }

    }

//    public ReportRun generateReportRunForCell(Row row) {
//        ReportRun reportRun = new ReportRun();
//        if (row.getRowNum() <= 1) {
//            reportRun = SpeacGenerator.initializeRow(row);
//        } else {
//            //if insert elligible insert else not
//            if (isRowInsertElligible(row, getPastDate())) {
//                reportRun = SpeacGenerator.initializeRow(row);
//            } else {
//                return null;
//            }
//        }
//        return reportRun;
//    }

    private boolean isRowInsertElligible(Row row, String pastDate) {
        String startDateTimeText = this.getReportStartTime();
        String endDateTimeText = this.getReportEndTime();
        int pastDates = Integer.valueOf(pastDate);
        //"Thu Oct 17 13:02:00 PDT 2019";
        LocalDateTime reportingStartTime = DateMassager.convertToDateTime(startDateTimeText).minusDays(pastDates);
        LocalDateTime reportingEndTime = DateMassager.convertToDateTime(endDateTimeText).minusDays(pastDates);

        String currentRowDateText = row.getCell(0).getDateCellValue().toString();
        LocalDateTime currentRowDate = DateMassager.convertTextToLocalDateTime(currentRowDateText);

        boolean isAfter = DateMassager.compareDate(currentRowDate, reportingStartTime, false);
        boolean isBefore = DateMassager.compareDate(currentRowDate, reportingEndTime, true);

        return !isAfter && !isBefore;
    }

    private ReportRun generateReportRunForCell(Row row) {
        ReportRun reportRun = new ReportRun();
        if (row.getRowNum() <= 1) {
            //treat it as a string since its a title
            reportRun.setStartedDateAndTime(row.getCell(0).getStringCellValue());
            reportRun.setRequestType(row.getCell(3).getStringCellValue());
            reportRun.setTotalProcessingTime(row.getCell(5).getStringCellValue());
        } else {
            //if insert elligible insert else not
            if (isRowInsertElligible(row, getPastDate())) {
                reportRun.setStartedDateAndTime(row.getCell(0).getDateCellValue().toString());
                reportRun.setRequestType(row.getCell(3).getStringCellValue());
                String hashValue = row.getCell(5).getRichStringCellValue().getString();
                long responseTimeInSeconds = convertToTimeToSeconds(hashValue);
                reportRun.setTotalProcessingTime(String.valueOf(responseTimeInSeconds));
            } else {
                return null;
            }
        }
        return reportRun;
    }

    private int convertToTimeToSeconds(String timeAsText) {
        String[] hms = timeAsText.split(":");
        int hr = Integer.valueOf(hms.length >= 1 ? hms[0] : "0");
        int minute = Integer.valueOf(hms.length >= 2 ? hms[1] : "0");
        int seconds = Integer.valueOf(hms.length >= 3 ? hms[2] : "0");


        minute += hr * 60;
        seconds += minute * 60;
        return seconds;
    }

    private int getTimeDifference(String reportStartTime, String reportEndTime) {
        LocalDateTime startTime = DateMassager.createCustomDateTimeFromHrAndMin(reportStartTime);
        LocalDateTime endTime = DateMassager.createCustomDateTimeFromHrAndMin(reportEndTime);
        return convertToTimeToSeconds(this.reportEndTime) - convertToTimeToSeconds(this.reportStartTime);
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

    private List<ReportRun> getReportRunForRequestType(RequestType requestType) {
        return runReportsTypesMap.get(requestType);
    }

    public void generateReport(RequestType requestType, boolean withDaily) {
        String typePath = getType(requestType, withDaily).toString();
        String outputFileName = outputDirectoryName + typePath + extension;
        RequestType type = getType(requestType, withDaily);
        List<ReportRun> reportRunForARequestType = getReportRunForRequestType(type);
        ExcelHelper.gnerateReport(reportRunForARequestType, outputFileName);
    }

    private boolean isDaily(ReportRun reportRun) {
        String numberOfSeconds = reportRun.getTotalProcessingTime();
        int numSeconds = Integer.valueOf(numberOfSeconds);
        return numSeconds < MAX_TIME_FOR_DAILY || numSeconds > MIN_TIME_FOR_DAILY;
    }
}
