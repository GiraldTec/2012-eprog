package GA;

import GACore.IGACromosome;
import GACore.IGAMutator;
import GACore.IGARandom;

public class GAAntPathMutatorFuncional extends IGAMutator{

	
	public Boolean mutate(IGACromosome cromosoma, double prob) {
		System.out.print("entraamos a mutar");
		Boolean res = new Boolean(false);
		GAProgramTree nodoTerminal = getNodoFuncionalAleatorio(((GAAntPathCromosome)cromosoma).getTreeP());
		if(IGARandom.getRDouble()<= prob){
			nodoTerminal.setOperator((byte)IGARandom.getRInt(3));
			res=true;
		}
		return res;
	}

	private GAProgramTree getNodoFuncionalAleatorio(GAProgramTree treeP) {
		// TODO Auto-generated method stub
		return null;
	}
}
