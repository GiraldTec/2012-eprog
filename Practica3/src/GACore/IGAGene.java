package GACore;

import java.util.ArrayList;
import java.util.logging.Logger;

import GA.GAStudent;

public abstract class IGAGene {
	protected int[] gen;
	
	public abstract Boolean mutate(IGAMutator mutator, double prob);
	public abstract double calcBalance(ArrayList<GAStudent> students);
	public abstract double evaluate(ArrayList<GAStudent> students);
	
	public static Logger log = Logger.getLogger("Engine");	
	
	public int[] getGen() {
		return gen;
	}
	public void setGen(int[] students) {
		this.gen = students;
	}
	
}
