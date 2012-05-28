package GACore;
import java.util.logging.Logger;

import GA.GAAntPathCromosome;

public abstract class IGAEngine {
	protected IGACromosome[] population; 		// población
	protected IGACromosome[] auxiliar_population; // población auxiliar(para la selección)
	protected IGACromosome generationBest;		// mejor especimen de la generación actual
	protected IGACromosome elite; 	// mejor individuo de todas las generaciones
	protected int population_Size=100;		// tamaño población
	protected int num_Max_Gen=100; 			// número máximo de generaciones
	protected int pos_Best; 			// posición del mejor cromosoma
	protected double population_Average;// media de la aptitud de la población
	protected double prob_Cross=0.65;	// probabilidad de cruce
	protected double prob_Mut=0.55;		// probabilidad de mutación
	protected boolean evol_Complete;	// indica si se ha alcanzado el objetivo
	protected int current_Generation;	// generación en la que estamos
	protected IGASelector selector;		// método de selección
	protected IGACross cruzador;		// método de cruzado
	protected IGAMutator mutator; 		// método de mutación
	
	protected String selectorName; 		// funcion de selección seleccionada en GUI
	protected String mutatorName; 		// funcion de mutación seleccionada en GUI
	protected boolean useElitism=true;  // si usamos elitismo o no (via GUI)
	
	protected double selecParams=1.0;
	
	public static Logger log = Logger.getLogger("Engine");
	
	public abstract void init();
	public abstract void loadConfig(Object config);	// carga configuración adicional (si es necesario)
	public abstract IGACromosome getAbsoluteBest();		// devuelve el mejor resultado obtenido hasta el momento
	public abstract IGACromosome getGenerationBest();		// devuelve el mejor resultado de la generación actual
	
	protected void evaluatePopulation(boolean sim)	{
		double acum_Score = 0; 		// puntuación acumulada
		double best_EvaluatedValue = 0; 	// mejor aptitud
		double sum_EvaluatedValue = 0;	// suma de la aptitud
		
		log.info("Engine: evaluatePopulation");	
		
		for (int i=0; i<population_Size; i++) {
			population[i].evaluate(sim);
			sum_EvaluatedValue = sum_EvaluatedValue + population[i].getEvaluatedValue();	
			if (population[i].getEvaluatedValue() > best_EvaluatedValue){
				pos_Best = i;
				best_EvaluatedValue = population[i].getEvaluatedValue();
				if(best_EvaluatedValue > elite.getEvaluatedValue())
					elite = (IGACromosome)population[i].clone();
			}
		}
		
		population_Average = sum_EvaluatedValue/population_Size;
		
		for (int i=0; i<population_Size; i++) {
			population[i].setScore(population[i].getEvaluatedValue() / sum_EvaluatedValue);
			population[i].setAcum_Score(population[i].getScore() + acum_Score);
			acum_Score = acum_Score + population[i].getScore();
			log.info("Cromosome "+i+" EvaluatedValue: "+population[i].getEvaluatedValue()+" | Score: "+population[i].getScore()) ;
		}
	}

	protected abstract void mutate();
	
	protected abstract void reproducePopulation();
	
	protected void selectPopulation() throws InstantiationException, IllegalAccessException
	{
		log.info("Engine: selectPopulation");
		
		auxiliar_population = selector.select(population, population_Size);
	}

	public void runEvolutionStep() throws InstantiationException, IllegalAccessException {
		log.info("Engine: runEvolutionStep");
		
		evaluatePopulation(false);	//evalúa los individuos y coge el mejor
		selectPopulation();  // selecciona los que van a cruce (permite repetidos)
		reproducePopulation(); // cruza segun la probabilidad entre los seleccionados
		mutate(); // suplanta segun la probabilidad
		evaluatePopulation(false);
		generationBest = population[pos_Best];
		
		log.info("Generation -> "+current_Generation+" <- Results");
		log.info("Generation best: "+generationBest.getEvaluatedValue()+" ref pointer "+generationBest);
		log.info("Elite: "+elite.getEvaluatedValue()+" ref pointer "+elite);
		current_Generation++;
	}
	
	public void porPantalla(){
			GAAntPathCromosome.porPantalla(((GAAntPathCromosome)elite).getTreeP());
			System.out.println("");
	}
//---- Getters & Setters ------------------------------------------------------//
	
	public int getPopulation_Size() {
		return population_Size;
	}
	public void setPopulation_Size(int population_Size) {
		this.population_Size = population_Size;
	}
	public int getNum_Max_Gen() {
		return num_Max_Gen;
	}
	public void setNum_Max_Gen(int num_Max_Gen) {
		this.num_Max_Gen = num_Max_Gen;
	}
	public String getSelectorName() {
		return selectorName;
	}
	public void setSelectorName(String selectorName) {
		this.selectorName = selectorName;
	}
	public double getProb_Cross() {
		return prob_Cross;
	}
	public void setProb_Cross(double prob_Cross) {
		this.prob_Cross = prob_Cross;
	}
	public double getProb_Mut() {
		return prob_Mut;
	}
	public void setProb_Mut(double prob_Mut) {
		this.prob_Mut = prob_Mut;
	}
	public int getCurrent_Generation() {
		return current_Generation;
	}
	public boolean isEvol_Complete() {
		return evol_Complete;
	}
	public double getPopulation_Average() {
		return population_Average;
	}
	public boolean getUseElitism() {
		return useElitism;
	}
	public void setUseElitism(boolean useElitism) {
		this.useElitism = useElitism;
	}
	public double getSelecParams() {
		return selecParams;
	}
	public void setSelecParams(double selecParams) {
		this.selecParams = selecParams;
	}
	public String getMutatorName() {
		return mutatorName;
	}
	public void setMutatorName(String mutatorName) {
		this.mutatorName = mutatorName;
	}
}
