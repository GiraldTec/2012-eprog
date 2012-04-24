package GACore;

import java.util.ArrayList;

import GA.GAStudent;

public abstract class IGAGene {
	protected int[] gen;
	
	public abstract Boolean mutate(IGAMutator mutator);
	public abstract double calcBalance(ArrayList<GAStudent> students);
	
	public int[] getGen() {
		return gen;
	}
	public void setGen(int[] students) {
		this.gen = students;
	}
	
}
