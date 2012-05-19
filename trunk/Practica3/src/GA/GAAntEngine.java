package GA;

import GACore.IGACromosome;
import GACore.IGAEngine;

public class GAAntEngine extends IGAEngine{
	private int minD=5;
	private int maxD=15;

	public void init() {
		// crear funci�n de selecci�n
		if (selectorName.equals("Ruleta"))
			selector = new GARouletteSelection();
		else
			System.err.println("Error al elegir la funci�n de selecci�n");
		
		// inicializar generaci�n
		current_Generation = 0;

		// inicializar array de poblaci�n
		population = new GAAntPathCromosome[population_Size]; 
		
		// crear poblaci�n inicial
		for (int i = 0; i < population_Size; i++) {
			population[i] = new GAAntPathCromosome();
			((GAAntPathCromosome)population[i]).initCromosome(i, i);
		}
		
		// asignar un individuo elite inicial
		elite = (GAAntPathCromosome)population[0].clone();

		//crear el cruzador
		cruzador = new GAAntPathCross();
		
		//crear el mutador
		mutador = new GAAntPathMutatorTerminal();	
	}
	
	protected void mutate() {
		// TODO Auto-generated method stub
	}

	protected void reproducePopulation() {
		// TODO Auto-generated method stub
	}

	public void loadConfig(String config) {
	}

	public IGACromosome getAbsoluteBest() {
		return elite;
	}

	public IGACromosome getGenerationBest() {
		return generationBest;
	}
	
	public void setMinD(int minD) {
		this.minD = minD;
	}

	public int getMinD() {
		return minD;
	}

	public void setMaxD(int maxD) {
		this.maxD = maxD;
	}

	public int getMaxD() {
		return maxD;
	}

}
