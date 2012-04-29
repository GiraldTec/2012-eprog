package GA;

import GACore.IGACromosome;
import GACore.IGACross;
import GACore.IGARandom;

public class GAStudentOrderCross extends IGACross {

	@Override
	public IGACromosome[] cross(IGACromosome[] parents) {
		
		GAStudentCromosome parent1 = (GAStudentCromosome) parents[0];
		GAStudentCromosome parent2 = (GAStudentCromosome) parents[1];
		
		int indexA, indexB;
		int genLength = parent1.getGene().getGen().length;
		indexA = IGARandom.getRInt(genLength/2-1);
		indexB = IGARandom.getRInt(genLength/2-1)+(genLength/2);
		
		int[] genD1, genD2;
		genD1 = new int[genLength];
		genD2 = new int[genLength];
		for(int i=0;i<genLength;i++){
			genD1[i]=genD2[i]=-1;
		}
		
		for(int i=indexA;i<indexB;i++){
			genD1[i]=parent2.getGene().getGen()[i];
			genD2[i]=parent1.getGene().getGen()[i];
		}
		int recorredorHijo=indexB;
		int recorredorPadre = indexB;
		while(recorredorHijo!=indexB-1){
			
			if(!pertenece(parent1.getGene().getGen()[recorredorPadre],genD1)){
				genD1[recorredorHijo]=parent1.getGene().getGen()[recorredorPadre];
				recorredorHijo++;
				recorredorHijo = (recorredorHijo % genLength);
			}
			recorredorPadre++;
			recorredorPadre = (recorredorPadre % genLength);
		}
		recorredorHijo=indexB;
		recorredorPadre = indexB;
		while(recorredorHijo!=indexB-1){
			
			if(!pertenece(parent2.getGene().getGen()[recorredorPadre],genD2)){
				genD2[recorredorHijo]=parent2.getGene().getGen()[recorredorPadre];
				recorredorHijo++;
				recorredorHijo = (recorredorHijo % genLength);
			}
			recorredorPadre++;
			recorredorPadre = (recorredorPadre % genLength);
		}
		
		
		
		GAStudentCromosome[] descendientes= new GAStudentCromosome[2];
		descendientes[0]= new GAStudentCromosome();
		descendientes[0].setGene(new GAStudentGene(genD1));
		descendientes[1]= new GAStudentCromosome();
		descendientes[1].setGene(new GAStudentGene(genD2));
		
		// TODO Auto-generated method stub
		return descendientes;
	}

}
