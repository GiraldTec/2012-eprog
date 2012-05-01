package GA;

import java.util.ArrayList;

import GACore.IGACromosome;
import GACore.IGACross;
import GACore.IGARandom;

public class GAStudentOrdinalCross extends IGACross{

	int longitud;
	
	public IGACromosome[] cross(IGACromosome[] parents) {
		GAStudentCromosome parent1 = (GAStudentCromosome) parents[0];
		GAStudentCromosome parent2 = (GAStudentCromosome) parents[1];
		
		longitud =parent1.getGene().getGen().length;		
		
		int[] p1Coded = codify(parent1.getGene().getGen());
		int[] p2Coded = codify(parent2.getGene().getGen());
		
		int cPoint = IGARandom.getRInt(longitud);
		int[] aux1 = new int[longitud];
		int[] aux2 = new int[longitud];
		
		for(int i=0;i<longitud;i++){
			if(i<cPoint){
				aux1[i]=p1Coded[i];
				aux2[i]=p2Coded[i];
			}else{
				aux1[i]=p2Coded[i];
				aux2[i]=p1Coded[i];
			}			
		}
		
		int[] genD1 = deCode(aux1);
		int[] genD2 = deCode(aux2);
		
		GAStudentGene genP1 = (GAStudentGene) parent1.getGene();
		GAStudentGene genP2 = (GAStudentGene) parent2.getGene();
		GAStudentCromosome[] descendientes= new GAStudentCromosome[2];
		descendientes[0]= new GAStudentCromosome();
		descendientes[0].setGene(new GAStudentGene(genD1, genP1.getNumberStudents(), genP1.getGroupSize(), genP1.getResultAverage(), genP1.getAlphaValue()));
		descendientes[1]= new GAStudentCromosome();
		descendientes[1].setGene(new GAStudentGene(genD2, genP2.getNumberStudents(), genP2.getGroupSize(), genP2.getResultAverage(), genP2.getAlphaValue()));
		
		return descendientes;
	}

	
	public int[] codify(int[] g){
		int[] cdRes = new int[longitud];
		
		ArrayList<Integer> listaD = new ArrayList<Integer>();
		for(int i=0;i<g.length;i++) listaD.add(new Integer(i));
		
		for(int i=0;i<g.length;i++){
			cdRes[i]=listaD.indexOf(new Integer(g[i]));
			listaD.remove(new Integer(g[i]));
		}
		
		return cdRes;
	}

	public int[] deCode(int[] g){
		
		int[] cdRes = new int[longitud];
		
		ArrayList<Integer> listaD = new ArrayList<Integer>();
		for(int i=0;i<g.length;i++) listaD.add(new Integer(i));
		
		for(int i=0;i<g.length;i++){
			cdRes[i]= listaD.remove(g[i]).intValue();
		}
		
		return cdRes;
	}
	
}
