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
		
		return clone;
	}

	public void initCromosome(ArrayList<GAStudent> students, int incompatibilities) {
		gen = new GAStudentGene(students.size());
		// calcular balance
		calcBalance(students);
		// calcular la funcion de evaluacion
		evaluate(incompatibilities);
	}
	
	public void loadCromosome(ArrayList<GAStudent> students, int incompatibilities){
		// calcular balance
		calcBalance(students);
		// calcular la funcion de evaluacion
		evaluate(incompatibilities);
	}
	
	@Override
	public boolean equals(IGACromosome c) {
		// TODO Auto-generated method stub
		return false;
	}




}