package GACore;

public abstract class IGACromosome <T> {
	protected IGAGene[] genes; 					// genotipo
	protected Double[] fenotype;				// fenotipoX
	protected double aptitude;				// función de evaluación fitness adaptación
	protected double score; 				// puntuación relativa (aptitud/suma)
	protected double acum_Score; 			// puntuación acumulada para selección
	protected IGAEvalFunction evalFunct;	// función de evaluación
	protected int cromosome_Lenght;			// longitud del cromosoma
	protected int numberGenes;

	public abstract void evaluate();
	public abstract void calcFenotype();
	public abstract Boolean mutateGen(int gen,double prob);
	
	public void initCromosome(int cromosome_Lenght){
		this.cromosome_Lenght = cromosome_Lenght;
		this.numberGenes = ((IGAEvalFunctionNum) evalFunct).getNumVars();
		fenotype = new Double[numberGenes];
	}
	
	public IGACromosome (IGAEvalFunction evalFunct) {
		this.evalFunct = evalFunct;
	}	
	
	public abstract IGACromosome<T> clone();
	
	//---- Getters & Setters ------------------------------------------------------//
	public double getAptitude() {
		return aptitude;
	}
	public void setAptitude(double aptitude) {
		this.aptitude = aptitude;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	public double getAcum_Score() {
		return acum_Score;
	}
	public void setAcum_Score(double acum_Score) {
		this.acum_Score = acum_Score;
	}
	public IGAGene[] getGenes() {
		return genes;
	}
	public void setGenes(IGAGene[] genes) {
		this.genes = genes;
	}
	public IGAGene getGene(int pos) {
        return genes[pos];
	}
	public void setGene(int pos, T t) {
		genes[pos].setGen(t);
	}
	public IGAEvalFunction getEvalFunct() {
		return evalFunct;
	}
	public void setEvalFunct(IGAEvalFunction evalFunct) {
		this.evalFunct = evalFunct;
	}
	public int getLength(){
		return cromosome_Lenght;
	}
	public Double[] getFenotypes() {
		return fenotype;
	}
	public double getFenotype(int pos){
		return fenotype[pos];
	}
	public int getNumberGenes(){
		return numberGenes;
	}

}
