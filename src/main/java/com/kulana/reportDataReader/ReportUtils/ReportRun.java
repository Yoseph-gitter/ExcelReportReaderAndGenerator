package com.kulana.reportDataReader.ReportUtils;

import com.kulana.reportDataReader.EnumUtils.RequestType;
import com.kulana.reportDataReader.ExcelUtils.Interfaces.IExcelRow;

public class ReportRun extends IExcelRow {

    public String getStartedDateAndTime() {
        return StartedDateAndTime;
    }

    public void setStartedDateAndTime(String startedDateAndTime) {
        StartedDateAndTime = startedDateAndTime;
    }

    public String getRequestType() {
        return RequestType;
    }

    public void setRequestType(String requestType) {
        RequestType = requestType;
    }

    public com.kulana.reportDataReader.EnumUtils.RequestType getRequestTypeEnum() {
        return requestTypeEnum;
    }

    public void setRequestTypeEnum(com.kulana.reportDataReader.EnumUtils.RequestType requestTypeEnum) {
        this.requestTypeEnum = requestTypeEnum;
    }

    public String getRequest() {
        return Request;
    }

    public void setRequest(String request) {
        Request = request;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getTotalProcessingTime() {
        return totalProcessingTime;
    }

    public void setTotalProcessingTime(String totalProcessingTime) {
        this.totalProcessingTime = totalProcessingTime;
    }

    private String StartedDateAndTime;

    private String RequestType;

    private RequestType requestTypeEnum;

    private String Request;
    private String Status;

    private String totalProcessingTime;
    private String errorsAndWarnings;
    private String submittedby = "ISU_Perf_Testing_lingqian";

    public ReportRun() {
    }
}
