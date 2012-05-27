package GA;

import java.util.HashSet;
import java.util.Iterator;

import GACore.IGACromosome;
import GACore.IGAMutator;
import GACore.IGARandom;

public class GAAntPathMutatorFuncional extends IGAMutator{

	
	public Boolean mutate(IGACromosome cromosoma, double prob) {
		Boolean res = new Boolean(false);
		GAProgramTree nodoTerminal = getNodoFuncionalAleatorio(((GAAntPathCromosome)cromosoma).getTreeP());
		if(IGARandom.getRDouble()<= prob){
			nodoTerminal.setOperator((byte)IGARandom.getRInt(3));
			res=true;
		}
		return res;
	}

	private GAProgramTree getNodoFuncionalAleatorio(GAProgramTree treeP) {
		HashSet<GAProgramTree> hashFuncionales = formHashFuncionales(treeP);
		Iterator<GAProgramTree> iterador= hashFuncionales.iterator();
		int i=0;
		int pto1= IGARandom.getRInt(hashFuncionales.size());
		while(i<pto1 && iterador.hasNext()){
			iterador.next();
			i++;
		}
		return iterador.next();
	}
	
	public HashSet<GAProgramTree> formHashFuncionales(GAProgramTree treeP) {
		HashSet<GAProgramTree> resNodos = new HashSet<GAProgramTree>();
		formHashFuncionalesR(resNodos,treeP);
		return resNodos;
	}
	
	public void formHashFuncionalesR(HashSet<GAProgramTree> nodos, GAProgramTree treeP){
		if(treeP.getLeftSon()==null &&
				treeP.getCenterSon()==null &&
					treeP.getRigthSon()==null){} // Si se trata de una hoja
		else{// si es un nodo funcional
			nodos.add(treeP);
			formHashFuncionalesR(nodos, treeP.getLeftSon());
			if(treeP.getCenterSon()!=null)
				formHashFuncionalesR(nodos, treeP.getCenterSon());
			formHashFuncionalesR(nodos, treeP.getRigthSon());
		}
	}
}
