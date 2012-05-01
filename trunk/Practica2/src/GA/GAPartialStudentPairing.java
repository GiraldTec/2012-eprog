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
		
		int aux1 = 0;
		int aux2 = 0;
		//cogemos las que no dan conflicto
		for(int i=0;i<genLength;i++){
			if(i<indexA | i>=indexB){
				//Mirando el hijo1
				if(!pertenece(padreGen1[i],genD1)){ // si no está ya en el gen, lo metemos
					genD1[i]=padreGen1[i];
				}else{
					while(pertenece(padreGen1[aux1],genD1))
						aux1++;
					genD1[i] = padreGen1[aux1];
				}
				//Mirando al hijo2
				if(!pertenece(padreGen2[i],genD2)){
					genD2[i]=padreGen2[i];
				}else{
					while(pertenece(padreGen2[aux2],genD2))
						aux2++;
					genD2[i] = padreGen2[aux2];
				}
			}
		}
		
		GAStudentGene genP1 = (GAStudentGene) parent1.getGene();
		GAStudentGene genP2 = (GAStudentGene) parent2.getGene();
		
		checkear(padreGen1);
		checkear(padreGen2);
		checkear(genD1);
		checkear(genD2);
				
		GAStudentCromosome[] descendientes= new GAStudentCromosome[2];
		descendientes[0]= new GAStudentCromosome();
		descendientes[0].setGene(new GAStudentGene(genD1, genP1.getNumberStudents(), genP1.getGroupSize(), genP1.getResultAverage(), genP1.getAlphaValue()));
		descendientes[1]= new GAStudentCromosome();
		descendientes[1].setGene(new GAStudentGene(genD2, genP2.getNumberStudents(), genP2.getGroupSize(), genP2.getResultAverage(), genP2.getAlphaValue()));
		
		return descendientes;
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
