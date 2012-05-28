package GA;
import java.util.logging.Logger;

import practica3.AntBoardManager;
import practica3.AntBoardManager.AntRotation;

public class GAAntPathEvaluator {
	private int maxSteps;
	private int maxFood;
	private static Integer steps=0;
	private static int food=0;
	private static int giros=0;
	private static int avanzas=0;
	private int simSpeed=200;
	private boolean useSim=true, simOverride=false;
	private AntBoardManager boardMngr;
	public static Logger log = Logger.getLogger("Engine");
	
	public GAAntPathEvaluator(AntBoardManager boardMngr, int maxSteps, int maxFood, boolean useSimulation, int simulationSpeed) {
		this.maxFood = maxFood;
		this.maxSteps = maxSteps;
		this.boardMngr = boardMngr;
		this.useSim = useSimulation;
		this.simSpeed = simulationSpeed;
	}
	
	public double evaluate(GAProgramTree program, boolean sim){
		steps = 0;
		food = 0;
		simOverride = sim;
		avanzas=0;
		giros=0;
				
		boardMngr.restoreInitialState();
		log.info("Comenzando Evaluación de programa");
		while (steps < maxSteps && food < maxFood){
			executeStep(boardMngr, program);
		}
		log.info("Steps: "+steps+" Avanzas: "+avanzas+ " Giros: "+giros);
		log.info("Programa completado");
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
		
		if (useSim && program.getOperator() < 3 && simOverride) {
			try {
				boardMngr.forceUpdateBoard();
				Thread.sleep(simSpeed);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		if (steps >= maxSteps || food >= maxFood)
			return;
		if (program.getOperator()<3)
		{
			steps++;
			food = boardMngr.getEatenFood();
		}
				
		//acciones a realizar en función del nodo en el que estemos
		//System.out.println("Operator: "+program.getOperator()+" ");
		switch (program.getOperator()){
			case 5 : 
				executeStep(boardMngr, program.getLeftSon());
				executeStep(boardMngr, program.getCenterSon());
				executeStep(boardMngr, program.getRigthSon());
				break;
			case 4:
				executeStep(boardMngr, program.getLeftSon());
				executeStep(boardMngr, program.getRigthSon());
				break;
			case 3:
				if (boardMngr.foodInfront())
					executeStep(boardMngr, program.getLeftSon());
				else
					executeStep(boardMngr, program.getRigthSon());
				break;
			case 2:
				giros++;
				boardMngr.rotateAnt(AntRotation.RIGHT);
				break;
			case 1:
				giros++;
				boardMngr.rotateAnt(AntRotation.LEFT);
				break;
			case 0:
				avanzas++;
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
