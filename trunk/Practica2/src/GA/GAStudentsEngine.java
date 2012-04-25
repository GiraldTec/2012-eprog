package GA;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

import GACore.IGACromosome;
import GACore.IGAEngine;
import GACore.IGARandom;

public final class GAStudentsEngine extends IGAEngine {
	private ArrayList<GAStudent> students;
	private HashMap<Integer, Integer> studentMap;
	private int groupSize = 6;
	private int fillerId = -1;
	private String studentPath;
	protected int incompatibilities;	
	
	public void init()
	{
		int id;
		
		students = new ArrayList<GAStudent>();
		studentMap = new HashMap<Integer, Integer>();
		fillerId = -1;
		
	/*	// crear función de selección
		if (selectorName.equals("Ruleta"))
			selector = new GARouletteSelection<Boolean>();
		else if (selectorName.equals("Torneo Det"))
			selector = new GATournametSelectionDet<Boolean>();
		else if (selectorName.equals("Torneo Prob"))
			selector = new GATournamentSelectionProb<Boolean>();
		else 
			System.err.println("Error al elegir función de selección");
						
		// inicializar generación
		current_Generation = 0;
		
		// inicializar array de población
		population = new GAStudentCromosome[population_Size]; 
		
		// crear población inicial
		for (int i = 0; i < population_Size; i++) {
			population[i] = new GAStudentCromosome();
			((GAStudentCromosome)population[i]).initCromosome(students,incompatibilities);
		}
		
		// asignar un individuo elite inicial
		elite = (GAStudentCromosome)population[0].clone();
		
		//crear el cruzador
		if (crossName.equals("Monopunto"))
			cruzador = new GABinaryMonoPointCross();
		else if (crossName.equals("Bipunto"))
			cruzador = new GABinaryBiPointCross();
		else
			System.err.println("Error al crear función de cruce");*/
		
		//crear el mutador
	}
	
	public void loadConfig(String config) {
			
		
	}
	
	private void loadStudents(String path)
	{
		try{
		  // Open the file
		  FileInputStream fstream = new FileInputStream("data/" + path);
		  DataInputStream in = new DataInputStream(fstream);
		  BufferedReader br = new BufferedReader(new InputStreamReader(in));
		  String strLine, data[];
		  GAStudent student;
		  int numRestrictions;
		  
		  // Read first line: | Num Students | Num restrictions | 
		  strLine = br.readLine();
		  data = strLine.split(" ");
		  
		  //log.info("Num estudiantes: "+ data[0] + "\nNum restricciones: " + data[1]);
		  System.out.println("Num estudiantes: "+ data[0] + "\nNum restricciones: " + data[1]);
		  
		  population_Size = Integer.parseInt(data[0]);
		  numRestrictions = Integer.parseInt(data[1]);
		  
		  // Read Student data: | ID | Result |
		  for (int i=0; i<population_Size; i++) {
			  strLine = br.readLine();
			  data = strLine.split(" ");
			  
			  System.out.println("Id : "+ data[0] + " Result: " + data[1]);
			  
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
			  
			  System.out.println("Student : "+ data[0] + " hates " + data[1]);
			  
			  student = students.get(studentMap.get(Integer.parseInt(data[0])));
			  student.getHaters().add(Integer.parseInt(data[1]));
		  }
		  
		  // Add filler students
		  while (population_Size % groupSize != 0){
			  student = new GAStudent(fillerId, 0.0);
			  studentMap.put(student.getId(), students.size());
			  students.add(student);
			  System.out.println("Num filler student: " + fillerId);
			  fillerId--;
			  population_Size++;
		  }
		  
		  // Close the input stream
		  in.close();
		    }catch (Exception e){//Catch exception if any
		  System.err.println("Error: " + e.getMessage());
		  }
	}
	
	/*public static void main(String[] args) {
		GAStudentsEngine ga = new GAStudentsEngine();
		ga.init();
		ga.loadStudents("data/1.txt");
	}*/

	
	 protected void mutate(){
			boolean hasMutated;

			log.info("Engine: mutate");
			
			for (int i=0; i < population_Size; i++) {
				//METER EL TIPO DE LA MUTACIÓN Y LOS ESTUDIANTES
				if (population[i].mutate(mutador)) {
					population[i].calcBalance(students);
					population[i].evaluate(incompatibilities);
				}
			}
		}
	 
	 protected void reproducePopulation(){
			int[] sel_Cross = new int[population_Size];	//seleccionados para reproducir
			int num_Sel_Cross = 0;						//contador seleccionados			
			double rand_prob_Cross;						//probabilidad de producirse un cruce
			
			log.info("Engine: reproducePopulation");
			
			//Se eligen los individuos a cruzar
			for (int i=0; i<population_Size; i++) {
				//se generan tam_pob números aleatorios en [0 1)
				rand_prob_Cross = IGARandom.getRDouble();
				//se eligen los individuos de las posiciones i si prob < prob_cruce
				if (rand_prob_Cross < prob_Cross){
					sel_Cross[num_Sel_Cross] = i;
					num_Sel_Cross++;
				}
			}
			log.info("Engine: reproducePopulation1");
			// el numero de seleccionados se hace par
			if ((num_Sel_Cross % 2) == 1)
				num_Sel_Cross--;
			
			// se cruzan los individuos elegidos en un punto al azar
			
			for (int i=0; i<num_Sel_Cross; i+=2){
				GAStudentCromosome[] parents = new GAStudentCromosome[2];
				parents[0] = (GAStudentCromosome) auxiliar_population[sel_Cross[i]];
				parents[1] = (GAStudentCromosome) auxiliar_population[sel_Cross[i+1]];
				log.info("Engine: evaluatePopulationA");
				GAStudentCromosome[] descendientes = (GAStudentCromosome[]) cruzador.cross(parents);
				// los nuevos individuos sustituyen a sus progenitores
				auxiliar_population[sel_Cross[i]] = descendientes[0];
				auxiliar_population[sel_Cross[i+1]] = descendientes[1];
			}
			log.info("Engine: reproducePopulation2");
			
			// si usamos elitismo sustituir a los peores individuos de la población por los hijos
			if (useElitism) {
				@SuppressWarnings("rawtypes")
				class Struct implements Comparable {
					private double aptitud;
					private int possition;
		
					public Struct(double apt, int pos) {
						aptitud = apt;
						possition = pos;
					}
		
					public int compareTo(Object o) {
		
						if (this.aptitud == ((Struct) o).getAptitude())
							return 0;
						else {
							if (this.aptitud < ((Struct) o).getAptitude()) {
								return -1;
							} else
								return 1;
						}
					}
		
					public double getAptitude() {
						return aptitud;
					}
				}
		
				PriorityQueue<Struct> rank = new PriorityQueue<Struct>();
				for (int i=0; i<population_Size;i++){
					rank.add(new Struct(population[i].getEvaluatedValue(),i));
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
			log.info("Engine: reproducePopulation3");
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
	public void setStudentPath(String studentPath) {
		this.studentPath = studentPath;
	}
}
