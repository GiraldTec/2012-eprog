package GA;

import java.util.ArrayList;

import GACore.IGAGene;
import GACore.IGAMutator;
import GACore.IGARandom;

public class GAStudentGene extends IGAGene implements Cloneable{
	private int groupSize;
	private int numberStudents;
	private double resultAverage;
	private double alphaValue;
	private double geneUnbalance;
	
	public GAStudentGene(int numberStudents, int groupSize, double resultAverage, double alpha){
		this.groupSize = groupSize;
		this.numberStudents = numberStudents;
		this.resultAverage = resultAverage;
		alphaValue = alpha;
		gen = new int[numberStudents];
		for (int i=0;i<numberStudents;i++){
			gen[i]=i;
		}
		shuffle(gen);
	}

	public GAStudentGene(int[] students){
		this.gen=students;
	}

	public Boolean mutate(IGAMutator mutator, double prob){
		return mutator.mutate(this,prob);
	}
	
	public GAStudentGene clone(){
		return new GAStudentGene(this.gen.clone());
	}
	
	/**
	* Desordena el array 'a'. Funcionaría con cualquier tipo de array.
	* Para generar una permutación aleatoria de enteros, bastaría con que
	* 'a' contuviese todos los enteros del 0 al N-1.
	*/
	public static void shuffle(int[] a) {
		for (int i=a.length-1; i>=1; i--) {
			int j = IGARandom.getRInt(i+1); // un numero entre 0 e i, inclusive
			// intercambia a[i], a[j]
			int aux = a[j];
			a[j] = a[i];
			a[i] = aux;
		}
	}

	@Override
	public double evaluate(int incompatibilities) {
		return alphaValue * geneUnbalance + ((1 - alphaValue) * incompatibilities);
	}
	
	public double calcBalance(ArrayList<GAStudent> students) {
		double partialUnbal;
		geneUnbalance = 0;
		for (int i=0; i < students.size(); i++){
			partialUnbal = 0.0f;
			for (int j=0; j < groupSize; j++){
				partialUnbal += students.get(i+j).getResult() - resultAverage;
			}
			geneUnbalance += Math.pow(partialUnbal, 2);
		}
		return geneUnbalance;
	}
}
