package GA;

import java.util.Arrays;
import java.util.Collections;

import GACore.IGACromosome;
import GACore.IGARandom;
import GACore.IGASelector;

public class GAShuffleSelection extends IGASelector{

	public IGACromosome[] select(IGACromosome[] pop, int pop_size)
			throws InstantiationException, IllegalAccessException {
		IGACromosome[] new_pop = new IGACromosome[pop_size];	
		
		for(int i=0;i<pop_size;i++){
			new_pop[pop_size-i]= pop[i];
		}
		
		int nBarajados = IGARandom.getRInt(10);
		for(int i= 0;i<nBarajados;i++) 
			Collections.shuffle(Arrays.asList(new_pop));
		
		return new_pop;

	}

}
