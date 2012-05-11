package GA;

public class GAProgrammeTree {

	private byte kindOf; 
	/* **************
	 * 	Tipos:
	 * **************
	 * 
	 *  -1 -- > Nothing
	 * 	 0 -- > Avanza
	 *   1 -- > Izquierda
	 *   2 -- > Derecha
	 *   3 -- > SiComidaDelante
	 *   4 -- > ProgN2
	 *   5 -- > ProgN3
	 *   
	 * **************
	 * */
	private GAProgrammeTree leftSon;
	private GAProgrammeTree centerSon;
	private GAProgrammeTree rigthSon;
	
	public GAProgrammeTree(){
		setKindOf((byte) -1);
		leftSon = null;
		centerSon = null;
		rigthSon = null;
	}
	
	public GAProgrammeTree initTree(){
		
		/*
		 * funcion creaArbol(TArbol arbol,entero prof_min,entero prof_max){
			si prof_min > 0 entonces //no puede ser hoja
				// generación del subarbol de operador
				
				operador = operador_aleatorio; // símbolo de operador aleatorio
				arbol.dato = operador;
				// se generan los hijos
				HI = construir_arbol(arbol.HI, prof_min - 1, prof_max - 1);
				arbol.num_nodos = arbol.num_nodos + arbol.HI.num_nodos;
				
				si tres_operandos(operador) entonces
					HC = construir_arbol(arbol.HC, prof_min - 1, prof_max - 1);
					arbol.num_nodos = arbol.num_nodos + arbol.HC.num_nodos;
				
				eoc // dos operandos
					HC = NULL;
					HD = construir_arbol(arbol.HD, prof_min - 1, prof_max - 1);
					arbol.num_nodos = arbol.num_nodos + arbol.HD.num_nodos;
			eoc // prof_min = 0
			
				si prof_max = 0 entonces // sólo puede ser hoja
					// generación del subarbol de operando
					operando = operando_aleatorio;
					// símbolo de operando aleatorio
					arbol.dato = operando;
					arbol.num_nodos = arbol.num_nodos + 1;
				eoc
					// se decide aleatoriamente operando u operador
					tipo = aleatorio_cero_uno;
					
					si tipo = 1 entonces // se genera operador
						// generación del subarbol de operador
						{ }
					eoc // se genera operando
						// generación del subarbol de operando
						{  }
			}
		 * 
		 * */
		return null;
	}

	public void setKindOf(byte kindOf) {
		this.kindOf = kindOf;
	}

	public GAProgrammeTree getLeftSon() {
		return leftSon;
	}

	public void setLeftSon(GAProgrammeTree leftSon) {
		this.leftSon = leftSon;
	}

	public GAProgrammeTree getCenterSon() {
		return centerSon;
	}

	public void setCenterSon(GAProgrammeTree centerSon) {
		this.centerSon = centerSon;
	}

	public GAProgrammeTree getRigthSon() {
		return rigthSon;
	}

	public void setRigthSon(GAProgrammeTree rigthSon) {
		this.rigthSon = rigthSon;
	}

	public short getKindOf() {
		return kindOf;
	}
}
