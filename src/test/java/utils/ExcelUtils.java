package utils;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class ExcelUtils {

    static String projPath = System.getProperty("user.dir");

    public static void getExcelRows() throws IOException {
        String excelFilePath = projPath + "/data/empList.xlsx";

        FileInputStream inputStream = new FileInputStream(excelFilePath);
        XSSFWorkbook workBook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workBook.getSheetAt(0);

        /* Using for-loop
        int rows = sheet.getLastRowNum();
        int cols = sheet.getRow(1).getLastCellNum();

        for (int r = 0; r < rows; r++) {
            XSSFRow row = sheet.getRow(r);
            for (int c = 0; c < cols; c++) {
                XSSFCell cell = row.getCell(c);
                switch (cell.getCellType()) {
                    case STRING:
                        System.out.print(cell.getStringCellValue());
                        break;
                    case NUMERIC:
                        System.out.print(cell.getNumericCellValue());
                        break;
                    case BOOLEAN:
                        System.out.print(cell.getBooleanCellValue());
                }
                System.out.print(" | ");
            }
            System.out.println();
        }*/

        //Using iterator
        Iterator iterator = sheet.iterator();
        while (iterator.hasNext()) {
            XSSFRow row = (XSSFRow) iterator.next();
            Iterator cellIterator = row.cellIterator();
            while (cellIterator.hasNext()) {
                XSSFCell cell = (XSSFCell) cellIterator.next();

                switch (cell.getCellType()) {
                    case STRING:
                        System.out.print(cell.getStringCellValue());
                        break;
                    case NUMERIC:
                        System.out.print(cell.getNumericCellValue());
                        break;
                    case BOOLEAN:
                        System.out.print(cell.getBooleanCellValue());
                }
                System.out.print(" | ");
            }
            System.out.println();
        }
    }

    public static void writeExcel() throws IOException {

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("empDetails");

        Object empData[][] = {
                {"EmpId", "Name", "Position"},
                {"1001", "Clara", "Receptionist"},
                {"1002", "Benji", "Engineer"},
                {"1003", "Gladys", "Premier"},
        };

        //Using for-loop
        /*int rows = empData.length;
        int cols = empData[0].length;

        System.out.println("Total no. of rows are " +rows);
        System.out.println("Total no. of columns are " +cols);

        for(int r=0; r<rows; r++) {
            XSSFRow row = sheet.createRow(r);
            for(int c=0; c<cols; c++) {
                XSSFCell cell = row.createCell(c);
                Object value = empData[r][c];

                if(value instanceof String)
                    cell.setCellValue((String) value);
                if(value instanceof Integer)
                    cell.setCellValue((Integer)value);
                if(value instanceof Boolean)
                    cell.setCellValue((Boolean) value);
            }
        }*/

        //Using for..each loop
        /*int rowCount = 0;

        for (Object emp[] : empData) {
            XSSFRow row = sheet.createRow(rowCount++);
            int colCount = 0;
            for (Object value : emp) {
                XSSFCell cell = row.createCell(colCount++);
                if (value instanceof String)
                    cell.setCellValue((String) value);
                if (value instanceof Integer)
                    cell.setCellValue((Integer) value);
                if (value instanceof Boolean)
                    cell.setCellValue((Boolean) value);
            }
        }*/

        /*String filePath = projPath + "/data/newEmpSheet.xlsx";
        FileOutputStream outputStream = new FileOutputStream(filePath);
        workbook.write(outputStream);
        outputStream.close();*/

        //Using ArrayList
        writeExcelUsingArrayList();
    }

    public static void writeExcelUsingArrayList() throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("empDetailsAL");

        ArrayList<Object[]> empData = new ArrayList<Object[]>();
        empData.add(new Object[]{"EmpId", "Name", "Position"});
        empData.add(new Object[]{"1001", "Macho", "Doctor"});
        empData.add(new Object[]{"1002", "Benji", "Engineer"});

        //Using for..each loop
        int rowNum = 0;

        for (Object[] emp : empData) {
            XSSFRow row = sheet.createRow(rowNum++);
            int colNum = 0;
            for (Object value : emp) {
                XSSFCell cell = row.createCell(colNum++);
                if (value instanceof String)
                    cell.setCellValue((String) value);
                if (value instanceof Integer)
                    cell.setCellValue((Integer) value);
                if (value instanceof Boolean)
                    cell.setCellValue((Boolean) value);
            }
        }

        String filePath = projPath + "/data/newEmpSheet.xlsx";
        FileOutputStream outputStream = new FileOutputStream(filePath);
        workbook.write(outputStream);
        outputStream.close();
    }

}
