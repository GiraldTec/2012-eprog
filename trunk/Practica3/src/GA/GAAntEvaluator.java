package GA;

public class GAAntEvaluator {
	
	public static double evaluate (int[][] board, int posX, int posY, int orient, GAProgramTree program){

		if(program.getOperator() == 3){
			
		}
		if(program.getOperator() == 4){
			
		}
		if(program.getOperator() == 5){
			
		}
		
		return 0;
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
