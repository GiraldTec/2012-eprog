package GA;
import practica3.AntBoardManager;
import practica3.AntBoardManager.AntRotation;

public class GAAntPathEvaluator {
	private int maxSteps;
	private int maxFood;
	private static Integer steps=0;
	private static int food=0;
	private int simSpeed=200;
	private boolean useSim=true;
	private AntBoardManager boardMngr;
	
	public GAAntPathEvaluator(AntBoardManager boardMngr, int maxSteps, int maxFood, boolean useSimulation, int simulationSpeed) {
		this.maxFood = maxFood;
		this.maxSteps = maxSteps;
		this.boardMngr = boardMngr;
		this.useSim = useSimulation;
		this.simSpeed = simulationSpeed;
	}
	
	public double evaluate(GAProgramTree program){
		steps = 0;
		food = 0;
		
		System.out.println("Comenzando Evaluación de programa");
		boardMngr.restoreInitialState();
		while (steps < maxSteps && food < maxFood){
			executeStep(boardMngr, program);
		}
		boardMngr.forceUpdateBoard();
		System.out.println("Programa completado");
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
		
		if (useSim && program.getOperator() < 3) {
			try {
				boardMngr.forceUpdateBoard();
				Thread.sleep(simSpeed);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		steps++;
		food = boardMngr.getEatenFood();
		
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
