package GA;

import java.lang.reflect.Array;

import GACore.IGACromosome;
import GACore.IGAMonoPointCross;

public class GABinaryMonoPointCross extends IGAMonoPointCross<Boolean>{
	
	public GABinaryMonoPointCross(){
		descendientes =(GABinaryCromosome[])Array.newInstance(GABinaryCromosome.class, 2);
	}
	public IGACromosome<Boolean>[] cross(IGACromosome<Boolean>[] parents){
		descendientes[0]=new GABinaryCromosome(parents[0].getEvalFunct());
	    descendientes[1]=new GABinaryCromosome(parents[0].getEvalFunct());
	    return crossGenerico(parents);
	}

}
