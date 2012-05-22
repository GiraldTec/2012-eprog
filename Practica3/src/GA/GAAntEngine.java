package GA;

import practica3.AntBoardManager;
import GACore.IGACromosome;
import GACore.IGAEngine;

public class GAAntEngine extends IGAEngine{
	private int minD=5;
	private int maxD=15;
	private int maxSteps=400;
	private int maxFood=90;
	protected boolean useSimulation=false;
	protected int simulationSpeed=500;
	private GAAntPathEvaluator evaluator;
	private AntBoardManager boardMngr;

	public void init() {
		// crear funci�n de selecci�n
		if (selectorName.equals("Ruleta"))
			selector = new GARouletteSelection();
		else
			System.err.println("Error al elegir la funci�n de selecci�n");
		
		//crear funci�n de cruce
		cruzador = new GAAntPathCross();
		
		//crear funci�n de mutaci�n
		mutador = new GAAntPathMutatorTerminal();	
		
		//crear funci�n de evaluaci�n
		evaluator = new GAAntPathEvaluator(boardMngr, maxSteps, maxFood);
		
		// inicializar generaci�n
		current_Generation = 0;

		// inicializar array de poblaci�n
		population = new GAAntPathCromosome[population_Size]; 
		
		// crear poblaci�n inicial
		for (int i = 0; i < population_Size; i++) {
			population[i] = new GAAntPathCromosome();
			((GAAntPathCromosome)population[i]).initCromosome(evaluator, 2, 5);
		}
		
		// asignar un individuo elite inicial
		elite = (GAAntPathCromosome)population[0].clone();		
		
	}
	
	protected void mutate() {
		// TODO Auto-generated method stub
	}

	protected void reproducePopulation() {
		// TODO Auto-generated method stub
	}

	public void loadConfig(Object config) {
		boardMngr = (AntBoardManager) config;
	}

	public IGACromosome getAbsoluteBest() {
		return elite;
	}

	public IGACromosome getGenerationBest() {
		return generationBest;
	}
	
	
	/*----------- Getters & Setters -------- ***/
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
	public int getMaxSteps() {
		return maxSteps;
	}

	public void setMaxSteps(int maxSteps) {
		this.maxSteps = maxSteps;
	}

	public int getMaxFood() {
		return maxFood;
	}

	public void setMaxFood(int maxFood) {
		this.maxFood = maxFood;
	}
	public boolean isUseSimulation() {
		return useSimulation;
	}

	public void setUseSimulation(boolean useSimulation) {
		this.useSimulation = useSimulation;
		evaluator.enableSimulation(useSimulation);
	}

	public int getSimulationSpeed() {
		return simulationSpeed;
	}

	public void setSimulationSpeed(int simulationSpeed) {
		this.simulationSpeed = simulationSpeed;
		evaluator.setSimulationSpeed(simulationSpeed);
	}


}
