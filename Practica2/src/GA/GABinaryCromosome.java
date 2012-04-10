package GA;


import java.lang.reflect.Array;

import GACore.IGACromosome;
import GACore.IGAEvalFunction;
import GACore.IGAEvalFunctionNum;
import GACore.IGARandom;

public class GABinaryCromosome extends IGACromosome<Boolean> implements Cloneable{
	
	public GABinaryCromosome(IGAEvalFunction evalFunct) {
		super(evalFunct);
	}
	
	public GABinaryCromosome clone() {
		GABinaryCromosome clone = new GABinaryCromosome(null);
		//clone.genes = (Boolean[])genes.clone();
		clone.acum_Score = acum_Score;
		clone.aptitude = aptitude;
		clone.numberGenes = numberGenes;
		clone.genes = (GABinaryGene[])Array.newInstance(GABinaryGene.class, numberGenes);
		clone.fenotype = new Double[numberGenes];
		for(int i=0;i<numberGenes;i++){
			clone.genes[i]=new GABinaryGene((Boolean[])genes[i].getGen());
			clone.fenotype[i] = new Double(fenotype[i]);
		}
		clone.score = score;
		return clone;
	}

	public void initCromosome(int cromosome_Lenght) {
		super.initCromosome(cromosome_Lenght);
		genes = (GABinaryGene[])Array.newInstance(GABinaryGene.class, numberGenes);
		for(int j = 0; j < (int)numberGenes; j++){
			genes[j] = new GABinaryGene(new Boolean[cromosome_Lenght]);
			for (int i = 0; i <cromosome_Lenght; i++) {
				((Boolean[])genes[j].getGen())[i] = IGARandom.getRBoolean();
			}
		}
		// calcular fenotipoç
		calcFenotype();
		// calcular aptitud
		evaluate();
	}
	
	public void calcFenotype() {
		for(int i=0;i<numberGenes;i++){

			double cotamax = ((IGAEvalFunctionNum)evalFunct).getXmax();
			double cotamin = ((IGAEvalFunctionNum)evalFunct).getXmin();

			if (((IGAEvalFunctionNum)evalFunct).getType() == 2 || ((IGAEvalFunctionNum)evalFunct).getType() == 4){
				if(i==1){
					cotamax = ((IGAEvalFunctionNum)evalFunct).getYmax();
					cotamin = ((IGAEvalFunctionNum)evalFunct).getYmin();
				}
			}
			
			fenotype[i] = cotamin + (((cotamax - cotamin) * binToDec((Boolean[])genes[i].getGen(),0,cromosome_Lenght)) / (Math.pow(2,(cromosome_Lenght)) - 1));
		}
	}
	
	private double binToDec(Boolean[] genes,int lowLimit, int highLimit) {
		double result=0;
		int auxiliar = 0;
		for(int i=lowLimit;i<highLimit;i++)	
			if(genes[i]){
				result += Math.pow(2,auxiliar);
				auxiliar++;
			}else{
				auxiliar+=1;
			}
		return result;
	}
	
	public void evaluate() {
		// calcula fitness o adaptacion del cromosoma
		aptitude = (Double) evalFunct.evaluate(numberGenes,fenotype);	// valor de la función a optimizar
	}

	public Boolean mutateGen(int gen,double prob) {
		return genes[gen].mutate(prob,cromosome_Lenght);	
	}
	

}
