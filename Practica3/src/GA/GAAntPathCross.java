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
//		System.out.print("Padre0>>");
//		GAAntPathCromosome.porPantalla(descendientes[0].getTreeP());
//		System.out.println("");
//		System.out.println("Padre1>>");
//		GAAntPathCromosome.porPantalla(descendientes[1].getTreeP());
//		System.out.println("");
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
		
		// obtenemos dos nodos aleatorios de cada uno de ellos
		GAProgramTree nodo1 = getNodo(gNodos1, pto1);
		GAProgramTree pand1 = nodo1.getFather();
		GAProgramTree nodo2 = getNodo(gNodos2, pto2);
		GAProgramTree pand2 = nodo2.getFather();
		
		// Averiguamos en que posicion respecto a sus padres están, si los tienen
		int posNod1 = -1;
		int posNod2 = -1;
		
		if(pand1!=null){
			if(pand1.getLeftSon()==nodo1)				posNod1=0;
			else	if(pand1.getCenterSon()==nodo1)		posNod1=1;
					else								posNod1=2;
		}
		if(pand2!=null){
			if(pand2.getLeftSon()==nodo2)				posNod2=0;
			else	if(pand2.getCenterSon()==nodo2)		posNod2=1;
				else									posNod2=2;
		}
		
		// Recolocamos los nodos bajo los padres que deben, si los tienen
		if(pand2!=null)
			if(posNod1==0)			pand2.setLeftSon(nodo1);
			else 	if(posNod1==1)	pand2.setCenterSon(nodo1);
					else 			pand2.setRigthSon(nodo1);
		
		nodo1.setFather(pand2); // Y le ponemos un nuevo padre
		if(pand1!=null)
			if(posNod2==0)			pand1.setLeftSon(nodo2);
			else 	if(posNod2==1)	pand1.setCenterSon(nodo2);
					else 			pand1.setRigthSon(nodo2);
		nodo2.setFather(pand1); // Y le ponemos un nuevo padre
		
		// Y los intercambiamos, de esta forma devolvemos los hijos y los-padres-sin-modificar
//		System.out.println("Hijo0>>");
//		GAAntPathCromosome.porPantalla(descendientes[0].getTreeP());
//		System.out.println("");
//		
//		System.out.println("Hijo1>>");
//		GAAntPathCromosome.porPantalla(descendientes[1].getTreeP());
//		System.out.println("");
		
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
