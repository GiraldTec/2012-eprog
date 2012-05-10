package GA;


import java.util.ArrayList;
import java.util.Arrays;

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
		int[] myGenes, theirGenes;
		int groupSize = ((GAStudentGene) gen).getGroupSize();
		
		myGenes = ((GAStudentGene) gen).getGen();
		theirGenes = ((GAStudentGene)((GAStudentCromosome) c).getGene()).getGen();
		
		for(int i=0; i < myGenes.length;i=+groupSize){
				if (! checkGroup(Arrays.copyOfRange(myGenes, i, i+groupSize), Arrays.copyOfRange(theirGenes, i, i+groupSize)))
					return false;
		}		
		return true;
	}
	
	private boolean checkGroup(int[] elemsA, int[] elemsB){
		for (int i=0; i<elemsA.length; i++){
			if (!(Arrays.binarySearch(elemsB, elemsA[i]) > 0))
				return false;
		}
		return true;
	}
}