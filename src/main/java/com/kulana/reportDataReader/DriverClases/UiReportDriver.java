package com.kulana.reportDataReader.DriverClases;

import com.kulana.reportDataReader.EnumUtils.FileSpecAssociate;
import com.kulana.reportDataReader.ReportUtils.UiReport.ExcelUiReader;

public class UiReportDriver {
    public static void main(String[] args) {
        UiReportDriver.generateUiReport();
        // UiReportDriver.generateApiReport();
        //UiReportDriver.generateIntegrationsReport();
    }

    public static void generateUiReport() {
        ExcelUiReader.generateUiReportFromList(
                FileSpecAssociate.UI.getSourceFile(),
                FileSpecAssociate.UI.getAssociatedSpecFile(),
                FileSpecAssociate.UI.getResultDestinationFile());
    }

    public static void generateApiReport() {
        ExcelUiReader.generateUiReportFromList(
                FileSpecAssociate.API.getSourceFile(),
                FileSpecAssociate.API.getAssociatedSpecFile(),
                FileSpecAssociate.API.getResultDestinationFile());

    }

    public static void generateIntegrationsReport() {
        ExcelUiReader.generateUiReportFromList(
                FileSpecAssociate.INTEGRATION.getSourceFile(),
                FileSpecAssociate.INTEGRATION.getAssociatedSpecFile(),
                FileSpecAssociate.INTEGRATION.getResultDestinationFile());

    }
}
