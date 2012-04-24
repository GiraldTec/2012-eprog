package GA;

import java.util.Random;

import GACore.IGACromosome;
import GACore.IGACross;
import GACore.IGARandom;

public class GAStudentOrdinalCross extends IGACross{

	int longitud;
	
	public IGACromosome[] cross(IGACromosome[] parents) {
		GAStudentCromosome parent1 = (GAStudentCromosome) parents[0];
		GAStudentCromosome parent2 = (GAStudentCromosome) parents[1];
		
		longitud =parent1.getGene().getGen().length;
		int cutPoint = IGARandom.getRInt(longitud);
		
		boolean[] dyList = new boolean[longitud];
		for(int i=0;i<longitud;i++) dyList[i] = true;
		
		
		int[] p1Coded = codify(dyList.clone(),parent1.getGene().getGen());
		int[] p2Coded = codify(dyList.clone(), parent2.getGene().getGen());
		
		
		return null;
	}

	
	public int[] codify(boolean[] dyList,int[] g){
		int[] cdRes = new int[longitud];
		return cdRes;
	}
}
