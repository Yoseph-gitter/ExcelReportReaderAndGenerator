ExcelReportCreator

This project is about reading and creating a report from an excel file using a predefined specification that tells the program the structure of the input file and how the resulting report in going to look like

Some Quick Notes on how to use this Tool : Excel Reader and Report Generator

Always make sure your Excel has only one row of heading otherwise you should adjust the code "readExcel" for same since headers are all string and the other data rows has some particular data type.

when you want to filter by a certain column you just have to set to a particular value of interest Ex: ExcelUiReader.FILTERING_COLUMN = 2

To manipulate and filter data that you want out of an Excel sheet, i have what is called specifications : they are json files

that thaks about the structure of the excel , index , data type or name that would be required to read the file seemlsly and would make it document agnostic.

There is also an out put specification file that you may want to provide if you want certain data and in certain ordering for your output.

Essentially how it does its job is : read Spec file , grab excel file read as per the spec file and generate a report as per the output specification
