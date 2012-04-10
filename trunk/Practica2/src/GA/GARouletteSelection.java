package GA;

import java.lang.reflect.Array;

import GACore.IGACromosome;
import GACore.IGARandom;
import GACore.IGASelector;


public class GARouletteSelection<T> extends IGASelector<T>{
	@SuppressWarnings("unused")
	private Class<T> classofT;
	
	@SuppressWarnings("unchecked")
	public IGACromosome<T>[] select(IGACromosome<T>[] pop, int pop_size) throws InstantiationException, IllegalAccessException {
		int sel_super[];				// seleccionados para sobrevivir
		double prob; 					// probabilidad de seleccion
		int pos_super; 					// posici�n del superviviente
		IGACromosome<T>[] new_pop;		// nueva poblaci�n seleccionada
		
		sel_super = new int[pop_size];
		
		new_pop = (IGACromosome[])Array.newInstance(IGACromosome.class, pop_size);
		for(int i=0; i < pop_size; i++) {
			prob = IGARandom.getRDouble();
			pos_super = 0;
			while ((prob > pop[pos_super].getAcum_Score()) && (pos_super < pop_size)) {
				pos_super++;
				sel_super[i] = pos_super;
			}
		}
		
		// se genera la poblacion intermedia
		for (int j=0; j < pop_size; j++) {
			new_pop[j] = pop[sel_super[j]];
		}
		return new_pop;
	}
}
