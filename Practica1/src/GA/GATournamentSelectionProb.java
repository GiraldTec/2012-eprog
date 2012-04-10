package GA;

import java.lang.reflect.Array;

import GACore.IGACromosome;
import GACore.IGARandom;
import GACore.IGASelector;

public class GATournamentSelectionProb<T> extends IGASelector<T>{
	@SuppressWarnings("unused")
	private Class<T> classofT;
	private double probLoser = 0.8; // probabilidad de tomar el 2º clasificado en vez del 1º
	
	@SuppressWarnings("unchecked")
	public IGACromosome<T>[] select(IGACromosome<T>[] pop, int pop_size) throws InstantiationException, IllegalAccessException {
		int sel_winners[];				// seleccionados para sobrevivir
		int pos_sel1, pos_sel2; 		// individuos a competir
		IGACromosome<T>[] new_pop;		// nueva población seleccionada
		double prob; 					// probabilidad de seleccion
		
		sel_winners = new int[pop_size];
		
		new_pop = (IGACromosome[])Array.newInstance(IGACromosome.class, pop_size);
		for(int i=0; i < pop_size; i++) {
			
			// seleccionamos 2 individuos aleatorios a competir
			pos_sel1 = IGARandom.getRInt(pop_size);
			pos_sel2 = IGARandom.getRInt(pop_size);
			
			// probabilidad de tomar el perdedor
			prob = IGARandom.getRDouble();
			
			// seleccionamos el mejor, a no ser que prob supere probLoser             XOR
			sel_winners[i] = pop[pos_sel1].getAptitude() > pop[pos_sel2].getAptitude() ^ prob > probLoser ? pos_sel1 : pos_sel2;
		}
		
		// se genera la poblacion intermedia
		for (int j=0; j < pop_size; j++) {
			new_pop[j] = pop[sel_winners[j]];
		}
		return new_pop;
	}

}
