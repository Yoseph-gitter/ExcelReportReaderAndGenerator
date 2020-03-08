package com.spec.InputSpec.specManipulator;

import com.kulana.reportDataReader.DateUtils.DateMassage;
import com.kulana.reportDataReader.ExcelUtils.Interfaces.ISpec;
import com.kulana.reportDataReader.ReportUtils.ReportRun;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Row;

import java.util.Date;

public class RowInitializer {
    @Getter
    @Setter
    private IRequestSpec requestSpec;


    public RowInitializer(IRequestSpec requestSpec) {
        this.requestSpec = requestSpec;
    }

    public ReportRun initialize(Row row, String specFileName) {
        ReportRun reportRun = new ReportRun();
        String columnValue;
        Date dateValue;

        for (ISpec spec : requestSpec.getSpecMap().values()) {
            if (spec.getDataType().equals("Date")) {
                if (row.getRowNum() <= 0) {
                    columnValue = row.getCell(spec.getIndex()).getStringCellValue();
                    reportRun.setStartedDateAndTime(columnValue);
                } else {
                    try {
                        dateValue = row.getCell(spec.getIndex()).getDateCellValue();
                        reportRun.setStartedDateAndTime(dateValue.toString());
                    } catch (IllegalStateException | NumberFormatException e) {
                        String timeValue = row.getCell(spec.getIndex()).getRichStringCellValue().toString();
                        System.out.println("Date is : " + timeValue);
                        int theDateValue = DateMassage.convertTimeToSeconds(String.valueOf(timeValue));
                        reportRun.setStartedDateAndTime(String.valueOf(theDateValue));
                    } finally {

                    }
                }
            } else if (spec.getDataType().equals("String")) {
                columnValue = row.getCell(spec.getIndex()).getStringCellValue();
                FindMethodByReflection.invokeSetterMethod(reportRun, specFileName, spec.getIndex(), columnValue);

            } else if (spec.getDataType().equals("Time")) {
                if (row.getRowNum() <= 0) {
                    columnValue = row.getCell(spec.getIndex()).getStringCellValue();
                    reportRun.setTotalProcessingTime(columnValue);
                } else {
                    String timeStrFormat = row.getCell(spec.getIndex()).getRichStringCellValue().getString();
                    long responseTimeInSeconds = DateMassage.convertTimeToSeconds(timeStrFormat);
                    reportRun.setTotalProcessingTime(String.valueOf(responseTimeInSeconds));
                }
            }
        }
        return reportRun;
    }

}
