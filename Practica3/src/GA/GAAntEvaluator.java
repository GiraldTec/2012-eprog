package GA;
import practica3.AntBoardManager;
import practica3.AntBoardManager.AntRotation;

public class GAAntEvaluator{
	private static int steps=0;
	private static int food=0;
	
	public double evaluate (AntBoardManager boardMngr, GAProgramTree program, int maxSteps, int maxFood){
		steps = 0;
		food = 0;
		
		while (steps < maxSteps && food < maxFood){
			executeStep(boardMngr, program, maxSteps, maxFood);
		}
		return food;
	}
	
	private void executeStep(AntBoardManager boardMngr, GAProgramTree program, int maxSteps, int maxFood) {
		
		 /* **************
		 *  -1 -- > Nothing
		 * 	 0 -- > Avanza
		 *   1 -- > Izquierda
		 *   2 -- > Derecha
		 *   3 -- > SiComidaDelante
		 *   4 -- > ProgN2
		 *   5 -- > ProgN3 
		 * **************/
		
		//acciones a realizar en función del nodo en el que estemos
		switch (program.getOperator()){
			case 5 : 
				executeStep(boardMngr, program.getLeftSon(), maxSteps, maxFood);
				executeStep(boardMngr, program.getCenterSon(), maxSteps, maxFood);
				executeStep(boardMngr, program.getRigthSon(), maxSteps, maxFood);
				break;
			case 4:
				executeStep(boardMngr, program.getLeftSon(), maxSteps, maxFood);
				executeStep(boardMngr, program.getCenterSon(), maxSteps, maxFood);
				break;
			case 3:
				if (boardMngr.foodInfront())
					executeStep(boardMngr, program.getLeftSon(), maxSteps, maxFood);
				else
					executeStep(boardMngr, program.getCenterSon(), maxSteps, maxFood);
				break;
			case 2:
				boardMngr.rotateAnt(AntRotation.RIGHT);
				break;
			case 1:
				boardMngr.rotateAnt(AntRotation.LEFT);
				break;
			case 0:
				boardMngr.advanceAnt();
				break;
			default:
				System.err.println("Error al evaluar: operador incorrecto");		
		}		
	}
	
	/*
		Función de adaptación
		La adaptación o aptitud de un individuo es la cantidad de alimento comido por la
		hormiga dentro de un espacio de tiempo razonable al ejecutar el programa a evaluar. Se
		considera que cada operación de movimiento o giro consume una unidad de tiempo. En
		nuestra versión del problema limitaremos el tiempo a 400 pasos. Lo planteamos como un
		problema de maximización.
		
		funcion adaptacion(TIndividuo individuo, TMapa mapa) {
			. . .
			mientras (pasos < 400 y bocados < 90)
			ejecutaArbol(individuo.arbol);
			. . .
		}
		
		public void ejecutaArbol(Arbol A){
			//mientras no se haya acabado el tiempo ni la comida
			si (pasos < 400 && bocados <90) entonces {
			//si estamos encima de comida comemos
			si (matriz[posicionX][posicionY]== 1) entonces {
			matriz[posicionX][posicionY] = 0;
			bocados++;
			}
			//acciones a realizar en función del nodo en el que estemos
			si A.getValor()== "PROGN3"){
			ejecutaArbol(A.getHi());
			ejecutaArbol(A.getHc());
			ejecutaArbol(A.getHd());
			}
			eoc si (A.getValor()== "PPROGN2"){
			ejecutaArbol(A.getHi());
			ejecutaArbol(A.getHc());
			}
			eoc if(A.getValor()== "SIC"){
			si (hayComida()) ejecutaArbol(A.getHi());
			eoc ejecutaArbol(A.getHc());
			}
			eoc si A.getValor()== "AVANZA") Avanza();
			eoc si A.getValor()== "DERECHA") Derecha();
			eoc si(A.getValor()== "IZQUIERDA") Izquierda();
			}
		}
		*/
}
