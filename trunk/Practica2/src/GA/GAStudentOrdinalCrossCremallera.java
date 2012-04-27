package GA;

import GACore.IGACromosome;
import GACore.IGACross;
import GACore.IGARandom;

public class GAStudentOrdinalCrossCremallera extends IGACross{

	int longitud;
	
	public IGACromosome[] cross(IGACromosome[] parents) {
		GAStudentCromosome parent1 = (GAStudentCromosome) parents[0];
		GAStudentCromosome parent2 = (GAStudentCromosome) parents[1];
		
		longitud =parent1.getGene().getGen().length;		
		
		int[] p1Coded = codify(parent1.getGene().getGen());
		int[] p2Coded = codify(parent2.getGene().getGen());
		
		int[] aux1 = new int[longitud];
		int[] aux2 = new int[longitud];
		
		for(int i=0;i<longitud;i++){
			if(i%2==0){
				aux1[i]=p1Coded[i];
				aux2[i]=p2Coded[i];
			}else{
				aux1[i]=p2Coded[i];
				aux2[i]=p1Coded[i];
			}			
		}
		
		int[] genD1 = deCode(aux1);
		int[] genD2 = deCode(aux2);
		
		GAStudentCromosome[] descendientes= new GAStudentCromosome[2];
		descendientes[0]= new GAStudentCromosome();
		descendientes[0].setGene(new GAStudentGene(genD1));
		descendientes[1]= new GAStudentCromosome();
		descendientes[1].setGene(new GAStudentGene(genD2));
		
		// TODO Auto-generated method stub
		return descendientes;
	}

	
	public int[] codify(int[] g){
		int[] cdRes = new int[longitud];
		
		boolean[] dyList = new boolean[longitud];
		for(int i=0;i<longitud;i++) dyList[i] = true;
		
		// Hay que buscar el "índice" relativo del elemento g[i] en la lista de booleanos
		// y asi obtener el valor codificado de su posición
		for (int i = 0; i<longitud;i++){
			cdRes[i]= getIndexOfElem(g[i],dyList);
		}
		
		return cdRes;
	}
	
	public int getIndexOfElem(int elemento, boolean[] lista){
		boolean encontrado=false;
		int index =0;
		int recorredor=0;
		while (!encontrado && recorredor <longitud){	if(index==elemento){
									encontrado=true;
									lista[index]=false;
								}else{	if(lista[recorredor]) 
											index++;
											recorredor++;
											}}
		return index;
	}
	
	public int getElemOfIndex(int indice, boolean[] lista){
		boolean encontrado=false;
		int index =0;
		int elemento=0;
		while (!encontrado && elemento <longitud){	if(index==indice){
									encontrado=true;
									lista[index]=false;
								}else{	if(lista[elemento]) 
											index++;
											elemento++;
											}}
		return elemento;
	}
	
	public int[] deCode(int[] g){
		
		int[] cdRes = new int[longitud];
		
		boolean[] dyList = new boolean[longitud];
		for(int i=0;i<longitud;i++) dyList[i] = true;
		
		for (int i = 0; i<longitud;i++){
			cdRes[i]= getElemOfIndex(g[i],dyList);
		}
		
		return cdRes;
	}
}