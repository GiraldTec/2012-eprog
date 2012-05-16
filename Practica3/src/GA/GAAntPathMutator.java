package GA;

import GACore.*;

public class GAAntPathMutator extends IGAMutator{

	@Override
	public Boolean mutate(IGAGene gen, double prob) {
		System.out.print("entraamos a mutar");
		Boolean res = new Boolean(false);
		//if(IGARandom.getRDouble()<prob){
			int[] auxGen = gen.getGen().clone();
			int longitud = auxGen.length;
			
			int pointB  = IGARandom.getRInt(longitud-1)+1;
			int pointA = IGARandom.getRInt(pointB);
			
			int recorredorA = pointA;
			
			
			for(int i=pointB-1;i>=pointA;i--){
				auxGen[i]=gen.getGen()[recorredorA];
				recorredorA++;
				
			}
			
			gen.setGen(auxGen);
			res= !res;
//		}
		return res;
	}

}
