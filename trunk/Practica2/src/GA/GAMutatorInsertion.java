package GA;

import GACore.IGAGene;
import GACore.IGAMutator;
import GACore.IGARandom;

public class GAMutatorInsertion extends IGAMutator{

	@Override
	public Boolean mutate(IGAGene gen, double prob) {
		Boolean res = new Boolean(false);
		if(IGARandom.getRDouble()<prob){
			
			int numInserciones = IGARandom.getRInt(gen.getGen().length%5);
			
			for(int i=1;i<=numInserciones;i++){
				int posOrg = IGARandom.getRInt(gen.getGen().length-1)+1;
				int elem = gen.getGen()[posOrg];
				
				int posDst = IGARandom.getRInt(posOrg);
				
				for(int j=posOrg;j>posDst;j--) 
					gen.getGen()[j]=gen.getGen()[j-1];
				
				gen.getGen()[posDst]=elem;
				
			}
			res= !res;
		}
		return res;
	}
}
