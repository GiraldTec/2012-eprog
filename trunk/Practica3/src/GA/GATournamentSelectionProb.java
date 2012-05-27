package GA;

import java.lang.reflect.Array;

import GACore.*;

public class GATournamentSelectionProb extends IGASelector{
    private double probLoser = 0.8; // probabilidad de tomar el 2º clasificado en vez del 1º
    
    public IGACromosome[] select(IGACromosome[] pop, int pop_size) throws InstantiationException, IllegalAccessException {
            int sel_winners[];                              // seleccionados para sobrevivir
            int pos_sel1, pos_sel2;                 // individuos a competir
            IGACromosome[] new_pop;         // nueva población seleccionada
            double prob;                                    // probabilidad de seleccion
            
            sel_winners = new int[pop_size];
            
            new_pop = (IGACromosome[])Array.newInstance(IGACromosome.class, pop_size);
            for(int i=0; i < pop_size; i++) {
                    
                    // seleccionamos 2 individuos aleatorios a competir
                    pos_sel1 = IGARandom.getRInt(pop_size);
                    pos_sel2 = IGARandom.getRInt(pop_size);
                    
                    // probabilidad de tomar el perdedor
                    prob = IGARandom.getRDouble();
                    
                    // seleccionamos el mejor, a no ser que prob supere probLoser             XOR
                    sel_winners[i] = pop[pos_sel1].getEvaluatedValue() > pop[pos_sel2].getEvaluatedValue() ^ prob > probLoser ? pos_sel1 : pos_sel2;
            }
            
            // se genera la poblacion intermedia
            for (int j=0; j < pop_size; j++) {
                    new_pop[j] = pop[sel_winners[j]];
            }
            return new_pop;
    }
    
    public void loadSelectorConf(double selecParams){
            probLoser = selecParams;                
    }

}