package GACore;

import java.util.ArrayList;

public abstract class IGACromosome{
	protected IGAGene gen; 					// permutacion de alumnos
	protected Double unbalance;			// desequilibrio
	protected Double evaluatedValue;     // valor de la funcion de evaluacion
	protected IGAEvalFunction evalFunct;    // referencia a la funcion de evaluacion
	
	
	public abstract void evaluate();
	public Boolean mutateGen(int type){
		return gen.mutate(type);
	}
	

	
	public IGACromosome (IGAEvalFunction evalFunct) {
		this.evalFunct = evalFunct;
	}	
	
	public abstract void initCromosome();	
	public abstract IGACromosome clone();
	public abstract boolean equals(IGACromosome c);
	
	
	//---- Getters & Setters ------------------------------------------------------//


	public void calcBalance(ArrayList<GAStudent> students){
		//A RELLENAR
	}
	
	public double getUnbalance() {
		return unbalance;
	}
	
	public void setUnbalance(double unbalance) {
		this.unbalance = unbalance;
	}

	public IGAGene getGenes() {
		return gen;
	}
	public void setGene(IGAGene gen) {
		this.gen = gen;
	}

	public IGAEvalFunction getEvalFunct() {
		return evalFunct;
	}
	public void setEvalFunct(IGAEvalFunction evalFunct) {
		this.evalFunct = evalFunct;
	}

}
