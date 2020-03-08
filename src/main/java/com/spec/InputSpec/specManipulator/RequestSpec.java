package com.spec.InputSpec.specManipulator;

import com.kulana.reportDataReader.ExcelUtils.Interfaces.ISpec;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
public class RequestSpec implements IRequestSpec {
    public Map<String, ISpec> getSpecMap() {
        return specMap;
    }

    public void setSpecMap(Map<String, ISpec> specMap) {
        this.specMap = specMap;
    }

    @Setter
    private Map<String, ISpec> specMap = null;

    public RequestSpec(Map<String, ISpec> specMap) {
        setSpecMap(specMap);
    }
}

