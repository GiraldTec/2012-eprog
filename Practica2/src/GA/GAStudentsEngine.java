package GA;

import GACore.IGACromosome;
import GACore.IGAEngine;

public final class GAStudentsEngine extends IGAEngine {
	private int fun5_N = 8;		// valor n de la Función 5, actualizada via loadConfig 
	
	public void init()
	{
		int id;
		
	/*	// crear función de selección
		if (selectorName.equals("Ruleta"))
			selector = new GARouletteSelection<Boolean>();
		else if (selectorName.equals("Torneo Det"))
			selector = new GATournametSelectionDet<Boolean>();
		else if (selectorName.equals("Torneo Prob"))
			selector = new GATournamentSelectionProb<Boolean>();
		else 
			System.err.println("Error al elegir función de selección");*/
		
		// crear función de evaluación		
		GANumericEvalFuncs numericEvalFuncts = new GANumericEvalFuncs(fun5_N);
		id = Character.getNumericValue((functionName.charAt(functionName.length()-1)));
		evalFunct = numericEvalFuncts.getEvaluatorFunction(id);
				
		// inicializar generación
		current_Generation = 0;
		

		
		// inicializar array de población
		population = new GAStudentCromosome[population_Size]; 
		
		// crear población inicial
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
			System.err.println("Error al crear función de cruce");*/
		
		
		//crear el mutador
	}
	
	public void loadConfig(String config) {
	// Cargamos la n de la Función 5
		try {
			fun5_N = Integer.parseInt(config);
		} catch (NumberFormatException nfe) {
			fun5_N = 8;
		}
	}

	public IGACromosome getAbsoluteBest() {
		return elite;
	}

	public IGACromosome getGenerationBest() {
		return generationBest;
	}
}
