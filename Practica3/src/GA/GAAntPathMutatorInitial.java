package GA;

import java.util.HashSet;
import java.util.Iterator;

import GACore.IGACromosome;
import GACore.IGAMutator;
import GACore.IGARandom;

public class GAAntPathMutatorInitial extends IGAMutator{

	
	@Override
	public Boolean mutate(IGACromosome cromosoma, double prob) {
		Boolean res = new Boolean(false);
		
		if(IGARandom.getRDouble()<= prob){
			HashSet<GAProgramTree> hashNodo = formHash(((GAAntPathCromosome)cromosoma).getTreeP());
			GAProgramTree nodoTerminal = getNodo(hashNodo,IGARandom.getRInt(hashNodo.size()));
			((GAAntPathCromosome)cromosoma).initTree(nodoTerminal, ((GAAntPathCromosome)cromosoma).getMinD(), ((GAAntPathCromosome)cromosoma).getMaxD());
			res=true;
		}
		return res;
	}
	
	public HashSet<GAProgramTree> formHash(GAProgramTree treeP) {
		HashSet<GAProgramTree> resNodos = new HashSet<GAProgramTree>();
		formHashRecursivo(resNodos,treeP);
		return resNodos;
	}
	
	public void formHashRecursivo(HashSet<GAProgramTree> nodos, GAProgramTree treeP){
		nodos.add(treeP);
		if(treeP.getLeftSon()!=null) 
			formHashRecursivo(nodos, treeP.getLeftSon());
		if(treeP.getCenterSon()!=null)
			formHashRecursivo(nodos, treeP.getCenterSon());
		if(treeP.getRigthSon()!=null)
			formHashRecursivo(nodos, treeP.getRigthSon());
	}
	
	public GAProgramTree getNodo(HashSet<GAProgramTree> nodos, int pto1) {
		int i=0;
		Iterator<GAProgramTree> iterador = nodos.iterator();
		while(iterador.hasNext()&i<pto1){ iterador.next(); i++;}
		return iterador.next();

	}
}
