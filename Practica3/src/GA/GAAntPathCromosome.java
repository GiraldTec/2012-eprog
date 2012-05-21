package GA;

import GACore.IGACromosome;
import GACore.IGARandom;

public class GAAntPathCromosome extends IGACromosome {
	private GAProgramTree treeP; // estrategia de rastreo
	private GAAntPathEvaluator evaluator;
	
	public void initCromosome(GAAntPathEvaluator eval, int minD, int maxD) {
		evaluator = eval;
		treeP = new GAProgramTree();
		initTree(treeP, minD, maxD);
	}

	public void evaluate() {
		evaluator.evaluate(treeP);
	}
	
	public IGACromosome clone() {
		GAAntPathCromosome clone = new GAAntPathCromosome();
		clone.setTreeP((GAProgramTree) this.treeP.clone());
		clone.setEvaluatedValue(evaluatedValue);

		return clone;
	}
	
	public static void initTree(GAProgramTree tree, int minD, int maxD){
		byte operator;
		
		if (minD > 0){	//no puede ser hoja
			// generación del subarbol de operador
			operator = (byte) (IGARandom.getRInt(3)+3); // símbolo de operador aleatorio
			tree.setOperator(operator);
			// se generan los hijos
			tree.setLeftSon(new GAProgramTree());
			initTree(tree.getLeftSon(), minD - 1 , maxD - 1);
			tree.getLeftSon().setFather(tree);
			
			if (operator == 5) { //si tres_operandos
				tree.setCenterSon(new GAProgramTree());
				initTree(tree.getCenterSon(), minD - 1 , maxD - 1);
				tree.getCenterSon().setFather(tree);
			}
			else { // dos operandos
				tree.setRigthSon(new GAProgramTree());
				initTree(tree.getRigthSon(), minD - 1 , maxD - 1);
				tree.getRigthSon().setFather(tree);
			}
		}
		else { // prof_min = 0
			if (maxD == 0) { // sólo puede ser hoja
				operator = (byte) IGARandom.getRInt(3); // símbolo de operador aleatorio AQUI HE PUESTO PARA QUE SOLO SALGA AVANZA, IZQ, O DER YA QUE ES UNA HOJA...
				tree.setOperator(operator);
			}
			else {
				// se decide aleatoriamente operando u operador
				if (IGARandom.getRInt(2) == 1) { // se genera operador
					
					
					operator = (byte) (IGARandom.getRInt(3)+3); // símbolo de operador aleatorio
					tree.setOperator(operator);
					// se generan los hijos
					tree.setLeftSon(new GAProgramTree());
					initTree(tree.getLeftSon(), minD - 1 , maxD - 1);
					tree.getLeftSon().setFather(tree);
					
					if (operator == 5) { //si tres_operandos
						tree.setCenterSon(new GAProgramTree());
						initTree(tree.getCenterSon(), minD - 1 , maxD - 1);
						tree.getCenterSon().setFather(tree);
					}
					else { // dos operandos
						tree.setRigthSon(new GAProgramTree());
						initTree(tree.getRigthSon(), minD - 1 , maxD - 1);
						tree.getRigthSon().setFather(tree);
					}
				
				
				}else { // se genera operando
					// generación del subarbol de operando
					operator = (byte) IGARandom.getRInt(3); // símbolo de operador aleatorio AQUI HE PUESTO PARA QUE SOLO SALGA AVANZA, IZQ, O DER YA QUE ES UNA HOJA...
					tree.setOperator(operator);
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
		
	}
	
	public GAProgramTree getTreeP(){
		return treeP;
	}
	public void setTreeP(GAProgramTree t){
		treeP = t;
	}
	

}
