package GACore;

import java.util.ArrayList;

import GA.GAStudent;

public abstract class IGACromosome{
	protected IGAGene gen; 					// permutacion de alumnos
	protected double unbalance;			// desequilibrio
	protected double evaluatedValue;     // valor de la funcion de evaluacion
	protected double score;
	protected double acum_score;
	
	public abstract void initCromosome(ArrayList<GAStudent> students, int incompatibilities, int groupSize, double resultAverage, double alpha);	
	public abstract IGACromosome clone();
	public abstract boolean equals(IGACromosome c);
	
	public void evaluate(ArrayList<GAStudent> students){
		evaluatedValue = gen.evaluate(students);
	};
	
	public Boolean mutate(IGAMutator mutator,double prob){
		return gen.mutate(mutator,  prob);
	}
	
	public void calcBalance(ArrayList<GAStudent> students){
		unbalance = gen.calcBalance(students);
	}

	//---- Getters & Setters ------------------------------------------------------//

	//GEN
	public IGAGene getGene() {
		return this.gen;
	}
	public void setGene(IGAGene gen) {
		this.gen = gen;
	}
	
	//UNBALANCE
	public double getUnbalance() {
		return unbalance;
	}
	
	public void setUnbalance(double unbalance) {
		this.unbalance = unbalance;
	}
	
	//EVALUATED VALUE
	public double getEvaluatedValue() {
		return evaluatedValue;
	}
	public void setEvaluatedValue(double evaluatedValue) {
		this.evaluatedValue = evaluatedValue;
	}
	
	//SCORE & ACUM-SCORE
	
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}		
	public double getAcum_Score() {
		return acum_score;
	}
	public void setAcum_Score(double acum_score) {
		this.acum_score = acum_score;
	}
}
