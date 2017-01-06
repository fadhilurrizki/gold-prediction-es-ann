/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esann;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Fadhil
 */
public class DataInput {
    //data training hasil normalisasi
    double[] data;
    private double[] normalizedTraining;
    private double[] normalizedTesting;

    private double[] Testing;
    private double max;
    private double min;
    public double[] getTesting() {
        return Testing;
    }
    public double getMax() {
        return max;
    }
    public double getMin() {
        return min;
    }
    public DataInput() throws FileNotFoundException, IOException {
   //load dataset
        String filePath = "D:\\Netbeans Projects\\ESANN\\src\\esann\\";
        String fileName = "dataset1.xlsx";
        FileInputStream file = new FileInputStream(new File(filePath+fileName));
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet sheet = workbook.getSheetAt(0);   
        int rowStart = sheet.getFirstRowNum();
        int rowEnd = sheet.getLastRowNum();
        Row row = sheet.getRow(rowStart);
        int cellCount = row.getLastCellNum();
        int jlhTraining = (int) Math.round((rowEnd)*0.6);
        normalizedTraining = new double[jlhTraining];
        int jlhTesting = rowEnd-jlhTraining;
        normalizedTesting = new double[jlhTesting];
        Testing = new double[jlhTesting+1];
        for (int i = rowStart; i < rowEnd; i++) {
            row = sheet.getRow(i);
            Cell cell = row.getCell(4);
            if(i<jlhTraining)
                normalizedTraining[i] = cell.getNumericCellValue();  
            else {
                normalizedTesting[i-jlhTraining] = cell.getNumericCellValue();     
                cell = row.getCell(0);
                Testing[i-jlhTraining] = cell.getNumericCellValue();        
            }
        }
        row = sheet.getRow(rowEnd);
        Cell cell = row.getCell(0);
        Testing[jlhTesting] = cell.getNumericCellValue();
        row = sheet.getRow(0);
        cell = row.getCell(3);
        this.max = cell.getNumericCellValue();
        row = sheet.getRow(1);
        cell = row.getCell(3);
        this.min = cell.getNumericCellValue();      
        file.close();
    }

    /**
     * @return the normalizedTraining
     */
    public double[] getNormalizedTraining() {
        return normalizedTraining;
    }

    /**
     * @param normalizedTraining the normalizedTraining to set
     */
    public void setNormalizedTraining(double[] normalizedTraining) {
        this.normalizedTraining = normalizedTraining;
    }

    /**
     * @return the normalizedTesting
     */
    public double[] getNormalizedTesting() {
        return normalizedTesting;
    }

    /**
     * @param normalizedTesting the normalizedTesting to set
     */
    public void setNormalizedTesting(double[] normalizedTesting) {
        this.normalizedTesting = normalizedTesting;
    }
}
