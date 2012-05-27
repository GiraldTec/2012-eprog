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
		treeP.setFather(null);
	}

	public GAAntPathEvaluator getEvaluator() {
		return evaluator;
	}

	public void setEvaluator(GAAntPathEvaluator evaluator) {
		this.evaluator = evaluator;
	}

	public void setMinD(int minD) {
		this.minD = minD;
	}

	public void setMaxD(int maxD) {
		this.maxD = maxD;
	}

	public void evaluate() {
		evaluatedValue = evaluator.evaluate(this.treeP);
	}
	
	public IGACromosome clone() {
		GAAntPathCromosome clon = new GAAntPathCromosome();
		clon.setAcum_Score(this.getAcum_Score());
		clon.setEvaluatedValue(this.getEvaluatedValue());
		clon.setScore(this.getScore());
		clon.setTreeP((GAProgramTree) this.getTreeP().clone(null));
		clon.setEvaluator(this.getEvaluator());
		clon.setMaxD(this.getMaxD());
		clon.setMinD(this.getMinD());
		return clon;
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
			// dos operandos
				tree.setRigthSon(new GAProgramTree());
				initTree(tree.getRigthSon(), minD - 1 , maxD - 1);
				tree.getRigthSon().setFather(tree);
			
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
					tree.setRigthSon(new GAProgramTree());
					initTree(tree.getRigthSon(), minD - 1 , maxD - 1);
					tree.getRigthSon().setFather(tree);
				
				}else { // se genera operando
					// generación del subarbol de operando
					operator = (byte) IGARandom.getRInt(3); // símbolo de operador aleatorio
					tree.setOperator(operator);
				}
			}
		}
	}
	
	public GAProgramTree getTreeP(){
		return this.treeP;
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
