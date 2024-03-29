package GA;

import GACore.IGACromosome;
import GACore.IGARandom;

public class GAAntPathCromosome extends IGACromosome {
	private GAProgramTree treeP; // estrategia de rastreo
	private GAAntPathEvaluator evaluator;
	private int minD, maxD;
	private static byte ultimoMov = -1; 
	
	public void initCromosome(GAAntPathEvaluator eval, int minD, int maxD) {
		evaluator = eval;
		this.minD = minD;
		this.maxD = maxD;
		treeP = new GAProgramTree();
		initTree(treeP, minD, maxD);
		ultimoMov=-1;
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

	public void evaluate(boolean sim) {
		evaluatedValue = evaluator.evaluate(this.treeP, sim);
		//porPantalla(treeP);
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
	
	public void initTree(GAProgramTree tree, int minD, int maxD){
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
			operator = IGARandom.getRInt(4) > 1 ? (byte)3 : (byte) (IGARandom.getRInt(2)+4);
			//operator = (byte) (IGARandom.getRInt(3)+3); // s�mbolo de operador aleatorio
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
			if (maxD == 0) { // s�lo puede ser hoja
				operator = IGARandom.getRInt(4) > 0 ? (byte)0 : (byte) (IGARandom.getRInt(2)+1);// s�mbolo de operador aleatorio
				/*if(ultimoMov == -1){
					ultimoMov = operator;
				}
				else{
					if((ultimoMov == 1 && operator == 2)||(ultimoMov == 2 && operator == 1)){
						operator = (byte)0;
					}
					ultimoMov = operator;
				}	*/			
				//operator = (byte)IGARandom.getRInt(3);
				//operator = IGARandom.getRInt(5) > 1 ? (byte)0 : (byte) IGARandom.getRInt(3);
				tree.setOperator(operator);
			}
			else {
				// se decide aleatoriamente operando u operador
				if (IGARandom.getRInt(2) == 1) { // se genera operador					
					//operator = IGARandom.getRInt(4) > 1 ? (byte)3 : (byte) (IGARandom.getRInt(3)+3); // s�mbolo de operador aleatorio
					operator = (byte) (IGARandom.getRInt(3)+3);
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
					// generaci�n del subarbol de operando
					operator = (byte)IGARandom.getRInt(3);// s�mbolo de operador aleatorio
					if(ultimoMov == -1){
						ultimoMov = operator;
					}
					else {
						if(ultimoMov == 1 || ultimoMov == 2){
							operator = (byte)0;
						}							
						ultimoMov = operator;
					}
					//operator = (byte)IGARandom.getRInt(3);
					//operator = IGARandom.getRInt(5) > 1 ? (byte)0 : (byte) IGARandom.getRInt(3);
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
	
	public static void porPantalla(GAProgramTree prog){
		if(prog!=null){
			if(prog.getOperator()>=3){
				switch (prog.getOperator()){
					case 3: System.out.print("SC("); break;
					case 4: System.out.print("P2("); break;
					case 5: System.out.print("P3("); break;
				}
				porPantalla(prog.getLeftSon()); System.out.print(",");
				porPantalla(prog.getCenterSon()); System.out.print(",");
				porPantalla(prog.getRigthSon()); System.out.print(")");
			}else{
				switch (prog.getOperator()){
				case 0: System.out.print("avanza"); break;
				case 1: System.out.print("izquierda"); break;
				case 2: System.out.print("derecha"); break;
			}
			}
		}
	}
}
