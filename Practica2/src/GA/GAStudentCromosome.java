package GA;


import java.util.ArrayList;

import GACore.IGACromosome;
import GACore.IGAEvalFunction;
import GA.GAStudentGene;

public class GAStudentCromosome extends IGACromosome implements Cloneable{
	
	public GAStudentCromosome(IGAEvalFunction evalFunct) {
		super(evalFunct);
	}
	
	public GAStudentCromosome clone() {
		GAStudentCromosome clone = new GAStudentCromosome(null);
		GAStudentGene newGene = ((GAStudentGene) gen).clone();
		clone.setGene(newGene);

		clone.setUnbalance(unbalance);
		clone.setEvalFunct(evalFunct);
		
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
	
	public void evaluate(int incompatibilities) {
		// calcula fitness o adaptacion del cromosoma
		//evaluatedValue = (Double) evalFunct.evaluate(numberGenes,fenotype);	// valor de la función a optimizar
	}

	@Override
	public boolean equals(IGACromosome c) {
		// TODO Auto-generated method stub
		return false;
	}




}