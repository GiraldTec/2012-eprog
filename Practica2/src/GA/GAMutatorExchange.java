package GA;

import GACore.IGAGene;
import GACore.IGAMutator;
import GACore.IGARandom;

public class GAMutatorExchange extends IGAMutator {
	public Boolean mutate(IGAGene gen, double prob) {
		Boolean res = new Boolean(false);
		if(IGARandom.getRDouble()<prob){
			// Solo con dos numeros aleatorios
			int auxB = IGARandom.getRInt(gen.getGen().length - 1)+1;
			int auxA = IGARandom.getRInt(auxB);
			
			
			int auxiliar = gen.getGen()[auxA];
			gen.getGen()[auxA]= gen.getGen()[auxB];
			gen.getGen()[auxB]=auxiliar;
			
			res= !res;
		}
		return res;
	}
}
