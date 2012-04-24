package GA;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import GACore.IGACromosome;
import GACore.IGAEngine;

public final class GAStudentsEngine extends IGAEngine {
	private int fun5_N = 8;		// valor n de la Funci�n 5, actualizada via loadConfig
	private ArrayList<GAStudent> studentList;
	private HashMap<Integer, Integer> studentMap;
	
	public void init()
	{
		int id;
		
	/*	// crear funci�n de selecci�n
		if (selectorName.equals("Ruleta"))
			selector = new GARouletteSelection<Boolean>();
		else if (selectorName.equals("Torneo Det"))
			selector = new GATournametSelectionDet<Boolean>();
		else if (selectorName.equals("Torneo Prob"))
			selector = new GATournamentSelectionProb<Boolean>();
		else 
			System.err.println("Error al elegir funci�n de selecci�n");*/
		
		// crear funci�n de evaluaci�n		
		//GANumericEvalFuncs numericEvalFuncts = new GANumericEvalFuncs(fun5_N);
		//id = Character.getNumericValue((functionName.charAt(functionName.length()-1)));
		//evalFunct = numericEvalFuncts.getEvaluatorFunction(id);
				
		// inicializar generaci�n
		current_Generation = 0;
		
		// inicializar array de poblaci�n
		population = new GAStudentCromosome[population_Size]; 
		
		// crear poblaci�n inicial
		for (int i = 0; i < population_Size; i++) {
			population[i] = new GAStudentCromosome(evalFunct);
			((GAStudentCromosome)population[i]).initCromosome(students,incompatibilities);
		}
		
		// asignar un individuo elite inicial
		elite = (GAStudentCromosome)population[0].clone();
		
		//crear el cruzador
		/*if (crossName.equals("Monopunto"))
			cruzador = new GABinaryMonoPointCross();
		else if (crossName.equals("Bipunto"))
			cruzador = new GABinaryBiPointCross();
		else
			System.err.println("Error al crear funci�n de cruce");*/
		
		//crear el mutador
	}
	
	public void loadConfig(String config) {
		// Cargamos la n de la Funci�n 5
		try {
			fun5_N = Integer.parseInt(config);
		} catch (NumberFormatException nfe) {
			fun5_N = 8;
		}
		
		
	}
	
	private void loadStudents(String path)
	{
		try{
		  // Open the file
		  FileInputStream fstream = new FileInputStream(path);
		  DataInputStream in = new DataInputStream(fstream);
		  BufferedReader br = new BufferedReader(new InputStreamReader(in));
		  String strLine, data[];
		  GAStudent student;
		  int numRestrictions;
		  
		  // Read first line: | Num Students | Num restrictions | 
		  strLine = br.readLine();
		  data = strLine.split(" ");
		  System.out.println(data);
		  
		  population_Size = Integer.parseInt(data[0]);
		  numRestrictions = Integer.parseInt(data[1]);
		  
		  // Read Student data: | ID | Result |
		  for (int i=0; i<population_Size; i++) {
			  strLine = br.readLine();
			  data = strLine.split(" ");
			  System.out.println(data);
			  
			  // Create student and load id + result
			  student = new GAStudent(Integer.parseInt(data[0]), Integer.parseInt(data[1]));
			  studentMap.put(student.getId(), students.size());
			  students.add(student);
		  }
		  
		  // Read restrictions:
		  for (int i=0; i<numRestrictions; i++)
		  {
			  strLine = br.readLine();
			  data = strLine.split(" ");
			  
			  student = students.get(studentMap.get(Integer.parseInt(data[0])));
			  student.getHaters().add(Integer.parseInt(data[1]));			  
		  }
		  
		  // Add filler students
		  
		  
		  // Close the input stream
		  in.close();
		    }catch (Exception e){//Catch exception if any
		  System.err.println("Error: " + e.getMessage());
		  }
	}

	public IGACromosome getAbsoluteBest() {
		return elite;
	}

	public IGACromosome getGenerationBest() {
		return generationBest;
	}
}
