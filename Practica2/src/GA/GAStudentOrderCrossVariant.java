package GA;

import GACore.IGACromosome;
import GACore.IGACross;
import GACore.IGARandom;

// Variación OX : Posiciones Prioritarias
public class GAStudentOrderCrossVariant extends IGACross {

	@Override
	public IGACromosome[] cross(IGACromosome[] parents) {
		
		GAStudentCromosome parent1 = (GAStudentCromosome) parents[0];
		GAStudentCromosome parent2 = (GAStudentCromosome) parents[1];
		
		int numPos, indexB;
		int genLength = parent1.getGene().getGen().length;
		numPos = IGARandom.getRInt(genLength/2)+1;
		
		int[] vectorPos = new int[numPos];
		
		for(int i=0;i<numPos;i++){
			int newPos= IGARandom.getRInt(genLength);
			while(pertenece(newPos, vectorPos)) newPos= IGARandom.getRInt(genLength);
			vectorPos[i]= newPos;
		}
		
		indexB = getMax(vectorPos);
		
		int[] genD1, genD2;
		genD1 = new int[genLength];
		genD2 = new int[genLength];
		
		for(int i=0;i<genLength;i++){
			genD1[i]=genD2[i]=-1;
		}
		
		int[] padreGen1 = parent1.getGene().getGen();
		int[] padreGen2 = parent2.getGene().getGen();
		
		for(int i=0;i<numPos;i++){
			int pos=vectorPos[i];
			genD1[pos]=padreGen2[pos];
			genD2[pos]=padreGen1[pos];
		}
		
		int i= indexB +1;
		while(i!=indexB){
			i%=genLength;
			if(genD1[i]==-1){
				int aux=i;
				while(pertenece(padreGen1[aux],genD1)){
					aux++;
					aux%=genLength;
				}
				genD1[i]=padreGen1[aux];
			}
			i++;
		}
		
		int j= indexB +1;
		while(j!=indexB){
			j%=genLength;
			if(genD2[j]==-1){
				int aux=j;
				while(pertenece(padreGen2[aux],genD2)){
					aux++;
					aux%=genLength;
				}
				genD2[j]=padreGen2[aux];
			}
			j++;
		}
		
		checkear(padreGen1);
		checkear(padreGen2);
		checkear(genD1);
		checkear(genD2);
		
		GAStudentGene genP1 = (GAStudentGene) parent1.getGene();
		GAStudentGene genP2 = (GAStudentGene) parent2.getGene();
		GAStudentCromosome[] descendientes= new GAStudentCromosome[2];
		descendientes[0]= new GAStudentCromosome();
		descendientes[0].setGene(new GAStudentGene(genD1, genP1.getNumberStudents(), genP1.getGroupSize(), genP1.getResultAverage(), genP1.getAlphaValue()));
		descendientes[1]= new GAStudentCromosome();
		descendientes[1].setGene(new GAStudentGene(genD2, genP2.getNumberStudents(), genP2.getGroupSize(), genP2.getResultAverage(), genP2.getAlphaValue()));
		
		return descendientes;
	}

	
	public int getMax(int[] v){
		int max= -1;
		for(int i=0;i<v.length;i++){
			if(i==0) max = v[i];
			else{
				if(v[i]>max) max = v[i];
			}
		}
		return max;
	}
	
	public static void checkear(int[] gn){
		int[] aux = gn.clone();
		for(int i=0;i<gn.length;i++){
			int contador =0;
			
			while(IGACross.pertenece(i, aux)){
				contador++;
				aux[IGACross.getIndex(i, aux)]=-1;
			}
			
			if(contador==0){
				System.out.println(i+" no sale");
			}
			if(contador>1){
				System.out.println(i+" sale repetido");				
			}
			
		}
	}
}
