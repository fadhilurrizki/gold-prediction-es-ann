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
public class JST {
    private double LearningRate = 0.5;
    private int InputNeuron = 2;
    private int HiddenNeuron = 50;
    private int OutputNeuron = 1;

    public double feedForward(Kromosom kromosom, double[] data, double[] asli,double max,double min, String performansi) {
        double[] a2 = new double[data.length];
        double[] error = new double[data.length-2];
        double hasil = 0;
        String hitung = performansi;
        for (int loop = 0; loop < data.length - 2; loop++) {
            //hitung summing function di hidden layer (A1)
            double[] a1 = new double[getHiddenNeuron()];
            double[] sum1 = new double[getHiddenNeuron()];
            for (int h = 0; h < getHiddenNeuron(); h++) {
                sum1[h] = data[loop] * kromosom.getVariable(4 * h) + data[loop + 1] * kromosom.getVariable(4 * h + 1) + kromosom.getVariable(4 * h + 2);
                a1[h] = 1 / (1 + Math.exp(-sum1[h])); //fungsi sigmoid bipolar
            }

            //hitung summing function di output (A2)
            a2 = new double[getOutputNeuron()];
            double[] sum2 = new double[getOutputNeuron()];
            for (int o = 0; o < getOutputNeuron(); o++) {
         
                for (int h = 0; h < getHiddenNeuron(); h++) {
                    sum2[o] += (a1[h] * kromosom.getVariable(4 * h + 3));
                }
                sum2[o] += kromosom.getVariable(kromosom.getVariableLenght()-1);
                a2[o] = 1 / (1 + Math.exp(-sum2[o])); //fungsi sigmoid
            }
            if (hitung.equals("MSE")) {
                error[loop] = data[loop + 2] - a2[0];
            } else if (hitung.equals("MAPE")) {
                double denormalize = (a2[0]-0.1)/0.8*(max-min)+min;
                double s = denormalize*asli[loop+2]+asli[loop+2];
                error[loop] = Math.abs((asli[loop + 3] - s)/asli[loop + 3]);
              
            }
        }
        if (hitung.equals("MSE")) {
            double jlh = 0;
            for (int i = 0; i < data.length - 2; i++) {
                jlh += error[i] * error[i];
            }
            hasil = jlh / (data.length - 1);
        } else if (hitung.equals("MAPE")) {
            hasil = 0;
            for (int i = 0; i < data.length - 2; i++) {
                hasil += error[i];
            }
            hasil = ((double)1/(double)(error.length)) * hasil * 100;
        }
        return hasil;
    }

   

    /**
     * @return the LearningRate
     */
    public double getLearningRate() {
        return LearningRate;
    }

    /**
     * @param LearningRate the LearningRate to set
     */
    public void setLearningRate(double LearningRate) {
        this.LearningRate = LearningRate;
    }

    /**
     * @return the InputNeuron
     */
    public int getInputNeuron() {
        return InputNeuron;
    }

    /**
     * @param InputNeuron the InputNeuron to set
     */
    public void setInputNeuron(int InputNeuron) {
        this.InputNeuron = InputNeuron;
    }

    /**
     * @return the HiddenNeuron
     */
    public int getHiddenNeuron() {
        return HiddenNeuron;
    }

    /**
     * @param HiddenNeuron the HiddenNeuron to set
     */
    public void setHiddenNeuron(int HiddenNeuron) {
        this.HiddenNeuron = HiddenNeuron;
    }

    /**
     * @return the OutputNeuron
     */
    public int getOutputNeuron() {
        return OutputNeuron;
    }

    /**
     * @param OutputNeuron the OutputNeuron to set
     */
    public void setOutputNeuron(int OutputNeuron) {
        this.OutputNeuron = OutputNeuron;
    }
}
