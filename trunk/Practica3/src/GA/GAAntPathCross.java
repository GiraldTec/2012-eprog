package GA;

import java.util.HashSet;
import java.util.Iterator;

import GACore.IGACromosome;
import GACore.IGACross;
import GACore.IGARandom;

public class GAAntPathCross extends IGACross {

	@Override
	public IGACromosome[] cross(IGACromosome[] parents) {
		GAAntPathCromosome[] descendientes= new GAAntPathCromosome[2];
		
		descendientes[0] = (GAAntPathCromosome) parents[0].clone();
		descendientes[1] = (GAAntPathCromosome) parents[1].clone();
		
		// A estas alturas nuestros descendientes son copias identicas de sus padres
		
		// vamos a trastocarlos un poco..
		
		GAAntPathCromosome padre1 = (GAAntPathCromosome)descendientes[0];
		GAAntPathCromosome padre2 = (GAAntPathCromosome)descendientes[1];
		
		// Y creamos de forma análoga dos conjuntos para manejar los programas
		
		HashSet<GAProgramTree> gNodos1 = formHash(padre1.getTreeP());
		HashSet<GAProgramTree> gNodos2 = formHash(padre2.getTreeP());
		
		// A cada uno, les hacemos creer que son padres, esto vale por las referrencias de java
		
		int pto1 = IGARandom.getRInt(gNodos1.size());
		int pto2 = IGARandom.getRInt(gNodos2.size());
		
		GAProgramTree nodo1 = getNodo(gNodos1, pto1);
		GAProgramTree pand1 = nodo1.getFather();
		
		GAProgramTree nodo2 = getNodo(gNodos2, pto2);
		GAProgramTree pand2 = nodo2.getFather();
		
		// obtenemos dos nodos aleatorios de cada uno de ellos
		nodo1.setFather(pand2);
		if(pand2.getLeftSon()==nodo1){
			pand2.setLeftSon(nodo1);
		}else{
			if(pand2.getCenterSon()==nodo1){
				pand2.setCenterSon(nodo1);
			}else{ //if(pand2.getRigthSon()==nodo1)
				pand2.setRigthSon(nodo1);
			}
		}
		nodo2.setFather(pand1);
		if(pand1.getLeftSon()==nodo2){
			pand1.setLeftSon(nodo2);
		}else{
			if(pand1.getCenterSon()==nodo2){
				pand1.setCenterSon(nodo2);
			}else{ //if(pand2.getRigthSon()==nodo1)
				pand1.setRigthSon(nodo2);
			}
		}
		
		// Y los intercambiamos, de esta forma devolvemos los hijos y los-padres-sin-modificar
		
		return descendientes;
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
