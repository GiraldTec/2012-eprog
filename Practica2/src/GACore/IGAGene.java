package GACore;

import java.util.ArrayList;

import GA.GAStudent;

public abstract class IGAGene implements Cloneable {
	protected int[] gen;
	
	public abstract Boolean mutate(IGAMutator mutator);
	public abstract double calcBalance(ArrayList<GAStudent> students);
	
	public IGAGene(int numberStudents){
		gen = new int[numberStudents];
		for (int i=0;i<numberStudents;i++){
			gen[i]=i;
		}
	}
	
	public IGAGene(int[] students){
		this.gen=students;
	}
	
	public int[] getGen() {
		return gen;
	}
	public void setGen(int[] students) {
		this.gen = students;
	}
	
}
