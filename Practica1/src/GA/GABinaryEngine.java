package GA;

import java.lang.reflect.Array;

import GACore.IGAEngine;

public final class GABinaryEngine extends IGAEngine<Boolean> {
	private int fun5_N = 8;		// valor n de la Funci�n 5, actualizada via loadConfig 
	
	public void init()
	{
		int id;
		
		// crear funci�n de selecci�n
		if (selectorName.equals("Ruleta"))
			selector = new GARouletteSelection<Boolean>();
		else if (selectorName.equals("Torneo Det"))
			selector = new GATournametSelectionDet<Boolean>();
		else if (selectorName.equals("Torneo Prob"))
			selector = new GATournamentSelectionProb<Boolean>();
		else 
			System.err.println("Error al elegir funci�n de selecci�n");
		
		// crear funci�n de evaluaci�n		
		GANumericEvalFuncs numericEvalFuncts = new GANumericEvalFuncs(fun5_N);
		id = Character.getNumericValue((functionName.charAt(functionName.length()-1)));
		evalFunct = numericEvalFuncts.getEvaluatorFunction(id);
				
		// inicializar generaci�n
		current_Generation = 0;
		
		// calcular tama�o del cromosoma binario segun la precisi�n
		cromosome_Lenght = evalFunct.calcCromLenght(precision);
		
		// inicializar array de poblaci�n
		population = (GABinaryCromosome[])Array.newInstance(GABinaryCromosome.class, population_Size);
		
		// crear poblaci�n inicial
		for (int i = 0; i < population_Size; i++) {
			population[i] = new GABinaryCromosome(evalFunct);
			((GABinaryCromosome)population[i]).initCromosome(cromosome_Lenght);
		}
		
		// asignar un individuo elite inicial
		elite = (GABinaryCromosome)population[0].clone();
		
		//crear el cruzador
		if (crossName.equals("Monopunto"))
			cruzador = new GABinaryMonoPointCross();
		else if (crossName.equals("Bipunto"))
			cruzador = new GABinaryBiPointCross();
		else
			System.err.println("Error al crear funci�n de cruce");
	}
	
	public void loadConfig(String config) {
	// Cargamos la n de la Funci�n 5
		try {
			fun5_N = Integer.parseInt(config);
		} catch (NumberFormatException nfe) {
			fun5_N = 8;
		}
	}

	public Double getAbsoluteBest() {
		return elite.getAptitude();
	}

	public Double getGenerationBest() {
		return generationBest.getAptitude();
	}
}
