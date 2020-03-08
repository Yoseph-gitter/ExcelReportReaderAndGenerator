package com.kulana.reportDataReader.EnumUtils;

import lombok.Getter;
import lombok.Setter;

public enum FileSpecAssociate {

    INTEGRATION("\\src\\main\\resources\\InputReports\\Integration\\Process_Monitor.xlsx",
            "\\src\\main\\resources\\Specs\\spec_Integration_Report.json",
            "\\src\\main\\resources\\OutputReports\\Integration\\Final_reports_Zulu.xlsx"),
    UI("\\src\\main\\resources\\InputReports\\Ui\\Process_Monitor.xlsx",
            "\\src\\main\\resources\\Specs\\spec_Ui_Report.json",
            "\\src\\main\\resources\\OutputReports\\Ui\\Final_reports_Zulu.xlsx"),
    API("\\src\\main\\resources\\InputReports\\API\\Process_Monitor.xlsx",
            "\\src\\main\\resources\\Specs\\spec_hireReport.json",
            "\\src\\main\\resources\\OutputReports\\Api\\Final_reports_Zulu.xlsx");

    private final String PROJECT_HOME_DIR  = System.getProperty("user.dir") ;

    @Setter @Getter
    private String sourceFile;
    @Setter @Getter
    private String associatedSpecFile;
    @Setter @Getter
    private String resultDestinationFile;

    FileSpecAssociate(String source, String spec , String result){
        this.sourceFile = PROJECT_HOME_DIR + source;
        this.associatedSpecFile = PROJECT_HOME_DIR + spec;
        this.resultDestinationFile = PROJECT_HOME_DIR + result;
    }

    public static void main(String[] args){
        System.out.println(FileSpecAssociate.INTEGRATION.getSourceFile());
        System.out.println(FileSpecAssociate.INTEGRATION.getAssociatedSpecFile());
        System.out.println(FileSpecAssociate.INTEGRATION.getResultDestinationFile());
    }

}
