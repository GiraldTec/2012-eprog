package GA;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

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
			Iterator<Integer> iterador = miniSet.iterator();
			while (iterador.hasNext()){
				indices[i]=iterador.next().intValue();
				valores[i]=gen.getGen()[indices[i]];
				i++;
			}
			
			// Las 6 posibles combinaciones de los tres indices en forma de genes:
			
			HashSet<GAStudentGene> genSet = new HashSet<GAStudentGene>();
			
			int[] pDes1 = gen.getGen().clone();
			pDes1[indices[0]]=valores[0];
			pDes1[indices[1]]=valores[1];
			pDes1[indices[2]]=valores[2];
			GAStudentGene genDes1 = (GAStudentGene) gen;
			GAStudentGene desGen1 = new GAStudentGene(pDes1, genDes1.getNumberStudents(), genDes1.getGroupSize(), genDes1.getResultAverage(), genDes1.getAlphaValue());
			desGen1.calcBalance(students);
			genSet.add(desGen1);
						
			int[] pDes2 = gen.getGen().clone();
			pDes2[indices[0]]=valores[0];
			pDes2[indices[1]]=valores[2];
			pDes2[indices[2]]=valores[1];
			GAStudentGene genDes2 = (GAStudentGene) gen;
			GAStudentGene desGen2 = new GAStudentGene(pDes2, genDes2.getNumberStudents(), genDes2.getGroupSize(), genDes2.getResultAverage(), genDes2.getAlphaValue());
			desGen2.calcBalance(students);
			genSet.add(desGen2);
			
			int[] pDes3 = gen.getGen().clone();
			pDes3[indices[0]]=valores[1];
			pDes3[indices[1]]=valores[0];
			pDes3[indices[2]]=valores[2];
			GAStudentGene genDes3 = (GAStudentGene) gen;
			GAStudentGene desGen3 = new GAStudentGene(pDes1, genDes3.getNumberStudents(), genDes3.getGroupSize(), genDes3.getResultAverage(), genDes3.getAlphaValue());
			desGen3.calcBalance(students);
			genSet.add(desGen3);
			
			int[] pDes4 = gen.getGen().clone();
			pDes4[indices[0]]=valores[1];
			pDes4[indices[1]]=valores[2];
			pDes4[indices[2]]=valores[0];
			GAStudentGene genDes4 = (GAStudentGene) gen;
			GAStudentGene desGen4 = new GAStudentGene(pDes4, genDes4.getNumberStudents(), genDes4.getGroupSize(), genDes4.getResultAverage(), genDes4.getAlphaValue());
			desGen4.calcBalance(students);
			genSet.add(desGen4);
			
			int[] pDes5 = gen.getGen().clone();
			pDes5[indices[0]]=valores[2];
			pDes5[indices[1]]=valores[0];
			pDes5[indices[2]]=valores[1];
			GAStudentGene genDes5 = (GAStudentGene) gen;
			GAStudentGene desGen5 = new GAStudentGene(pDes5, genDes5.getNumberStudents(), genDes5.getGroupSize(), genDes5.getResultAverage(), genDes5.getAlphaValue());
			desGen5.calcBalance(students);
			genSet.add(desGen5);
			
			int[] pDes6 = gen.getGen().clone();
			pDes6[indices[0]]=valores[2];
			pDes6[indices[1]]=valores[1];
			pDes6[indices[2]]=valores[0];
			GAStudentGene genDes6 = (GAStudentGene) gen;
			GAStudentGene desGen6 = new GAStudentGene(pDes6, genDes6.getNumberStudents(), genDes6.getGroupSize(), genDes6.getResultAverage(), genDes6.getAlphaValue());
			desGen6.calcBalance(students);
			genSet.add(desGen6);
			
			double mejorV = gen.evaluate(this.students);
			GAStudentGene mejorGen = (GAStudentGene) gen;
			Iterator<GAStudentGene> gSetIterador = genSet.iterator();
			while( gSetIterador.hasNext() ){
				GAStudentGene auxgen = gSetIterador.next();
				if (auxgen.evaluate(this.students)<mejorV) mejorGen = auxgen;				
			}
			gen=mejorGen;
			res= !res;
		}
		return res;
	}


	
	

}
