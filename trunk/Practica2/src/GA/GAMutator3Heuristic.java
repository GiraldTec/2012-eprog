package GA;

import java.util.ArrayList;
import java.util.HashSet;

import GACore.IGAGene;
import GACore.IGAMutator;
import GACore.IGARandom;


public class GAMutator3Heuristic extends IGAMutator{

	GAStudentsEngine engine;
	ArrayList<GAStudent> students;
	
	public void getExtraParams(ArrayList<GAStudent> students){
		this.students=students;
	}
	
	public Boolean mutate(IGAGene gen, double prob) {
		Boolean res = new Boolean(false);
		if(IGARandom.getRDouble()<prob){
			
			int[] indices = new int[3];
			int[] valores = new int[3];
			int i=0;
			
			HashSet<Integer> miniSet = new HashSet<Integer>(); // conjunto de 3 futuros indices distintos
			
			while(i<3){
				if (miniSet.add(new Integer(IGARandom.getRInt(gen.getGen().length)))){
					i++;
				}
			}
			i=0;
			while (miniSet.iterator().hasNext()){
				indices[i]=miniSet.iterator().next().intValue();
				valores[i]=gen.getGen()[indices[i]];
			}
			
			// Las 6 posibles combinaciones de los tres indices en forma de genes:
			
			HashSet<GAStudentGene> genSet = new HashSet<GAStudentGene>();
			
			int[] pDes1 = gen.getGen().clone();
			pDes1[indices[0]]=valores[0];
			pDes1[indices[1]]=valores[1];
			pDes1[indices[2]]=valores[2];
			GAStudentGene desGen1 = new GAStudentGene(pDes1);
			desGen1.calcBalance(students);
			genSet.add(desGen1);
			
			int[] pDes2 = gen.getGen().clone();
			pDes2[indices[0]]=valores[0];
			pDes2[indices[1]]=valores[2];
			pDes2[indices[2]]=valores[1];
			GAStudentGene desGen2 = new GAStudentGene(pDes2);
			desGen2.calcBalance(students);
			genSet.add(desGen2);
			
			int[] pDes3 = gen.getGen().clone();
			pDes3[indices[0]]=valores[1];
			pDes3[indices[1]]=valores[0];
			pDes3[indices[2]]=valores[2];
			GAStudentGene desGen3 = new GAStudentGene(pDes3);
			desGen3.calcBalance(students);
			genSet.add(desGen3);
			
			int[] pDes4 = gen.getGen().clone();
			pDes4[indices[0]]=valores[1];
			pDes4[indices[1]]=valores[2];
			pDes4[indices[2]]=valores[0];
			GAStudentGene desGen4 = new GAStudentGene(pDes4);
			desGen4.calcBalance(students);
			genSet.add(desGen4);
			
			int[] pDes5 = gen.getGen().clone();
			pDes5[indices[0]]=valores[2];
			pDes5[indices[1]]=valores[0];
			pDes5[indices[2]]=valores[1];
			GAStudentGene desGen5 = new GAStudentGene(pDes5);
			desGen5.calcBalance(students);
			genSet.add(desGen5);
			
			int[] pDes6 = gen.getGen().clone();
			pDes6[indices[0]]=valores[2];
			pDes6[indices[1]]=valores[1];
			pDes6[indices[2]]=valores[0];
			GAStudentGene desGen6 = new GAStudentGene(pDes6);
			desGen6.calcBalance(students);
			genSet.add(desGen6);
			
			double mejorV = gen.evaluate(this.students);
			GAStudentGene mejorGen = (GAStudentGene) gen;
			while( genSet.iterator().hasNext() ){
				GAStudentGene auxgen = genSet.iterator().next();
				if (auxgen.evaluate(this.students)<mejorV) mejorGen = auxgen;				
			}
			gen=mejorGen;
			res= !res;
		}
		return res;
	}


	
	

}
