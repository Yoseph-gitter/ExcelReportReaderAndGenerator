package com.kulana.reportDataReader;

import lombok.Getter;
import lombok.Setter;

public class ReportRun {
    private String startedDateAndTime ;
    private String requestType ;
    @Getter @Setter
    private RequestType requestTypeEnum;
    private String status;
    private String totalProcessingTime;
    private String errorsAndWarnings;
    private String submittedby = "ISU_Perf_Testing_lingqian" ;

    public ReportRun() {
    }

    public String getStartedDateAndTime() {
        return startedDateAndTime;
    }

    public void setStartedDateAndTime(String startedDateAndTime) {
        this.startedDateAndTime = startedDateAndTime;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotalProcessingTime() {
        return (totalProcessingTime);
    }

    public void setTotalProcessingTime(String totalProcessingTime) {
        this.totalProcessingTime = totalProcessingTime;
    }

    public String getErrorsAndWarnings() {
        return errorsAndWarnings;
    }

    public void setErrorsAndWarnings(String errorsAndWarnings) {
        this.errorsAndWarnings = errorsAndWarnings;
    }

    public String getSubmittedby() {
        return submittedby;
    }

    public void setSubmittedby(String submittedby) {
        this.submittedby = submittedby;
    }
}
