package GACore;

import java.lang.reflect.Array;
import java.util.PriorityQueue;
import java.util.logging.Logger;

public abstract class IGAEngine<T> {
	protected IGACromosome<T>[] population; 		// población
	protected IGACromosome<T>[] auxiliar_population; // población auxiliar(para la selección)
	protected IGACromosome<T> generationBest;		// mejor especimen de la generación actual
	protected IGACromosome<T> elite; 	// mejor individuo de todas las generaciones
	protected int population_Size;		// tamaño población
	protected int num_Max_Gen; 			// número máximo de generaciones
	protected int pos_Best; 			// posición del mejor cromosoma
	protected double population_Average;// media de la aptitud de la población
	protected double prob_Cross=0.5;	// probabilidad de cruce
	protected double prob_Mut=0.2;		// probabilidad de mutación
	protected double precision=0.01;	// tolerancia de la representación
	protected int cromosome_Lenght;		// tamaño del cromosoma
	protected boolean evol_Complete;	// indica si se ha alcanzado el objetivo
	protected int current_Generation;	// generación en la que estamos
	protected IGASelector<T> selector;	// método de selección
	protected IGAEvalFunction evalFunct;// función de evaluación
	protected IGAMonoPointCross<T> cruzador;
	
	protected String functionName; 		// funcion de evaluación seleccionada en GUI
	protected String selectorName; 		// funcion de selección seleccionada en GUI
	protected String crossName; 		// funcion de cruce seleccionada en GUI
	protected boolean useElitism=true;  // si usamos elitismo o no (via GUI)
	public static Logger log = Logger.getLogger("Engine");
	
	public abstract void init();
	public abstract void loadConfig(String config);	// carga configuración adicional (si es necesario)
	public abstract Object getAbsoluteBest();		// devuelve el mejor resultado obtenido hasta el momento
	public abstract Object getGenerationBest();		// devuelve el mejor resultado de la generación actual
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void evaluatePopulation()	{
		double acum_Score = 0; 		// puntuación acumulada
		double best_Aptitude = 0; 	// mejor aptitud
		double sum_Aptitude = 0;	// suma de la aptitud
		
		log.info("Engine: evaluatePopulation");
		
		if(evalFunct.getType()==1 || evalFunct.getType()==2){
			for (int i=0; i<population_Size; i++) {
				sum_Aptitude = sum_Aptitude + population[i].getAptitude();	
				if (population[i].getAptitude() > best_Aptitude){
					pos_Best = i;
					best_Aptitude = population[i].getAptitude();
					if(best_Aptitude > elite.getAptitude())
						elite = (IGACromosome)population[i].clone();
				}
			}
			//sum_Aptitude += population_Size*best_Aptitude;
			population_Average = sum_Aptitude/population_Size;
			
			for (int i=0; i<population_Size; i++) {
				//double aptitud_revised = population[i].getAptitude()+best_Aptitude;
				population[i].setScore(population[i].getAptitude() / sum_Aptitude);
				population[i].setAcum_Score(population[i].getScore() + acum_Score);
				acum_Score = acum_Score + population[i].getScore();
				log.info("Cromosome "+i+": "+"Aptitud="+population[i].getAptitude()+" | Score="+population[i].getScore()+
						" | Fenotype="+population[i].getFenotype(0));
			}
		}
		
		if(evalFunct.getType()==3||evalFunct.getType()==4||evalFunct.getType()==5){
			for (int i=0; i<population_Size; i++) {
				sum_Aptitude = sum_Aptitude + population[i].getAptitude();	
				if (population[i].getAptitude() < best_Aptitude){
					pos_Best = i;
					best_Aptitude = population[i].getAptitude();
					if(best_Aptitude < elite.getAptitude())
						elite = (IGACromosome)population[i].clone();
				}
			}
			//sum_Aptitude += population_Size*best_Aptitude;
			population_Average = sum_Aptitude/population_Size;
			
			for (int i=0; i<population_Size; i++) {
				//double aptitud_revised = population[i].getAptitude()+best_Aptitude;
				population[i].setScore(population[i].getAptitude() / sum_Aptitude);
				population[i].setAcum_Score(population[i].getScore() + acum_Score);
				acum_Score = acum_Score + population[i].getScore();
				log.info("Cromosome "+i+": "+"Aptitud="+population[i].getAptitude()+" | Score="+population[i].getScore()+
						" | FenotypeX="+population[i].getFenotype(0)) ;
			}
		}
	}
	
	@SuppressWarnings("unchecked")
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
			IGACromosome<T>[] parents = (IGACromosome[])Array.newInstance(IGACromosome.class, 2);
			parents[0] = auxiliar_population[sel_Cross[i]];
			parents[1] = auxiliar_population[sel_Cross[i+1]];
			log.info("Engine: evaluatePopulationA");
			IGACromosome<T>[] descendientes = cruzador.cross(parents);
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
							return 1;
						} else
							return -1;
					}
				}
	
				public double getAptitude() {
					return aptitud;
				}
			}
	
			PriorityQueue<Struct> rank = new PriorityQueue<Struct>();
			for (int i=0; i<population_Size;i++){
				rank.add(new Struct(population[i].aptitude,i));
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
	
	protected void selectPopulation() throws InstantiationException, IllegalAccessException
	{
		log.info("Engine: selectPopulation");
		
		auxiliar_population = selector.select(population, population_Size);
	}
	
	protected void mutate(){
		boolean hasMutated;

		log.info("Engine: mutate");
		
		for (int i=0; i < population_Size; i++) {
			hasMutated = false;
			for (int j=0; j < ((IGAEvalFunctionNum)evalFunct).getNumVars() ; j++) {
				// se genera un numero aleatorio en [0 1)
				hasMutated = hasMutated || population[i].mutateGen(j,prob_Mut);
			}
			if (hasMutated) {
				population[i].calcFenotype();
				population[i].evaluate();
			}
		}
	}
	
	public void runEvolutionStep() throws InstantiationException, IllegalAccessException {
		log.info("Engine: runEvolutionStep");
		
		evaluatePopulation();	//evalúa los individuos y coge el mejor
		selectPopulation();
		reproducePopulation();
		mutate();
		evaluatePopulation();
		generationBest = population[pos_Best];
		
		log.info("Generation -> "+current_Generation+" <- Results");
		log.info("Generation best: "+generationBest.getAptitude()+" ref pointer "+generationBest);
		log.info("Elite: "+elite.getAptitude()+" ref pointer "+elite);
		current_Generation++;
	}
	
	
//---- Getters & Setters ------------------------------------------------------//
	
	public int getPopulation_Size() {
		return population_Size;
	}
	public void setPopulation_Size(int population_Size) {
		this.population_Size = population_Size;
	}
	public String getFunctionName() {
		return functionName;
	}
	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}	
	public double getPrecision() {
		return precision;
	}
	public void setPrecision(double precision) {
		this.precision = precision;
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
	public String getCrossName() {
		return crossName;
	}
	public void setCrossName(String crossName) {
		this.crossName = crossName;
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
}
