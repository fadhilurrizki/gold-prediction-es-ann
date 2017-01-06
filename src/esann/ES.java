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
public class ES {
    private static int hidden;
    public static Kromosom UniformSelection(Populasi pop){         
        Random r = new Random();
        Kromosom k = pop.getKromosom(r.nextInt(pop.getNumbOfPop())); 
        return k;     
    }      
    
    public static Kromosom CrossoverIL(Populasi pop, int n){ 
    //Rekombinasi Intermediary Lokal         
        Kromosom parent1 = UniformSelection(pop);         
        Kromosom parent2 = UniformSelection(pop);         
        Kromosom child = new Kromosom(n);         
        Random r1 = new Random();       
        for(int i=0;i<parent1.getVariableLenght();i++){             
            double x = parent1.getVariable(i);
            double y = parent2.getVariable(i);             
            double xy = (x+y)/2;     
            child.setVariable(xy, i); 
        }
        pop = null;
        return child;
    }
    
    public static Kromosom CrossoverDL(Populasi pop, int n){ 
    //Rekombinasi Discrete Lokal         
        Kromosom parent1 = UniformSelection(pop);         
        Kromosom parent2 = UniformSelection(pop);         
        Kromosom child = new Kromosom(n);         
        Random r1 = new Random();                 
        for(int i=0;i<parent1.getVariableLenght();i++){  
            int a = r1.nextInt(2);
            if(a == 0) {
                child.setVariable(parent1.getVariable(i), i); 
            } else {
                child.setVariable(parent2.getVariable(i), i); 
            }
        }
        pop = null;
        return child;
    }
    
    public static Kromosom Mutation(Kromosom kromosom) {         
    //Mutasi tanpa korelasi dengan satu thau         
        Random r = new Random();         
        double a = r.nextGaussian();         
        double lr = 1 / (Math.sqrt(kromosom.getVariableLenght()));         
        double[] newMutationStep = new double[kromosom.getVariableLenght()];         
        newMutationStep[0] = kromosom.getMutationStep(0) * (Math.exp(lr * a));
        if(newMutationStep[0]<0.01)
                newMutationStep[0] = 0.01;
        double[] newVariable = new double[kromosom.getVariableLenght()];         
        for (int i = 0; i < kromosom.getVariableLenght(); i++) {             
            double b = r.nextGaussian();
            newVariable[i] = kromosom.getVariable(i) + newMutationStep[0] * b;    
        }             
        Kromosom newKromosom = new Kromosom(newVariable, newMutationStep);     
        kromosom = null;
        return newKromosom;     
    }  
    
    public static Kromosom Mutation2(Kromosom kromosom) {         
    //Mutasi tanpa korelasi dengan n thau         
        Random r = new Random();         
        double a = r.nextGaussian();         
        double aa = r.nextGaussian();         
        double lr = 1 / (Math.sqrt(2*kromosom.getVariableLenght()));         
        double lr2 = 1 / (Math.pow((2 * kromosom.getVariableLenght()), 1 / 4));         
        double[] newMutationStep = new double[kromosom.getVariableLenght()];
   
        for (int i = 0; i < kromosom.getVariableLenght(); i++) {             
            newMutationStep[i] = kromosom.getMutationStep(i) * (Math.exp((lr * a)+(lr2 * aa)));  
            if(newMutationStep[i]<0.01)
                newMutationStep[i] = 0.01;
        }         
        double[] newVariable = new double[kromosom.getVariableLenght()];         
        for (int i = 0; i < kromosom.getVariableLenght(); i++) {             
            double b = r.nextGaussian();             
            newVariable[i] = kromosom.getVariable(i) + newMutationStep[i] * b;         
        }         
        Kromosom newKromosom = new Kromosom(newVariable, newMutationStep);         
        kromosom = null;
        return newKromosom;     
    }  
    
    
    public static Populasi SurvivorSelection(Populasi pop) {         
        Populasi totalPopulation = new Populasi(hidden,pop.getNumbOfPop(), false);         
        for (int i = 0; i < pop.getNumbOfPop(); i++) {             
            totalPopulation.setKromosom(i, pop.getKromosom(i));         
        }         
        pop.sortByFitness();         
        Populasi newPopulation = new Populasi(pop.getKromosom(0).getVariableLenght(), pop.getNumbOfPop()/8, false); 
        for (int i = 0; i < newPopulation.getNumbOfPop(); i++) {             
            newPopulation.setKromosom(i, pop.getKromosom(i));         
        }         
        pop = null;
        return newPopulation;     
    }
    
    public static Populasi Evolution(Populasi pop, int numOfHidden,  DataInput input) {         
    //Tanpa Rekombinasi, Mutasi tanpa korelasi dengan satu thau, Seleksi Survivor 1      
        JST jst = new JST();
        hidden = numOfHidden;
        jst.setLearningRate(0.1);
        jst.setInputNeuron(2);
        jst.setOutputNeuron(1);
        jst.setHiddenNeuron(numOfHidden);
        Populasi newPopulation = new Populasi(numOfHidden, pop.getNumbOfPop() * 8, false);    
        for (int i = 0; i < newPopulation.getNumbOfPop(); i++) { 
            if(i<pop.getNumbOfPop()) {
                newPopulation.setKromosom(i, pop.getKromosom(i));
            } else {      
                Kromosom x = UniformSelection(pop);
                x = Mutation(x);                 
                newPopulation.setKromosom(i, x);  
            }                  
        }                  
        for(int i = 0; i<newPopulation.getNumbOfPop(); i++) {
            double MSE = jst.feedForward(newPopulation.getKromosom(i), input.getNormalizedTraining(),null,0,0, "MSE");
            newPopulation.getKromosom(i).setFitness(MSE);
        }
        newPopulation = SurvivorSelection(newPopulation);   
        pop = null;
        return newPopulation;      
    }     
    
    public static Populasi Evolution2(Populasi pop, int numOfHidden, DataInput input) {         
    //Tanpa Rekombinasi, Mutasi tanpa korelasi dengan n thau, Seleksi Survivor 1  
        JST jst = new JST();
        hidden = numOfHidden;
        jst.setLearningRate(0.1);
        jst.setInputNeuron(2);
        jst.setOutputNeuron(1);
        jst.setHiddenNeuron(numOfHidden);
        Populasi newPopulation = new Populasi(numOfHidden, pop.getNumbOfPop() * 8, false);    
        for (int i = 0; i < newPopulation.getNumbOfPop(); i++) { 
            if(i<pop.getNumbOfPop()) {
                newPopulation.setKromosom(i, pop.getKromosom(i));
            } else {
                Kromosom x = UniformSelection(pop);
                x = Mutation2(x);                 
                newPopulation.setKromosom(i, x);  
            }                  
        }                  
        for(int i = 0; i<newPopulation.getNumbOfPop(); i++) {
            double MSE = jst.feedForward(newPopulation.getKromosom(i), input.getNormalizedTraining(),null,0,0, "MSE");
            newPopulation.getKromosom(i).setFitness(MSE);
        }
        newPopulation = SurvivorSelection(newPopulation);   
        pop = null;
        return newPopulation; 
    }     
    
    public static Populasi Evolution3(Populasi pop, int numOfHidden, DataInput input) {         
    //Rekombinasi Intermediary Lokal, Mutasi tanpa korelasi dengan satu thau, Seleksi Survivor 2   
        JST jst = new JST();
        jst.setLearningRate(0.1);
        jst.setInputNeuron(2);
        jst.setOutputNeuron(1);
        jst.setHiddenNeuron(numOfHidden);
        Populasi newPopulation = new Populasi(numOfHidden,pop.getNumbOfPop() * 8, false);         
        for (int i = 0; i < newPopulation.getNumbOfPop(); i++) {             
               
            Kromosom x;             
            Kromosom y;             
            if (i < pop.getNumbOfPop()) { 
                x = pop.getKromosom(i);                 
                newPopulation.setKromosom(i, x);             
            } else {                 
                y = CrossoverIL(pop,jst.getHiddenNeuron());                 
                x = Mutation2(y);      
                newPopulation.setKromosom(i, x);     
            }         
        }         
        for(int i = 0; i<newPopulation.getNumbOfPop(); i++) {
            double MSE = jst.feedForward(newPopulation.getKromosom(i), input.getNormalizedTraining(),null,0,0, "MSE");
            newPopulation.getKromosom(i).setFitness(MSE);
        }
        newPopulation = SurvivorSelection(newPopulation);     
        pop = null;
        return newPopulation;     
    } 
    
}
