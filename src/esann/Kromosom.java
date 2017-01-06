/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esann;

import java.util.Random;

/**
 *
 * @author Fadhil
 */
public class Kromosom {
    private double[] variable;     
    private double[] stepSize; 
    private double[] rotationAngle;
    private double fitness;     
    private int win;  
    Random r = new Random();
    
    public Kromosom(double[] var, double[] step) {
        this.variable = var;
        this.stepSize = new double[step.length];
        this.stepSize[0] = step[0];
    }
    
    public Kromosom(double[] var, double[] step, double[] rot) {         
        this.variable = var;
        this.stepSize = step;
        this.rotationAngle = rot;       
        this.win = 0;     
    }
    
    public Kromosom(int numOfHidden) {         
        variable = new double[numOfHidden*4+1];
        stepSize = new double[numOfHidden*4+1];
        int k = variable.length*(variable.length-1)/2;
        rotationAngle = new double[k];
        for (int i = 0; i < variable.length; i++) {             
            this.variable[i] = randomizeWeight();    
            this.stepSize[i] = randomizeWeight(); 
        } 
        
        for (int i = 0; i < k; i++) {
            this.rotationAngle[i] = r.nextInt(181);
        }       
        this.win = 0;     
    }     
    
    private double randomizeWeight() {                  
        return r.nextDouble();     
    }     
    
    public double getFitness() {        
        return fitness;     
    }     
    
    public void setFitness(double MSE) {         
        this.fitness = 1 / (MSE);     
    }     
    
    public double getVariable(int index) {         
        return variable[index];     
    }     
    
    public void setVariable(double x, int index) {         
        this.variable[index] = x;     
    }     
    
    public double getMutationStep(int index) {         
        return stepSize[index];     
    }     
    
    public void setMutationStep(double x, int index) {         
        this.stepSize[index] = x;     
    }     
    
    public double getRotationAngle(int index) {
        return rotationAngle[index];
    }
    
    public void setRotationAngle(double x, int index) {
        this.rotationAngle[index] = x;
    }
    
    public int getVariableLenght() {         
        return variable.length;     
    } 
}
