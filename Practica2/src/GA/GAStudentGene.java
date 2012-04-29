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
	private int incompatibilities;
	
	public GAStudentGene(int numberStudents, int groupSize, double resultAverage, double alpha){
		this.groupSize = groupSize;
		this.numberStudents = numberStudents;
		this.resultAverage = resultAverage;
		this.incompatibilities = 0;
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
	public double evaluate(ArrayList<GAStudent> students) {
		incompatibilities = countIncompatibilities (students);
		return alphaValue * geneUnbalance + ((1 - alphaValue) * incompatibilities);
	}
	
	public double calcBalance(ArrayList<GAStudent> students) {
		double partialUnbal = 0.0;
		int count = 0;
		
		geneUnbalance = 0;
		for (int i=0; i < students.size(); i++){
			if (count < groupSize) {
				int j = gen[i];
				partialUnbal += students.get(j).getResult() - resultAverage;
				count++;
			}
			else { // fin de ese grupo
				geneUnbalance += Math.pow(partialUnbal, 2);
				partialUnbal = 0;
				count = 0;
			}			
		}
		return geneUnbalance;
		
	
	}
	
	
	public int countIncompatibilities (ArrayList<GAStudent> students){
		
		int[][] genOrdenado = new int[gen.length/groupSize][groupSize]; // genOrdenado[i][j] == el estudiante de nombre [i][j] en el grupo i
		int numGrupos = gen.length/groupSize;
		
		for (int recorredor =0;recorredor<gen.length;recorredor++){
			genOrdenado[recorredor/groupSize][recorredor%groupSize]= gen[recorredor];
		}
		
		for(int i=0;i<numGrupos;i++){
			ArrayList<Integer> grupoEnSi = new ArrayList<Integer>();
			for(int j=0;j<groupSize;j++){//recorriendo el grupo para meterlo en un ArrayList		
				grupoEnSi.add(genOrdenado[i][j]);
			}
			for(int j=0;j<groupSize;j++){// recorremos ahora para examinar la lista de haters de cada miembro
				GAStudent auxEstudiante = students.get(genOrdenado[i][j]);
				while (auxEstudiante.getHaters().iterator().hasNext()){ // recorremos la lista de haters
					if(grupoEnSi.contains(auxEstudiante.getHaters().iterator().next())){
						incompatibilities++;
					}
				}
			}
		}
		
		
		return 0;
	}
}
