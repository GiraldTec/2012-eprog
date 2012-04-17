package GACore;

import java.util.ArrayList;

import GA.GAStudent;

public abstract class IGAGene {
	protected int[] gen;
	
	public abstract Boolean mutate(int type);
	public abstract double calcBalance(ArrayList<GAStudent> students);
	
	public IGAGene(ArrayList<GAStudent> students){
		//Colocar una permutacion de los indices de Student
		gen = null;
	}
	
	public Object getGen() {
		return gen;
	}
	public void setGen(int[] students) {
		this.gen = students;
	}

	
	
}
