package com.spec.InputSpec.specManipulator;

import com.kulana.reportDataReader.ExcelUtils.Interfaces.ISpec;

import java.util.Map;

public interface IRequestSpec {
    Map<String, ISpec> getSpecMap();
}
