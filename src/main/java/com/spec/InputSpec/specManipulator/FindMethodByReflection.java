package com.spec.InputSpec.specManipulator;

import com.kulana.reportDataReader.ExcelUtils.Interfaces.IExcelRow;
import com.kulana.reportDataReader.ExcelUtils.Interfaces.ISpec;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class FindMethodByReflection {
    public static String getMethod(String specFileName, int columnIndex) {
        String propertyName = "";
        //get row editor object
        RowEditorWithSpec rowEditorWithSpec = RowEditorWithSpec.getInstance(specFileName);
        Map<String, ISpec> specMap = rowEditorWithSpec.getRowInitializer().getRequestSpec().getSpecMap();
        //find the columnName
        for (String specProperty : specMap.keySet()) {
            if (specMap.get(specProperty).getIndex() == columnIndex) {
                propertyName = specProperty;
                break;
            }
        }
        return propertyName;
    }

    /**
     * Given a column index a specification object , this method will provide us with getter methods for particular column
     */
    public static String invokeGetterMethod(IExcelRow reportRun, String specFileName, int columnIndex) {
        String getterMethodName = getMethod(specFileName, columnIndex);
        //reportRun.get
        String reflectedValue = "";
        try {
            reflectedValue = callGetter(reportRun, getterMethodName);
        } catch (Exception e) {
            System.out.println(e.getCause());
        }
        return reflectedValue;
    }

    /**
     * Given a column index a specification object , this method will provide us with getter methods for particular column
     */
    public static void invokeSetterMethod(IExcelRow reportRun, String specFileName, int columnIndex, String valueToSet) {
        String setterMethodName = getMethod(specFileName, columnIndex);
        try {
            callSetter(reportRun, setterMethodName, valueToSet);
        } catch (Exception e) {
            System.out.println(e.getCause());
        }
    }

    private static void callSetter(Object obj, String fieldName, Object value) {
        PropertyDescriptor pd;
        try {
            pd = new PropertyDescriptor(fieldName, obj.getClass());
            pd.getWriteMethod().invoke(obj, value);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | IntrospectionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static String callGetter(Object obj, String fieldName) {
        PropertyDescriptor pd;
        String returnedVaue = null;
        try {
            pd = new PropertyDescriptor(fieldName, obj.getClass());
            returnedVaue = (String) pd.getReadMethod().invoke(obj);
        } catch (IntrospectionException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return returnedVaue;
    }
}
