package GA;

import GACore.*;

public class GAMutatorInversion extends IGAMutator{

	@Override
	public Boolean mutate(IGAGene gen, double prob) {
		Boolean res = new Boolean(false);
		if(IGARandom.getRDouble()<prob){
			int[] auxGen = gen.getGen().clone();
			int longitud = auxGen.length;
			
			int pointA = IGARandom.getRInt(longitud-1);
			int pointB = IGARandom.getRInt(longitud - pointA)+pointA;
			
			int recorredorA = pointA;
			int recorredorB = pointB;
			
			for(int i=0;i<(pointB-pointA);i++){
				auxGen[i+recorredorA]=gen.getGen()[recorredorB-i-1];
				recorredorA++;
				recorredorB--;
			}
			
			gen.setGen(auxGen);
			res= !res;
		}
		return res;
	}

}
