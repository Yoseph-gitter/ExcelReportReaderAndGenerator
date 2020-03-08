package com.kulana.reportDataReader.ReportUtils.UiReport;

import com.kulana.reportDataReader.ExcelUtils.ExcelHelper;
import com.kulana.reportDataReader.ExcelUtils.Interfaces.IExcelRow;
import com.kulana.reportDataReader.ReportUtils.ReportRun;
import com.kulana.reportDataReader.ReportUtils.UiReportCreator;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ExcelUiReader {
    private static final String formatter = "\n%-55s%-10s%-15s%-15s%-15s";

    @Setter
    @Getter
    private static int FILTERING_COLUMN = 3;
    private static final String OUTPUT_UI_FILE = "\\src\\main\\resources\\OutputReports\\Ui\\";

    /**
     * Printing the header only once
     */
    static {
        System.out.format("\n%-105s", "********************************************************************************");
    }

    public static void generateUiReportFromList(String fileName, String outPutSpecFileName, String outPutReportFileNameUi) {
        Map<String, UiReportCreator> result = null;
        int count = 0;
        try {
            System.out.println("Spec file in ExcelUiReader ...." + outPutSpecFileName);
            result = ExcelUiReader.reportGenerator(fileName, outPutSpecFileName, FILTERING_COLUMN);
        } catch (Exception e) {
            System.out.println("Report is NOT generated !");
        }
        if (count++ == 0) {
            System.out.format(formatter, "ReportType", "Minimum", "Maximum", "No of Requests", "Number of Failures");
        }
        List<UiReportCreator> reports = new LinkedList<>();
        for (String key : result.keySet()) {
            UiReportCreator report = result.get(key);
            report.setRequestType(key);
            report.initializeReportCreator();
            reports.add(report);
            System.out.format(formatter, key, report.getMin(), report.getMax(), report.getNumberOfRequests(), report.getNumberOfFailures());
            System.out.println();
        }
        //create final report
        UiReportRenderer.generateUiReport(reports, outPutReportFileNameUi);
    }

    public static void generateUiReportFromFiles(String fileName, String outPutSpecFileName, String outPutReportFileNameUi) {
        File[] files = getFilesInDir(fileName);
        int count = 0;
        List<UiReportCreator> reports = new LinkedList<>();
        for (File file : files) {
            Map<String, UiReportCreator> result = ExcelUiReader.reportGenerator(file.getAbsolutePath(), outPutSpecFileName, FILTERING_COLUMN);
            if (count++ == 0) {
                System.out.format(formatter, "ReportType", "Minimum", "Maximum", "No of Requests", "Number of Failures");
            }
            for (String key : result.keySet()) {
                UiReportCreator report = result.get(key);
                report.setRequestType(key);
                report.initializeReportCreator();
                reports.add(report);
                System.out.format(formatter, key, report.getMin(), report.getMax(), report.getNumberOfRequests(), report.getNumberOfFailures());
                System.out.println();
            }
        }
        //create final report
        UiReportRenderer.generateUiReport(reports, outPutReportFileNameUi);
    }

    public static Map<String, UiReportCreator> reportGenerator(String fileName, String specFileName, int filteringColumn) {
        Map<String, List<IExcelRow>> filteredOutPut = null;
        try {
            filteredOutPut = ExcelHelper.readAndFilter(fileName, specFileName, filteringColumn);
        } catch (Exception e) {

        }
        return aggregator(filterByPassFail(filteredOutPut));
    }


    public static Map<String, List[]> filterByPassFail(Map<String, List<IExcelRow>> mapOfReportRuns) {
        Map<String, List[]> filteredBy = new HashMap<>();

        for (String key : mapOfReportRuns.keySet()) {
            List[] passFailArray = new List[2];
            List<IExcelRow> passList = new LinkedList<>();
            List<IExcelRow> failList = new LinkedList<>();
            for (IExcelRow reportRun : mapOfReportRuns.get(key)) {
                //filter pass or fail
                if (((ReportRun) reportRun).getStatus().contains("Completed")) {
                    passList.add(reportRun);
                } else if (((ReportRun) reportRun).getStatus().contains("Failed")) {
                    failList.add(reportRun);
                }
            }
            passFailArray[0] = passList;
            passFailArray[1] = failList;
            filteredBy.put(key, passFailArray);
        }
        return filteredBy;
    }

    public static Map<String, UiReportCreator> aggregator(Map<String, List[]> filteredMap) {
        Map<String, UiReportCreator> agregatedReportRun = new HashMap<>();
        for (String key : filteredMap.keySet()) {
            List<ReportRun> passed = filteredMap.get(key)[0];
            List<ReportRun> failed = filteredMap.get(key)[1];
            UiReportCreator uiReportCreator = new UiReportCreator(passed);
            uiReportCreator.setNumberOfFailures(failed.size());
            agregatedReportRun.put(key, uiReportCreator);
        }
        return agregatedReportRun;
    }

    public static File[] getFilesInDir(String fileName) {
        File givenFile = new File(fileName);
        if (!givenFile.isDirectory()) {
            File parentDirectory = new File(givenFile.getParent());
            return parentDirectory.listFiles();
        }
        File[] files = givenFile.listFiles();
        return files;
    }
}
