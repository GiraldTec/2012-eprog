package GA;

import GACore.IGACromosome;
import GACore.IGACross;
import GACore.IGARandom;

public class GAPartialStudentPairing extends IGACross{

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
		
		int[][] conflictos = new int[2][genLength];
		for(int i=0;i<2;i++)
			for(int j=0;j<genLength;j++)
				conflictos[i][j]=-1;
		
		
		for(int i=indexA;i<=indexB;i++){
			genD1[i]=parent2.getGene().getGen()[i];
			genD2[i]=parent1.getGene().getGen()[i];
			conflictos[0][i]= genD1[i];
			conflictos[1][i]= genD2[i];
		}
		
		for(int i=0;i<genLength;i++){
			if(i>=indexA && i<=indexB){
				if(!pertenece(parent1.getGene().getGen()[i],conflictos[0]))
					genD1[i]=parent1.getGene().getGen()[i];
				if(!pertenece(parent2.getGene().getGen()[i],conflictos[1]))
					genD2[i]=parent2.getGene().getGen()[i];				
			}
		}
		
		for(int i=0;i<genLength;i++){
			if(i>=indexA && i<=indexB){
				if(pertenece(parent1.getGene().getGen()[i],genD1))
					genD1[i]=conflictos[1][getIndex(parent1.getGene().getGen()[i], genD1)];
				if(pertenece(parent2.getGene().getGen()[i],genD2))
					genD2[i]=conflictos[0][getIndex(parent2.getGene().getGen()[i],genD2)];
			}
		}
			
		GAStudentGene genP1 = (GAStudentGene) parent1.getGene();
		GAStudentGene genP2 = (GAStudentGene) parent2.getGene();
		GAStudentCromosome[] descendientes= new GAStudentCromosome[2];
		descendientes[0]= new GAStudentCromosome();
		descendientes[0].setGene(new GAStudentGene(genD1, genP1.getNumberStudents(), genP1.getGroupSize(), genP1.getResultAverage(), genP1.getAlphaValue()));
		descendientes[1]= new GAStudentCromosome();
		descendientes[1].setGene(new GAStudentGene(genD2, genP2.getNumberStudents(), genP2.getGroupSize(), genP2.getResultAverage(), genP2.getAlphaValue()));
		
		return descendientes;
	}

	
}
