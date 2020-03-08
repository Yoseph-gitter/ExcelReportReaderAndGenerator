package com.kulana.reportDataReader.ExcelUtils.Interfaces;

/**
 * ISpec is a specification that represtents an Excel column. A column  has a name( this can be considered a title ) , an index and data type to retrieve
 * a specific value from a given row. However the Catigories attribute is added incase we want to associate a particular property to that column.
 */
public interface ISpec {
    String[] getCategories();

    Object getDataType();

    int getIndex();

    String getName();
}
