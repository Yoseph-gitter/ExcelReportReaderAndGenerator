package com.kulana.reportDataReader.ExcelUtils.Interfaces;

import com.spec.InputSpec.specManipulator.RowEditorWithSpec;

import java.util.List;
import java.util.Map;

/**
 * Given an Microsoft Excel file this interface is going to provide the way to extract and generate
 * different kind of report files in Excel format
 */
public interface IExcelHelper {
    List<? extends IExcelRow> readExcelFile(String fileName, String specFileName);

    Map<String, List<IExcelRow>> filterByColumn(List<IExcelRow> rowsToFilter, String specFileName, int columnIndex);

    void writeToExcelFile(List<? extends IExcelRow> reportRuns, String fileName, String specFileName);

    void generateExcelReport();
}
