package com.kulana.reportDataReader.EnumUtils;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum RequestType {
    IMPORT_CONTRACT_CONTINGENT_WORKER("Import Contract Contingent Worker"),
    IMPORT_HIRE_EMPLOYEE("Import Hire Employee"),
    IMPORT_HIRE_EMPLOYEE_DAILY("Import Hire Employee"),
    IMPORT_CONTRACT_CONTINGENT_WORKER_DAILY("Import Contract Contingent Worker")
    ;
    @Setter @Getter
    String requestType ;
    RequestType(String s) {
        this.requestType = s;
        this.setName(this);
    }

    String name ;

    public void setName(RequestType requestType){
        this.name = requestType.name();
    }

}
