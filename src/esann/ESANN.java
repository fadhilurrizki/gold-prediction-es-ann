/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esann;

import java.awt.Color;
import java.awt.Panel;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JPanel;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFChartSheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jfree.ui.RefineryUtilities;


/**
 *
 * @author Fadhil
 */
public class ESANN {

    /**
     * @param args the command line arguments
     */
    private static int[] numOfGen = {400, 200, 50};
    private static int[] numOfPop = {50, 100, 400};
    private static final int[] numOfHidden = {10,15,40};
    private  static final int kombinasiParameter = 30*3*numOfGen.length*numOfHidden.length;
    private static double[] performansi = new double[kombinasiParameter];
   
    public static void main(String[] args) throws FileNotFoundException, IOException {
        // TODO code application logic here
   
        DataInput input = new DataInput();
        double[] datatraining = input.getNormalizedTraining();
        double[] datatesting = input.getNormalizedTesting();
        double[] datatestingasli = input.getTesting();
        double max = input.getMax();
        double min = input.getMin();
        //set parameter jstk,
        //generate populasi
        int iterasi = 0;
        XSSFWorkbook testing = new XSSFWorkbook();
        String filePath = "D:\\Netbeans Projects\\ESANN\\src\\esann\\";
        String fileName = "hasilTesting.xlsx";
        FileOutputStream fileOutTest = new FileOutputStream(filePath+fileName);  
        XSSFSheet sheetTest = testing.createSheet("Testing");
        for(int i = 0; i<3; i++) {
        //skema evolusi ES
            for(int j=0; j<numOfHidden.length; j++) {
                //jumlah hidden layer 
                for(int k=0; k<numOfPop.length; k++) {
                    //ukuran populasi
                    XSSFWorkbook output = new XSSFWorkbook();         
                    filePath = "D:\\Netbeans Projects\\ESANN\\src\\esann\\";
                    fileName = String.valueOf(i) + "-" + String.valueOf(numOfHidden[j]) + "-" + String.valueOf(numOfPop[k])+ ".xlsx";
                    FileOutputStream fileOut = new FileOutputStream(filePath+fileName);       
                    for(int m = 0; m<30; m++) {
                        XSSFSheet sheetOutput = output.createSheet(String.valueOf(m));
                        Populasi pop = new Populasi(numOfHidden[j],numOfPop[k],true);
                        double data[] = new double[numOfGen[k]];
                        for(int l = 0; l<numOfGen[k]; l++) {
                            Row r = sheetOutput.createRow(l);
                            Cell c = r.createCell(0);
                            c.setCellValue(l); 
                            switch (i) {
                                case 0 : pop = ES.Evolution(pop, numOfHidden[j], input);
                                    break;
                                case 1 : pop = ES.Evolution2(pop, numOfHidden[j], input);
                                    break;
                                case 2 : pop = ES.Evolution3(pop, numOfHidden[j], input);
                                    break;       
                                
                            }     
                            data[l] = pop.getFittest().getFitness();
                
                            c = r.createCell(1);
                            c.setCellValue(pop.getFittest().getFitness());
                            c = r.createCell(2);
                            c.setCellValue(1/pop.getFittest().getFitness());
                        }
                        Grafik demo = new Grafik("Fitness Generasi",data,iterasi);
                        Kromosom kromosom = pop.getFittest();
                        //testing, hitung MAPE
                        JST jst = new JST();
                        jst.setHiddenNeuron(numOfHidden[j]);
                        double MAPE = jst.feedForward(kromosom, datatesting,datatestingasli,max,min, "MAPE"); 
                        System.out.println("MAPE : " + MAPE);
                        Row r = sheetTest.createRow(iterasi);
                        for (int z = 0; z < 7; z++) {
                                Cell c = r.createCell(z);
                                switch (z) {
                                    case 0:
                                        c.setCellValue(i);
                                        break;
                                    case 1:
                                        c.setCellValue(numOfHidden[j]);
                                        break;
                                    case 2:
                                        c.setCellValue(numOfPop[k]);
                                        break;
                                    case 3:
                                        c.setCellValue(numOfGen[k]);
                                        break;
                                    case 4:
                                        c.setCellValue(kromosom.getFitness());
                                        break;
                                    case 5:
                                        c.setCellValue(1/kromosom.getFitness());
                                        break; 
                                    case 6:
                                        c.setCellValue(MAPE);
                                        break; 

                               }
                        }       
                        iterasi++;
                        System.out.println("Completed : " + iterasi + "/" + kombinasiParameter);
                        pop = null;
                    } 
                    output.write(fileOut);
                    fileOut.close();          
                }
            }
        }
        testing.write(fileOutTest);
        fileOutTest.close();
         
    }  

   
}
