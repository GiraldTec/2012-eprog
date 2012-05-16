package GA;

import GACore.IGARandom;

public class GAProgramTree {

	private byte operator; 
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
	private GAProgramTree leftSon;
	private GAProgramTree centerSon;
	private GAProgramTree rigthSon;
	
	private int numNodes;
	
	// valores por defecto
	private int minDepth = 5;
	private int maxDepth = 10;
	
	public GAProgramTree(){
		setOperator((byte) -1);
		leftSon = null;
		centerSon = null;
		rigthSon = null;
		numNodes = 0;
		
		// crear el arbol
		initTree(this, minDepth, maxDepth);
	}
	
	public GAProgramTree initTree(GAProgramTree tree, int minD, int maxD){
		byte operator;
		GAProgramTree hIzq, hDer, hCen;
		
		if (minD > 0){	//no puede ser hoja
			// generación del subarbol de operador
			operator = (byte) IGARandom.getRInt(5); // símbolo de operador aleatorio
			tree.operator = operator;
			// se generan los hijos
			hIzq = new GAProgramTree();
			hIzq.initTree(hIzq, minD - 1 , maxD - 1);
			numNodes = numNodes + hIzq.getNumNodes();
			
			if (operator == 5) { //si tres_operandos
				hCen = new GAProgramTree();
				hCen.initTree(hCen, minD - 1 , maxD - 1);
				numNodes = numNodes + hCen.getNumNodes();
			}
			else { // dos operandos
				hDer = new GAProgramTree();
				hDer.initTree(hDer, minD - 1 , maxD - 1);
				numNodes = numNodes + hDer.getNumNodes();
			}
		}
		else { // prof_min = 0
			if (maxD == 0) { // sólo puede ser hoja
				operator = (byte) IGARandom.getRInt(2); // símbolo de operador aleatorio AQUI HE PUESTO PARA QUE SOLO SALGA AVANZA, IZQ, O DER YA QUE ES UNA HOJA...
				numNodes++;
			}
			else {
				// se decide aleatoriamente operando u operador
				if (IGARandom.getRInt(1) == 1) { // se genera operador
					// generación del subarbol de operador
					
				}
				else { // se genera operando
					// generación del subarbol de operando
					
				}
			}
		}
			
		/*si prof_min > 0 entonces 
				
				
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
		 */
		return this;
	}

	public void setOperator(byte operator) {
		this.operator = operator;
	}

	public GAProgramTree getLeftSon() {
		return leftSon;
	}

	public void setLeftSon(GAProgramTree leftSon) {
		this.leftSon = leftSon;
	}

	public GAProgramTree getCenterSon() {
		return centerSon;
	}

	public void setCenterSon(GAProgramTree centerSon) {
		this.centerSon = centerSon;
	}

	public GAProgramTree getRigthSon() {
		return rigthSon;
	}

	public void setRigthSon(GAProgramTree rigthSon) {
		this.rigthSon = rigthSon;
	}

	public short getOperator() {
		return operator;
	}

	public int getNumNodes() {
		return numNodes;
	}

	public void setNumNodes(int numNodes) {
		this.numNodes = numNodes;
	}
}
