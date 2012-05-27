package GA;

import GACore.IGACromosome;
import GACore.IGAMutator;
import GACore.IGARandom;

public class GAAntPathMutatorTerminal extends IGAMutator{

	
	public Boolean mutate(IGACromosome cromosoma, double prob) {
		System.out.print("entraamos a mutar");
		Boolean res = new Boolean(false);
		GAProgramTree nodoTerminal = getNodoTerminalAleatorio(((GAAntPathCromosome)cromosoma).getTreeP());
		if(IGARandom.getRDouble()<= prob){
			nodoTerminal.setOperator((byte)IGARandom.getRInt(3));
			res=true;
		}
		return res;
	}

	private GAProgramTree getNodoTerminalAleatorio(GAProgramTree treeP) {
		if(treeP.getLeftSon()==null &&
				treeP.getCenterSon()==null &&
					treeP.getRigthSon()==null){
			return treeP;
		}else{
			int hijos=2; // Por defecto siempre tienen dos hijos
			if(treeP.getCenterSon()==null){
				hijos=3;
			}// Dos hijos
			int aux= IGARandom.getRInt(hijos);
			if(aux==2) return getNodoTerminalAleatorio(treeP.getCenterSon());
			else
				if(aux==1) return getNodoTerminalAleatorio(treeP.getRigthSon());
				else
					return getNodoTerminalAleatorio(treeP.getLeftSon());
		}
	}

}
