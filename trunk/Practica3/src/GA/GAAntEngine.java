package GA;

import java.util.PriorityQueue;

import practica3.AntBoardManager;
import GACore.IGACromosome;
import GACore.IGAEngine;
import GACore.IGARandom;

public class GAAntEngine extends IGAEngine{
	private int minD=1;
	private int maxD=5;
	private int maxSteps=400;
	private int maxFood=90;
	protected boolean useSimulation=false;
	protected int simulationSpeed=500;
	private GAAntPathEvaluator evaluator;
	private AntBoardManager boardMngr;

	public void init() {
		// crear función de selección
		if (selectorName.equals("Ruleta"))
			selector = new GARouletteSelection();
		else
			System.err.println("Error al elegir la función de selección");
		
		//crear función de cruce
		cruzador = new GAAntPathCross();
		
		//crear función de mutación
		if (mutatorName.equals("Inicial"))
			mutator = new GAAntPathMutatorInitial();
		else if (mutatorName.equals("Operacional"))
			mutator = new GAAntPathMutatorFuncional();
		else if (mutatorName.equals("Terminal"))
			mutator = new GAAntPathMutatorTerminal();
		else
			System.err.println("Error al elegir la función de selección");
		
		//crear función de evaluación
		evaluator = new GAAntPathEvaluator(boardMngr, maxSteps, maxFood, useSimulation, simulationSpeed);
		
		// inicializar generación
		current_Generation = 0;

		// inicializar array de población
		population = new GAAntPathCromosome[population_Size]; 
		
		// crear población inicial
		for (int i = 0; i < population_Size; i++) {
			population[i] = new GAAntPathCromosome();
			((GAAntPathCromosome)population[i]).initCromosome(evaluator, minD, maxD);
		}
		
		// asignar un individuo elite inicial
		elite = (GAAntPathCromosome)population[0].clone();		
		
	}
	
	protected void mutate() {
		log.info("Engine: mutate");
        
        for (int i=0; i < population_Size; i++) {
                //METER EL TIPO DE LA MUTACIÓN Y LOS ESTUDIANTES
                if (mutator.mutate(population[i], prob_Mut)) {
                        population[i].evaluate(false);
                }
                
                log.info("Grupos tras mutación: ");
//                for (Integer j : population[i].getGene().getGen())
//                {
//                        log.info(j + " ");
//                }
//                log.info("\n");
        }
	}

	protected void reproducePopulation() {
		int[] sel_Cross = new int[population_Size];     //seleccionados para reproducir
        int num_Sel_Cross = 0;                                          //contador seleccionados                        
        double rand_prob_Cross;                                         //probabilidad de producirse un cruce
        
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
        
        // el numero de seleccionados se hace par
        if ((num_Sel_Cross % 2) == 1)
                num_Sel_Cross--;
        
        // se cruzan los individuos elegidos en un punto al azar
        
        for (int i=0; i<num_Sel_Cross; i+=2){
        		GAAntPathCromosome[] parents = new GAAntPathCromosome[2];
                parents[0] = (GAAntPathCromosome) auxiliar_population[sel_Cross[i]];
                parents[1] = (GAAntPathCromosome) auxiliar_population[sel_Cross[i+1]];
                GAAntPathCromosome[] descendientes = (GAAntPathCromosome[]) cruzador.cross(parents);
                // los nuevos individuos sustituyen a sus progenitores
                auxiliar_population[sel_Cross[i]] = descendientes[0];
                auxiliar_population[sel_Cross[i]].evaluate(false);
                auxiliar_population[sel_Cross[i+1]] = descendientes[1];
                auxiliar_population[sel_Cross[i+1]].evaluate(false);
        }
        
        // si usamos elitismo sustituir a los peores individuos de la población por los hijos
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
                                        if (this.aptitud > o.getAptitude()) {
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

	public void loadConfig(Object config) {
		boardMngr = (AntBoardManager) config;
	}

	public void evaluateElite(){
		elite.evaluate(true);
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
		if (evaluator != null)
			evaluator.enableSimulation(useSimulation);
	}

	public int getSimulationSpeed() {
		return simulationSpeed;
	}

	public void setSimulationSpeed(int simulationSpeed) {
		this.simulationSpeed = simulationSpeed;
		if (evaluator != null)
			evaluator.setSimulationSpeed(simulationSpeed);
	}
	
	public IGACromosome getAbsoluteBest() {
		return elite;
	}

	public IGACromosome getGenerationBest() {
		return generationBest;
	}

}
