package GA;


import java.util.ArrayList;

import GACore.IGACromosome;
import GA.GAStudentGene;

public class GAStudentCromosome extends IGACromosome implements Cloneable{
	
	public GAStudentCromosome clone() {
		GAStudentCromosome clone = new GAStudentCromosome();
		GAStudentGene newGene = ((GAStudentGene) gen).clone();
		clone.setGene(newGene);

		clone.setUnbalance(unbalance);
		clone.setEvaluatedValue(evaluatedValue);

		
		return clone;
	}

	public void initCromosome(ArrayList<GAStudent> students, int groupSize, double resultAverage, double alpha) {
		gen = new GAStudentGene(students.size(), groupSize, resultAverage, alpha);
		loadCromosome(students);
	}
	
	public void loadCromosome(ArrayList<GAStudent> students){
		// calcular balance
		calcBalance(students);
		// calcular la funcion de evaluacion
		evaluate(students);
	}
	
	@Override
	public boolean equals(IGACromosome c) {
		// TODO Auto-generated method stub
		return false;
	}
}