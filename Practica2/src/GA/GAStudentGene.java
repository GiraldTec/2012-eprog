package GA;

import java.util.ArrayList;
import java.util.Iterator;

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
	private ArrayList<Integer> gruposConfict;	//sólo para la interfaz

	public GAStudentGene(int numberStudents, int groupSize, double resultAverage, double alpha){
		this.groupSize = groupSize;
		this.numberStudents = numberStudents;
		this.resultAverage = resultAverage;
		this.incompatibilities = 0;
		alphaValue = alpha;
		gruposConfict = new ArrayList<Integer>();
		gen = new int[numberStudents];
		for (int i=0;i<numberStudents;i++){
			gen[i]=i;
		}
		shuffle(gen);
	}

	public GAStudentGene(int[] students, int numberStudents, int groupSize, double resultAverage, double alpha){
		this.gen=students;
		this.groupSize = groupSize;
		this.numberStudents = numberStudents;
		this.resultAverage = resultAverage;
		this.incompatibilities = 0;
		alphaValue = alpha;
		gruposConfict = new ArrayList<Integer>();
	}

	public Boolean mutate(IGAMutator mutator, double prob){
		return mutator.mutate(this,prob);
	}
	
	public GAStudentGene clone(){
		GAStudentGene clone = new GAStudentGene(this.gen.clone(), groupSize, groupSize, alphaValue, alphaValue);
		clone.incompatibilities = this.incompatibilities;
		clone.gruposConfict = this.gruposConfict;
		return clone;
	}
	
	public void loadGene(ArrayList<GAStudent> students){
		// calcular balance
		geneUnbalance = calcBalance(students);
		// calcular la funcion de evaluacion
		//evaluate(students);
	}
	
	/**
	* Desordena el array 'a'. Funcionaría con cualquier tipo de array.
	* Para generar una permutación aleatoria de enteros, bastaría con que
	* 'a' contuviese todos los enteros del 0 al N-1.
	*/
	public static void shuffle(int[] a) {
		log.info("Haciendo shuffle: ");
		for (int i=a.length-1; i>=1; i--) {
			int j = IGARandom.getRInt(i+1); // un numero entre 0 e i, inclusive
			// intercambia a[i], a[j]
			int aux = a[j];
			a[j] = a[i];
			a[i] = aux;
		}
		
		for (int i=0; i<a.length; i++) {
			log.info((a[i]+1)+ " ");
		}
		log.info("\n");
	}

	@Override
	public double evaluate(ArrayList<GAStudent> students) {
		countIncompatibilities (students);
		//System.out.println("INCOPATIBLES: " + incompatibilities);
		return alphaValue * geneUnbalance + ((1 - alphaValue) * incompatibilities);
	}
	
	public double calcBalance(ArrayList<GAStudent> students) {
		double partialUnbal = 0.0;
		int count = 0, j = 0;
		
		geneUnbalance = 0;
		for (int i=0; i < students.size(); i++){
			j = gen[i];
			if (count < groupSize) {
				partialUnbal = partialUnbal + (students.get(j).getResult() - resultAverage);
				count++;
			}
			else { // fin de ese grupo
				geneUnbalance = geneUnbalance + Math.pow(partialUnbal, 2);
				partialUnbal = students.get(j).getResult() - resultAverage;
				count = 1;
			}			
		}
		geneUnbalance = geneUnbalance + Math.pow(partialUnbal, 2);
		return geneUnbalance;
	}
	
	
	public void countIncompatibilities (ArrayList<GAStudent> students){
		int idHater;
		int[][] genOrdenado = new int[gen.length/groupSize][groupSize]; // genOrdenado[i][j] == el estudiante de nombre [i][j] en el grupo i
		int[][] genOrdenadoIds = new int[gen.length/groupSize][groupSize]; 
		int numGrupos = gen.length/groupSize;
		
		incompatibilities=0;
		
		for (int recorredor = 0; recorredor < gen.length; recorredor++){
			genOrdenado[recorredor/groupSize][recorredor%groupSize]= gen[recorredor];
			genOrdenadoIds[recorredor/groupSize][recorredor%groupSize]= students.get(gen[recorredor]).getId();
		}
		
		for(int i=0; i<numGrupos; i++){
			ArrayList<Integer> grupoEnSi = new ArrayList<Integer>();
			//recorriendo el grupo para meterlo en un ArrayList	
			for(int j=0; j<groupSize; j++){	
				grupoEnSi.add(genOrdenadoIds[i][j]);
			}
			// recorremos ahora para examinar la lista de haters de cada miembro
			for(int k=0; k<groupSize; k++){
				GAStudent auxEstudiante = students.get(genOrdenado[i][k]);
				// recorremos la lista de haters
				Iterator<Integer> iterHaters = auxEstudiante.getHaters().iterator();
				while (iterHaters.hasNext()){
					idHater = iterHaters.next();
					if(grupoEnSi.contains(idHater)) {
						incompatibilities++;
						if (!gruposConfict.contains(i))
							gruposConfict.add(i);
					}
				}
			}
		}
	}
	
	public int getNumberStudents() {
		return numberStudents;
	}

	public double getResultAverage() {
		return resultAverage;
	}

	public double getAlphaValue() {
		return alphaValue;
	}
	public int getGroupSize() {
		return groupSize;
	}
	public int getIncompatibilities() {
		return incompatibilities;
	}
	public ArrayList<Integer> getGruposConfict() {
		return gruposConfict;
	}
	
}
