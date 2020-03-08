package com.spec.InputSpec.specManipulator;

import com.kulana.reportDataReader.ExcelUtils.Interfaces.ISpec;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Spec implements ISpec {
    @Setter
    private String[] categories; //{"Import Hire Employee", "Import Contract Contingent Worker"} ;
    @Setter
    private String dataType;
    @Setter
    private int index;
    @Setter
    private String name;
}
