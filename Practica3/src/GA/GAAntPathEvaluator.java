package GA;
import practica3.AntBoardManager;
import practica3.AntBoardManager.AntRotation;

public class GAAntPathEvaluator {
	private int maxSteps;
	private int maxFood;
	private int steps=0;
	private int food=0;
	private int simSpeed=400;
	private boolean useSim=false;
	private AntBoardManager boardMngr;
	
	public GAAntPathEvaluator(AntBoardManager boardMngr, int maxSteps, int maxFood) {
		this.maxFood = maxFood;
		this.maxSteps = maxSteps;
		this.boardMngr = boardMngr;
	}
	
	public double evaluate(GAProgramTree program){
		steps = 0;
		food = 0;
		
		boardMngr.restoreInitialState();
		while (steps < maxSteps && food < maxFood){
			executeStep(boardMngr, program);
			food = boardMngr.getEatenFood();
			steps++;
		}
		boardMngr.forceUpdateBoard();
		return food;
	}
	
	private void executeStep(AntBoardManager boardMngr, GAProgramTree program) {
		
		 /* **************
		 *  -1 -- > Nothing
		 * 	 0 -- > Avanza
		 *   1 -- > Izquierda
		 *   2 -- > Derecha
		 *   3 -- > SiComidaDelante
		 *   4 -- > ProgN2
		 *   5 -- > ProgN3
		 * **************/
		
		if (/*useSim*/ program.getOperator() < 3) {
			try {
				boardMngr.forceUpdateBoard();
				Thread.sleep(simSpeed);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		//acciones a realizar en función del nodo en el que estemos
		System.out.println("Operator: "+program.getOperator()+" ");
		switch (program.getOperator()){
			case 5 : 
				executeStep(boardMngr, program.getLeftSon());
				executeStep(boardMngr, program.getCenterSon());
				executeStep(boardMngr, program.getRigthSon());
				break;
			case 4:
				executeStep(boardMngr, program.getLeftSon());
				executeStep(boardMngr, program.getCenterSon());
				break;
			case 3:
				if (boardMngr.foodInfront())
					executeStep(boardMngr, program.getLeftSon());
				else
					executeStep(boardMngr, program.getCenterSon());
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
		
	void enableSimulation(boolean activate){
		useSim = activate;
	}
	
	void setSimulationSpeed(int speed){
		simSpeed = speed;
	}
}
