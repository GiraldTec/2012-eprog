package GA;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

import GACore.IGACromosome;
import GACore.IGAEngine;
import GACore.IGARandom;

public final class GAStudentsEngine extends IGAEngine {
	private ArrayList<GAStudent> students;
	private HashMap<Integer, Integer> studentMap;
	private int groupSize = 2;
	private int fillerId = -1;
	private String studentPath="1.txt";
	protected int incompatibilities;
	protected double resultAverage;
	 
	
	public void init()
	{		
		students = new ArrayList<GAStudent>();
		studentMap = new HashMap<Integer, Integer>();
		fillerId = -1;
				
		// crear funci�n de selecci�n
		selector = new GARouletteSelection();
		if (selectorName.equals("Ruleta"))
			selector = new GARouletteSelection();
		else if (selectorName.equals("Torneo Det"))
			selector = new GATournametSelectionDet();
		else if (selectorName.equals("Torneo Prob")){
			selector = new GATournamentSelectionProb();
			((GATournamentSelectionProb)selector).loadSelectorConf(selecParams);
		}
		else if (selectorName.equals("Ranking"))
			selector = new GARankingSelection();
		else if (selectorName.equals("Shuffle"))
			selector = new GAShuffleSelection();
		else
			System.err.println("Error al elegir la funci�n de selecci�n");
			
		// cargar alumnos
		loadStudents(studentPath);
		
		// calculamos la media de sus notas
		calcAverageScore();
						
		// inicializar generaci�n
		current_Generation = 0;

		// inicializar array de poblaci�n
		population = new GAStudentCromosome[population_Size]; 
		
		// crear poblaci�n inicial
		for (int i = 0; i < population_Size; i++) {
			population[i] = new GAStudentCromosome();
			((GAStudentCromosome)population[i]).initCromosome(students, groupSize, resultAverage, alfaValue);
		}
		
		// asignar un individuo elite inicial
		elite = (GAStudentCromosome)population[0].clone();

		//crear el cruzador
		if (crossName.equals("PMX"))
			cruzador = new GAPartialStudentPairing();
		else if (crossName.equals("OX"))
			cruzador = new GAStudentOrderCross();
		else if (crossName.equals("Variante OX"))
			cruzador = new GAStudentOrderCrossVariant();
		else if (crossName.equals("Ordinal"))
			cruzador = new GAStudentOrdinalCross();
		else if (crossName.equals("Cremallera"))
			cruzador = new GAStudentOrdinalCrossCremallera();
		else
			System.err.println("Error al elegir la funci�n de cruce");
		
		//crear el mutador
		if (mutName.equals("Inserci�n"))
			mutador = new GAMutatorInsertion();
		else if (mutName.equals("Intercambio"))
			mutador = new GAMutatorExchange();
		else if (mutName.equals("Inversi�n"))
			mutador = new GAMutatorInversion();
		else if (mutName.equals("Heur�stica")){
			mutador = new GAMutator3Heuristic();
			((GAMutator3Heuristic)mutador).getExtraParams(students);
		}
		else
			System.err.println("Error al elegir la funci�n de mutaci�n");
			
	}
	
	public void loadConfig(String config) {
	}
	
	private void loadStudents(String path)
	{
		try{

			if (path.isEmpty())
				path  = "p.txt";

			// Open the file
			FileInputStream fstream = new FileInputStream("data/" + path);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine, data[];
			GAStudent student;
			int numRestrictions, number_of_students;

			// Read first line: | Num Students | Num restrictions | 
			strLine = br.readLine();
			data = strLine.split(" ");

			//log.info("Num estudiantes: "+ data[0] + "\nNum restricciones: " + data[1]);
			log.info("Num estudiantes: "+ data[0] + "\nNum restricciones: " + data[1]);
			
			number_of_students = Integer.parseInt(data[0]);		
			numRestrictions = Integer.parseInt(data[1]);

			// Read Student data: | ID | Result |
			for (int i=0; i<number_of_students; i++) {
				strLine = br.readLine();
				data = strLine.split(" ");

				log.info("Id : "+ data[0] + " Result: " + data[1]);

				// Create student and load id + result
				student = new GAStudent(Integer.parseInt(data[0]), Double.parseDouble(data[1]));
				studentMap.put(student.getId(), students.size());
				students.add(student);
			}

			// Read restrictions:
			for (int i=0; i<numRestrictions; i++)
			{
				strLine = br.readLine();
				data = strLine.split(" ");

				log.info("Student : "+ data[0] + " hates " + data[1]);

				student = students.get(studentMap.get(Integer.parseInt(data[0])));
				student.getHaters().add(Integer.parseInt(data[1]));
			}	

			// Add filler students
			while (number_of_students % groupSize != 0){
				student = new GAStudent(fillerId, 0.0);
				studentMap.put(student.getId(), students.size());
				students.add(student);
				log.info("Num filler student: " + fillerId);
				fillerId--;
				number_of_students++;
			}

			// Close the input stream
			in.close();
		}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}
	
	private void calcAverageScore(){
		double resultAvrg = 0;
		for (int i=0; i<students.size(); i++){
			resultAvrg += students.get(i).getResult();
		}
		resultAvrg /= students.size();
		
		resultAverage = resultAvrg;
	}

	
	 protected void mutate(){
			log.info("Engine: mutate");
			
			for (int i=0; i < population_Size; i++) {
				//METER EL TIPO DE LA MUTACI�N Y LOS ESTUDIANTES
				if (population[i].mutate(mutador, prob_Mut)) {
					population[i].calcBalance(students);
					population[i].evaluate(students);
				}
				
				log.info("Grupos tras mutaci�n: ");
				for (Integer j : population[i].getGene().getGen())
				{
					log.info(j + " ");
				}
				log.info("\n");
			}
		}
	 
	 protected void reproducePopulation(){
			int[] sel_Cross = new int[population_Size];	//seleccionados para reproducir
			int num_Sel_Cross = 0;						//contador seleccionados			
			double rand_prob_Cross;						//probabilidad de producirse un cruce
			
			log.info("Engine: reproducePopulation");
			
			//Se eligen los individuos a cruzar
			for (int i=0; i<population_Size; i++) {
				//se generan tam_pob n�meros aleatorios en [0 1)
				rand_prob_Cross = IGARandom.getRDouble();
				//se eligen los individuos de las posiciones i si prob < prob_cruce
				if (rand_prob_Cross < prob_Cross){
					sel_Cross[num_Sel_Cross] = i;
					num_Sel_Cross++;
				}
			}
			
			// el numero de seleccionados se hace par
			if ((num_Sel_Cross % 2) == 1)
				num_Sel_Cross--;
			
			// se cruzan los individuos elegidos en un punto al azar
			
			for (int i=0; i<num_Sel_Cross; i+=2){
				GAStudentCromosome[] parents = new GAStudentCromosome[2];
				parents[0] = (GAStudentCromosome) auxiliar_population[sel_Cross[i]];
				parents[1] = (GAStudentCromosome) auxiliar_population[sel_Cross[i+1]];
				GAStudentCromosome[] descendientes = (GAStudentCromosome[]) cruzador.cross(parents);
				// los nuevos individuos sustituyen a sus progenitores
				auxiliar_population[sel_Cross[i]] = descendientes[0];
				auxiliar_population[sel_Cross[i]].loadCromosome(students);
				auxiliar_population[sel_Cross[i+1]] = descendientes[1];
				auxiliar_population[sel_Cross[i+1]].loadCromosome(students);
			}
			
			// si usamos elitismo sustituir a los peores individuos de la poblaci�n por los hijos
			if (useElitism) {
				final class CromoData implements Comparable<CromoData> {
					private double aptitud;
					private int possition;
		
					public CromoData(double apt, int pos) {
						aptitud = apt;
						possition = pos;
					}
		
					public int compareTo(CromoData o) {
		
						if (this.aptitud == o.getAptitude())
							return 0;
						else {
							if (this.aptitud < o.getAptitude()) {
								return 1;
							} else
								return -1;
						}
					}
		
					public double getAptitude() {
						return aptitud;
					}
				}
		
				PriorityQueue<CromoData> rank = new PriorityQueue<CromoData>();
				for (int i=0; i<population_Size;i++){
					rank.add(new CromoData(population[i].getEvaluatedValue(),i));
				}
				for(int i=0;i<num_Sel_Cross;i++){
					population[rank.poll().possition]=auxiliar_population[sel_Cross[i]];
				}
			}
			
			// si no usamos elitismo sustituir padres por hijos directamente
			else {
				for(int i=0;i<num_Sel_Cross;i++){
					population[sel_Cross[i]]=auxiliar_population[sel_Cross[i]];
				}
			}
		}	
	
	// Getters Setters ....
	public IGACromosome getAbsoluteBest() {
		return elite;
	}
	public IGACromosome getGenerationBest() {
		return generationBest;
	}
	public int getGroupSize() {
		return groupSize;
	}
	public void setGroupSize(int groupSize) {
		this.groupSize = groupSize;
	}
	public String getStudentPath() {
		return studentPath;
	}
	public void setStudentPath(String studentPath) {
		this.studentPath = studentPath;
	}
	public ArrayList<GAStudent> getStudents() {
		return students;
	}
}
