package com.spec.InputSpec.specManipulator;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.kulana.reportDataReader.ExcelUtils.Interfaces.ISpec;
import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class SpecGenerator {
    private static Gson json = new Gson();
    @Setter
    @Getter
    private String specFilePath = "";
    private Class<IRequestSpec> iRequestSpecClass;
    private static Map<String, SpecGenerator> mapSpecGenerator = new HashMap<>();

    public static SpecGenerator getInstance(String specFileNamePath, Class<IRequestSpec> iRequestSpecClass) {
        if (!mapSpecGenerator.containsKey(specFileNamePath)) {
            SpecGenerator specGenerator = new SpecGenerator(specFileNamePath, iRequestSpecClass);
            mapSpecGenerator.put(specFileNamePath, specGenerator);
            return specGenerator;
        }
        return mapSpecGenerator.get(specFileNamePath);
    }

    private SpecGenerator(String specFileNamePath, Class<IRequestSpec> iRequestSpecClass) {
        this.specFilePath = specFileNamePath;
        this.iRequestSpecClass = iRequestSpecClass;
    }

    public synchronized RequestSpec serializeJsonTo() {
        Map<String, ISpec> specMap = readJsonToObject(readSpecFromJsonFile(this.specFilePath));
        for (String key : specMap.keySet()) {
            //we have to create a json object from the sub-json
            JsonObject jsonObject = json.toJsonTree(specMap.get(key)).getAsJsonObject();
            Spec result = json.fromJson(jsonObject.toString(), Spec.class);
            specMap.put(key, result);
        }
        RequestSpec requestSpec = new RequestSpec(specMap);
        return requestSpec;
    }

    public Map<String, ISpec> readJsonToObject(StringBuffer jsonObjectSpec) throws JsonSyntaxException {
        Map<String, ISpec> map = new HashMap();
        map = (Map<String, ISpec>) json.fromJson(jsonObjectSpec.toString(), map.getClass());
        return map;
    }

    public StringBuffer readSpecFromJsonFile(String specFileNamePath) {
        StringBuffer jsonObjectSpec = new StringBuffer();
        BufferedReader bufferedReader = null;
        String line = "";
        System.out.println("Kanonical file path is : " + specFileNamePath);
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(specFileNamePath))));
            while ((line = bufferedReader.readLine()) != null) {
                jsonObjectSpec.append(line);
            }
            System.out.println(jsonObjectSpec.toString());
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return jsonObjectSpec;
        }
    }
}

