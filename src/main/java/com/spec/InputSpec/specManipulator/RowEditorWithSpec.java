package com.spec.InputSpec.specManipulator;

import com.kulana.reportDataReader.ExcelUtils.Interfaces.IExcelRow;
import com.kulana.reportDataReader.ExcelUtils.Interfaces.ISpec;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Row;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * A class that is responsible to creating Spec generation from a specification file and is a deligate to RowInitializer
 * class that does init a row based on the provided spec.
 */
public class RowEditorWithSpec {
    @Setter
    @Getter
    private String specFileName;
    @Setter
    @Getter
    private SpecGenerator specGenerator;

    private static RowEditorWithSpec rowEditorWithSpec;
    private static Map<String, RowEditorWithSpec> mapRowEditorWithSpec = new HashMap<>();

    public RowInitializer getRowInitializer() {
        return rowInitializer;
    }

    private RowInitializer rowInitializer;

    private RowEditorWithSpec(String specFileName) {
        this.specFileName = specFileName;
        IRequestSpec requestSpec = new RequestSpec(null);
        Class<IRequestSpec> classOfT = (Class<IRequestSpec>) (requestSpec.getClass());
        this.specGenerator = SpecGenerator.getInstance(this.specFileName, classOfT);
        IRequestSpec requestSpec1 = specGenerator.serializeJsonTo();
        System.out.println("Request spec generated : " + requestSpec);
        this.rowInitializer = new RowInitializer(requestSpec1);
    }

    /**
     * A map based singleton class that creates one instance for each spec file we use to Edit a row object
     *
     * @param readSpecFileName
     * @return
     */
    public static RowEditorWithSpec getInstance(String readSpecFileName) {
        if (!mapRowEditorWithSpec.containsKey(readSpecFileName)) {
            rowEditorWithSpec = new RowEditorWithSpec(readSpecFileName);
            //keep the instantiated object for later use in hash
            mapRowEditorWithSpec.put(readSpecFileName, rowEditorWithSpec);
            return rowEditorWithSpec;
        }
        return mapRowEditorWithSpec.get(readSpecFileName);
    }

    public IExcelRow generateRowModel(Row row) {
        IExcelRow reportRun = rowEditorWithSpec.getRowInitializer().initialize(row, this.specFileName);
        return reportRun;
    }

    public List<String> getReportTypes() {
        List<String> reportTypes = new LinkedList<>();
        Map<String, ISpec> specMap = rowInitializer.getRequestSpec().getSpecMap();
        for (String key : specMap.keySet()) {
            String[] catigories = specMap.get(key).getCategories();
            if (catigories.length != 0) {
                for (String catigory : catigories) {
                    reportTypes.add(catigory);
                }
            }
        }
        return reportTypes;
    }
}
