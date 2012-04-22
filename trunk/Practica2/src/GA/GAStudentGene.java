package GA;

import java.util.ArrayList;
import java.util.Random;

import GACore.IGAGene;
import GACore.IGAMutator;
import GACore.IGARandom;

public class GAStudentGene extends IGAGene{

	public GAStudentGene(int numberStudents){
		super(numberStudents);
		shuffle(gen);
	}

	public GAStudentGene(int[] students){
		super(students);
	}

	public Boolean mutate(IGAMutator mutator){
		return mutator.mutate(this);
	}

	public double calcBalance(ArrayList<GAStudent> students) {
	
		// TODO Auto-generated method stub
		return 0;
	}
	
	public GAStudentGene clone(){
		return null;
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
}
