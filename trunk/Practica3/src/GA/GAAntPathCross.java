package GA;

import GACore.IGACromosome;
import GACore.IGACross;

public class GAAntPathCross extends IGACross {

	@Override
	public IGACromosome[] cross(IGACromosome[] parents) {

		GAAntPathCromosome[] descendientes= new GAAntPathCromosome[2];
		// etc etc ...
		
		return descendientes;
	}
	
	/*
	 	El operador de cruce más utilizado en este tipo de problemas es el cruce por
		intercambio de subárboles: seleccionamos 2 nodos de manera aleatoria e intercambiamos sus
		subárboles.
		
		TArbol subarbol1, subarbol2;
		entero num_nodos;
		num_nodos=minimo(num_nodos(padre1.arbol),num_nodos(padre2.arbol));
		nodo_cruce = alea_entero(1,num_nodos);
		hijo1.arbol = padre1.arbol;
		hijo2.arbol = padre2.arbol;
		subarbol1 = hijo1.arbol.BuscarNodo(nodo_cruce);
		subarbol2 = hijo2.arbol.BuscarNodo(nodo_cruce);
		hijo1.arbol.SustituirSubarbol(nodo_cruce, subarbol2);
		hijo2.arbol.SustituirSubarbol(nodo_cruce, subarbol1);
		hijo1.adaptacion = adaptacion(hijo1);
		hijo2.adaptacion = adaptacion(hijo2);
		...
	 */
	
	

}
