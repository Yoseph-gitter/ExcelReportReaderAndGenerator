package com.kulana.reportDataReader.ExcelUtils;

import com.kulana.reportDataReader.DateUtils.DateMassage;
import com.kulana.reportDataReader.ExcelUtils.Interfaces.IExcelHelper;
import com.kulana.reportDataReader.ExcelUtils.Interfaces.IExcelRow;
import com.kulana.reportDataReader.ExcelUtils.Interfaces.ISpec;
import com.kulana.reportDataReader.ReportUtils.ReportRun;
import com.spec.InputSpec.specManipulator.FindMethodByReflection;
import com.spec.InputSpec.specManipulator.RowEditorWithSpec;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @Author :Yoseph hailu
 */
public class ExcelHelper implements IExcelHelper {
    private static Map<String, List<IExcelRow>> runReportsTypesMap = new HashMap<>();

    public static Map<String, List<IExcelRow>> readAndFilter(String fileName, String specFileName, int FILTERING_COLUMN) {
        Map<String, List<IExcelRow>> filteredMap = null;
        try {
            ExcelHelper helperVersion = new ExcelHelper();
            List<IExcelRow> reportRuns = helperVersion.readExcelFile(fileName, specFileName);
            filteredMap = helperVersion.filterByColumn(reportRuns, specFileName, FILTERING_COLUMN);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        } finally {
            return filteredMap;
        }
    }

    @Override
    public synchronized List<IExcelRow> readExcelFile(String fileName, String specFileName) {
        //get row editor with spec class
        RowEditorWithSpec rowEditorWithSpec = RowEditorWithSpec.getInstance(specFileName);
        List<IExcelRow> processMonitorList = new LinkedList<>();
        try {
            System.out.println("File Name used to read is : " + fileName);
            File file = new File(fileName);
            Workbook inputWorkbook = WorkbookFactory.create(file);
            inputWorkbook.getActiveSheetIndex();
            Sheet sheet = inputWorkbook.getSheetAt(0);
            System.out.println("Grabbing data from Sheet  : " + sheet.getSheetName());
            Iterator<Row> rowIterator = sheet.rowIterator();
            //Getting and printing the title
            Row row = rowIterator.next();
            IExcelRow reportRun = rowEditorWithSpec.generateRowModel(row);
            System.out.println("Title " + reportRun.toString());
           // processMonitorList.add(reportRun);

            while (rowIterator.hasNext()) {
                row = rowIterator.next();
                reportRun = rowEditorWithSpec.generateRowModel(row);
                //if reportRun is created push it otherwise do nothing
                if (reportRun != null) {
                    processMonitorList.add(reportRun);
                }
            }

        } catch (IOException | InvalidFormatException | IllegalStateException e) {
            System.out.println("Error happened when reading input Excel File");
            e.printStackTrace();
        }
        return processMonitorList;
    }

    /**
     * This method filters a list of Excel row with a given specification file and the column index to filter that list with
     * we use reflection to get the value at a given column based on the spec.
     *
     * @param rowsToFilter
     * @param specFileName
     * @param columnIndex
     * @return
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @Override
    public Map<String, List<IExcelRow>> filterByColumn(List<IExcelRow> rowsToFilter, String specFileName, int columnIndex) {
        //remove the first row as its a title before doing any further processing
       // rowsToFilter.remove(0);
        //once we get the method name we can call it using reflection
        for (IExcelRow reportRun : rowsToFilter) {
            List<IExcelRow> singleReport = new LinkedList<>();
            //invoke method on a given column using reflection
            String reflectedValue = FindMethodByReflection.invokeGetterMethod(reportRun, specFileName, columnIndex);
            if (reflectedValue != "" && runReportsTypesMap.containsKey(reflectedValue)) {
                runReportsTypesMap.get(reflectedValue).add(reportRun);
            } else {
                singleReport = new LinkedList<>();
                singleReport.add(reportRun);
                runReportsTypesMap.put(reflectedValue, singleReport);
            }
        }
        return runReportsTypesMap;
    }

    public void writeToExcelFile(List<IExcelRow> reportRunForARequestType, String outputFileName, RowEditorWithSpec rowEditorWithSpec) {
        try {
            Workbook outPutWorkbook = writeToWorkbook(reportRunForARequestType, rowEditorWithSpec);
            FileOutputStream outputStream = new FileOutputStream(new File(outputFileName));
            outPutWorkbook.write(outputStream);
            outputStream.close();
            System.out.print("\n Done Writing to XSLS file ");
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeToExcelFile(List<? extends IExcelRow> listOfReportRuns, String atOutputFileName, String withSpecFileName) {
        //get row editor with spec class
        RowEditorWithSpec rowEditorWithSpec = RowEditorWithSpec.getInstance(withSpecFileName);
        //write to output file per the specified spec
        writeToExcelFile((List<IExcelRow>) listOfReportRuns, atOutputFileName, rowEditorWithSpec);
    }

    @Override
    public void generateExcelReport() {

    }

    /**
     * Creates a workbook object from a list of rows and rowEditorSpec object
     *
     * @param reportRunForARequestType
     * @param rowEditorWithSpec
     * @return
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static Workbook writeToWorkbook(List<IExcelRow> reportRunForARequestType, RowEditorWithSpec rowEditorWithSpec)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Workbook outPutWorkbook = new XSSFWorkbook();
        XSSFSheet sheet = (XSSFSheet) outPutWorkbook.createSheet("Daily_Perf_Run");
        Map<String, ISpec> specMap = rowEditorWithSpec.getRowInitializer().getRequestSpec().getSpecMap();
        int rowCount = 0;
        for (IExcelRow reportRun : reportRunForARequestType) {
            //create each row
            Row row = sheet.createRow(rowCount++);
            //cell for starting date and time
            for (String name : specMap.keySet()) {
                //synthesizing the method names
                String methodName = "get" + name;
                Method InstanceMethod = ReportRun.class.getMethod(methodName);
                Cell firstCell = row.createCell(specMap.get(name).getIndex());
                String reflectedValue = (String) InstanceMethod.invoke(reportRun);
                firstCell.setCellValue(reflectedValue);
            }
        }
        return outPutWorkbook;
    }

    public List<IExcelRow> readExcelFilteredBetweenDate(String fileName, String specFileName, String startDateTimeText, String endDateTimeText, String pastDate) {
        List<IExcelRow> filteredRow = new LinkedList<>();
        List<IExcelRow> reportRuns = readExcelFile(fileName, specFileName);
        //remove the first row as its holding title only info
        reportRuns.remove(0);
        String currentRowDateText;
        for (IExcelRow excelRow : reportRuns) {
            currentRowDateText = FindMethodByReflection.invokeGetterMethod(excelRow, specFileName, 0);
            if (isRowInsertEligible(currentRowDateText, startDateTimeText, endDateTimeText, pastDate)) {
                filteredRow.add(excelRow);
            }
        }
        return filteredRow;
    }

    //a flag that checks if a given row is insertable or not based on the time column
    public boolean isRowInsertEligible(String currentRowDateText, String startDateTimeText, String endDateTimeText, String pastDate) {
        int pastDates = Integer.valueOf(pastDate);
        //"Thu Oct 17 13:02:00 PDT 2019";
        LocalDateTime reportingStartTime = DateMassage.convertToDateTime(startDateTimeText).minusDays(pastDates);
        LocalDateTime reportingEndTime = DateMassage.convertToDateTime(endDateTimeText).minusDays(pastDates);
        LocalDateTime currentRowDate = DateMassage.convertTextToLocalDateTime(currentRowDateText);

        boolean isAfter = DateMassage.compareDate(currentRowDate, reportingStartTime, false);
        boolean isBefore = DateMassage.compareDate(currentRowDate, reportingEndTime, true);

        return !isAfter && !isBefore;
    }

}
