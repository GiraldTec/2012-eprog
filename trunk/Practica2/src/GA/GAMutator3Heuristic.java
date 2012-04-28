package GA;

import GACore.IGAGene;
import GACore.IGAMutator;
import GACore.IGARandom;


public class GAMutator3Heuristic extends IGAMutator{

	@Override
	public Boolean mutate(IGAGene gen, double prob) {
		Boolean res = new Boolean(false);
		if(IGARandom.getRDouble()<prob){
			
			
			
			res= !res;
		}
		return res;
	}

}
