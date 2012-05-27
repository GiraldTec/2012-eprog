package GA;

import GACore.IGACromosome;
import GACore.IGARandom;

public class GAAntPathCromosome extends IGACromosome {
	private GAProgramTree treeP; // estrategia de rastreo
	private GAAntPathEvaluator evaluator;
	private int minD, maxD;
	
	public void initCromosome(GAAntPathEvaluator eval, int minD, int maxD) {
		evaluator = eval;
		this.minD = minD;
		this.maxD = maxD;
		treeP = new GAProgramTree();
		initTree(treeP, minD, maxD);
	}

	public void evaluate() {
		evaluatedValue = evaluator.evaluate(treeP);
	}
	
	public IGACromosome clone() {
		GAAntPathCromosome clone = new GAAntPathCromosome();
		clone.setTreeP((GAProgramTree) this.treeP.clone());
		clone.setEvaluatedValue(evaluatedValue);
		return clone;
	}
	
	public static void initTree(GAProgramTree tree, int minD, int maxD){
		byte operator;
		
		 /* **************
		 *  -1 -- > Nothing
		 * 	 0 -- > Avanza
		 *   1 -- > Izquierda
		 *   2 -- > Derecha
		 *   3 -- > SiComidaDelante
		 *   4 -- > ProgN2
		 *   5 -- > ProgN3
		 * **************/
		
		if (minD > 0){	//no puede ser hoja
			// generaci�n del subarbol de operador
			operator = (byte) (IGARandom.getRInt(3)+3); // s�mbolo de operador aleatorio
			tree.setOperator(operator);
			// se generan los hijos
			tree.setLeftSon(new GAProgramTree());
			initTree(tree.getLeftSon(), minD - 1 , maxD - 1);
			tree.getLeftSon().setFather(tree);
			
			if (operator == 5) { //si tres_operandos
				tree.setCenterSon(new GAProgramTree());
				initTree(tree.getCenterSon(), minD - 1 , maxD - 1);
				tree.getCenterSon().setFather(tree);
				
				tree.setRigthSon(new GAProgramTree());
				initTree(tree.getRigthSon(), minD - 1 , maxD - 1);
				tree.getRigthSon().setFather(tree);
			}
			else { // dos operandos
				tree.setCenterSon(new GAProgramTree());
				initTree(tree.getCenterSon(), minD - 1 , maxD - 1);
				tree.getCenterSon().setFather(tree);
			}
		}
		else { // prof_min = 0
			if (maxD == 0) { // s�lo puede ser hoja
				operator = (byte) IGARandom.getRInt(3); // s�mbolo de operador aleatorio AQUI HE PUESTO PARA QUE SOLO SALGA AVANZA, IZQ, O DER YA QUE ES UNA HOJA...
				tree.setOperator(operator);
			}
			else {
				// se decide aleatoriamente operando u operador
				if (IGARandom.getRInt(2) == 1) { // se genera operador					
					operator = (byte) (IGARandom.getRInt(3)+3); // s�mbolo de operador aleatorio
					tree.setOperator(operator);
					// se generan los hijos
					tree.setLeftSon(new GAProgramTree());
					initTree(tree.getLeftSon(), minD - 1 , maxD - 1);
					tree.getLeftSon().setFather(tree);
					
					if (operator == 5) { //si tres_operandos
						tree.setCenterSon(new GAProgramTree());
						initTree(tree.getCenterSon(), minD - 1 , maxD - 1);
						tree.getCenterSon().setFather(tree);
						
						tree.setRigthSon(new GAProgramTree());
						initTree(tree.getRigthSon(), minD - 1 , maxD - 1);
						tree.getRigthSon().setFather(tree);
					}
					else { // dos operandos
						tree.setCenterSon(new GAProgramTree());
						initTree(tree.getCenterSon(), minD - 1 , maxD - 1);
						tree.getCenterSon().setFather(tree);
					}
				
				}else { // se genera operando
					// generaci�n del subarbol de operando
					operator = (byte) IGARandom.getRInt(3); // s�mbolo de operador aleatorio
					tree.setOperator(operator);
				}
			}
		}
	}
	
	public GAProgramTree getTreeP(){
		return treeP;
	}
	public void setTreeP(GAProgramTree t){
		treeP = t;
	}

	public int getMinD() {
		return minD;
	}

	public int getMaxD() {
		return maxD;
	}
}