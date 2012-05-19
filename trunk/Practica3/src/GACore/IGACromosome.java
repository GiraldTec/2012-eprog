package GACore;

public abstract class IGACromosome{
	protected double evaluatedValue;     // valor de la funcion de evaluacion
	protected double score;
	protected double acum_score;
	
	public abstract void initCromosome(int minD, int maxD);	
	public abstract IGACromosome clone();
	public abstract void evaluate();
	
	public Boolean mutate(IGAMutator mutator,double prob){
		return mutator.mutate(this, prob);
	}

	//---- Getters & Setters ------------------------------------------------------//

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
