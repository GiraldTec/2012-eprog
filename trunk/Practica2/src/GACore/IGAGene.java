package GACore;

import java.util.ArrayList;

import GA.GAStudent;

public abstract class IGAGene {
	protected int[] gen;
	
	public abstract void replace(int pos, IGAGene other);
	public abstract Boolean mutate(int type);
	
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
