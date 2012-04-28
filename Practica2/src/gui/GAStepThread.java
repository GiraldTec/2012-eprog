package gui;

import javax.swing.SwingWorker;

import GACore.IGAEngine;

/***
 * 
 * Esta clase existe con fin de no bloquear la interfaz en previsión a problemas mucho más complejos 
 * en los que un step de la evolución requiera un tiempo de procesamiento considerable
 *
 */

class GAStepThread extends SwingWorker<Void, Void> {
	IGAEngine gaEngine;
	double dataAbsoluteBest[];
	double dataGenerationBest[];
	double dataGenerationAverage[];
	double dataGenerationCount[];
	int progress;
	
	public GAStepThread (IGAEngine gaEngine, Object dataAbsoluteBest2, Object dataGenerationAverage2, Object dataGenerationBest2, Object dataGenerationCount2) {
		this.gaEngine = gaEngine;
		this.dataAbsoluteBest = (double[])dataAbsoluteBest2;
		this.dataGenerationAverage = (double[])dataGenerationBest2;
		this.dataGenerationBest = (double[])dataGenerationAverage2;
		this.dataGenerationCount = (double[])dataGenerationCount2;
	}
	
	// Main task. Executed in background thread.
    public Void doInBackground() {
    	int currGeneration = gaEngine.getCurrent_Generation();
    	progress = (currGeneration * 100) / gaEngine.getNum_Max_Gen();
        //Initialize progress property.
        setProgress(progress);
            while (!gaEngine.isEvol_Complete() && currGeneration < gaEngine.getNum_Max_Gen()){
            	try {
            		// ejecutamos un step de la evolución
					gaEngine.runEvolutionStep();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}		
				// actualizar resultados
				dataAbsoluteBest[currGeneration] = gaEngine.getAbsoluteBest().getEvaluatedValue();
				dataGenerationBest[currGeneration] = gaEngine.getGenerationBest().getEvaluatedValue();
				dataGenerationAverage[currGeneration] = gaEngine.getPopulation_Average();
				
				dataGenerationCount[currGeneration] = currGeneration;
				currGeneration = gaEngine.getCurrent_Generation();
				
				setProgress((currGeneration * 100) / gaEngine.getNum_Max_Gen());
			 }
			return null;
    }
}
