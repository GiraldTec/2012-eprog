package GACore;

import java.util.logging.Logger;

public abstract class IGAGene {
	protected int[] gen;
	
	public abstract Boolean mutate(IGAMutator mutator, double prob);
	public abstract double evaluate();
	
	public static Logger log = Logger.getLogger("Engine");	
	
	public int[] getGen() {
		return gen;
	}
	public void setGen(int[] students) {
		this.gen = students;
	}
	
}
