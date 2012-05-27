package GA;

import java.util.*;

import GACore.*;

public class GAShuffleSelection extends IGASelector{

    public GAAntPathCromosome[] select(IGACromosome[] pop, int pop_size)
                    throws InstantiationException, IllegalAccessException {
    	GAAntPathCromosome[] new_pop = new GAAntPathCromosome[pop_size];    
            
            for(int i=0;i<pop_size;i++){
                    new_pop[pop_size-i-1]= (GAAntPathCromosome) pop[i];
            }
            
            int nBarajados = IGARandom.getRInt(10);
            for(int i= 0;i<nBarajados;i++) 
                    Collections.shuffle(Arrays.asList(new_pop));
            
            return new_pop;

    }

}
