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
		indexB = IGARandom.getRInt(genLength-1)+1;
		indexA = IGARandom.getRInt(indexB);
		
		int[] genD1, genD2;
		genD1 = new int[genLength];
		genD2 = new int[genLength];
		
		int[] padreGen1= parent1.getGene().getGen();
		int[] padreGen2= parent2.getGene().getGen();
		
		checkear(padreGen1);
		checkear(padreGen2);
		
		//inicializando los genesDescendientes
		for(int i=0;i<genLength;i++){	genD1[i]=genD2[i]=-1;}
		
		//Intercambio de la zona delimitada por los indices
		// Y crear los pares
		
		int[] parejaH1 = new int[genLength];
		int[] parejaH2 = new int[genLength];
		
		for(int i=indexA;i<indexB;i++){
			genD1[i]=padreGen2[i];
			
			genD2[i]=padreGen1[i];
			
			parejaH1[genD1[i]]=genD2[i];
			parejaH2[genD2[i]]=genD1[i];
		}
		
		
		for(int i=0;i<genLength;i++){
			if(i<indexA | i>=indexB){
				//Mirando el hijo1
				if(!pertenece(padreGen1[i],genD1)){
					genD1[i]=padreGen1[i];
				}else{
					genD1[i]=parejaH1[padreGen1[i]];
				}
				//Mirando al hijo2
				if(!pertenece(padreGen2[i],genD2)){
					genD2[i]=padreGen2[i];
				}else{
					genD2[i]=parejaH2[padreGen2[i]];
				}
			}
		}
		
		
		GAStudentGene genP1 = (GAStudentGene) parent1.getGene();
		checkear(genD1);
		checkear(genD2);
		GAStudentGene genP2 = (GAStudentGene) parent2.getGene();
		GAStudentCromosome[] descendientes= new GAStudentCromosome[2];
		descendientes[0]= new GAStudentCromosome();
		descendientes[0].setGene(new GAStudentGene(genD1, genP1.getNumberStudents(), genP1.getGroupSize(), genP1.getResultAverage(), genP1.getAlphaValue()));
		descendientes[1]= new GAStudentCromosome();
		descendientes[1].setGene(new GAStudentGene(genD2, genP2.getNumberStudents(), genP2.getGroupSize(), genP2.getResultAverage(), genP2.getAlphaValue()));
		
		return descendientes;
	}

	public void checkear(int[] gn){
		boolean argu=true;
		int i=0;
		while(i<gn.length & argu){
			argu=pertenece(i, gn);
			i++;
		}
		System.out.print(argu);
	}
	
}
