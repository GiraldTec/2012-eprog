package practica3;

import gui.AntBoard;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

import GACore.IGARandom;

public class AntBoardManager {
	public enum PieceType {
		NOTHING(0),	PATH(1), FOOD(2), EATENFOOD(3), ANT(4);		
		public int id;		
		PieceType(int i){id = i;}
	};
	public enum AntRotation {
		UP(0),	RIGHT(1), DOWN(2), LEFT(3);	
		public int id;
		AntRotation(int i){id = i;}
	};
	
	private int size;
	private PieceType[] state;
	private PieceType[] initialState;
	private int antPosX=-1, antPosY=-1;
	private int oldX=-1, oldY=-1;
	private int oldFoodX=-1, oldFoodY=-1;
	private AntRotation currentAntRot;
	private int eatenFood = 0;
	private boolean ateFood=false;
	private AntBoard board;
	private boolean snakeMode=false;

	public AntBoardManager(int newSize) {
		size = newSize;
		currentAntRot = AntRotation.RIGHT;

		state = new PieceType[size * size];
		initialState = new PieceType[size * size];
				
		setAntPosGoodCoord(0,0);
		System.arraycopy(state,0,initialState,0,state.length);		
	}
	
	public int getSize(){
		return size;
	}

	public PieceType[] getState() {
		return state;
	}
	
	public void setPosValue(int i, int j, PieceType p) {
		state[i * size + j] = p;
	}
	
	public PieceType getPos(int i, int j) {
		return state[i * size + j];
	}
	
	public PieceType getPosGoodCoord(int i, int j) {
		i = Math.abs(i-(size-1));
		return state[i * size + j];
	}
	
	public void move(int i, int j, PieceType p) {
		i = Math.abs(i-(size-1));
		state[i * size + j] = p;
	}
	public void setAntPos(int i, int j) {
		// Clear old ant
		move(antPosX, antPosY, PieceType.NOTHING);
		setPosValue(i, j, PieceType.ANT);
		antPosX = Math.abs(i-(size-1));
		antPosY = j;
	}
	
	public void setAntPosGoodCoord(int i, int j) {
		// Clear old ant
		if (antPosX != -1 && antPosY != -1){
			if (oldFoodX != -1){
				move(oldFoodX, oldFoodY, PieceType.EATENFOOD);
				oldFoodX = -1;
			}
			else
				move(antPosX, antPosY, PieceType.PATH);
		}
		
		move(i, j, PieceType.ANT);
		antPosX = i;
		antPosY = j;
	}	
	
	public void resetAntPos() {
		// Clear old ant
		move(0, 0, PieceType.ANT);
		int i = Math.abs(oldX-(size-1));
		i = (i * size) + oldY;
		move(oldX, oldY, initialState[i]);
		currentAntRot = AntRotation.RIGHT;
		eatenFood = 0;
		board.setFoodText(0);
		
		oldX = -1;
		oldY = -1;
		oldFoodX = -1;
		oldFoodY = -1;
		antPosX = 0;
		antPosY = 0;
	}
	
	public void rotateAnt(AntRotation rot){
		int newRot=currentAntRot.id;
		
		if (rot == AntRotation.RIGHT){
			newRot = (currentAntRot.id + 1) % 4; 
		}
		else if(rot == AntRotation.LEFT){
			if (currentAntRot.id == 0)
				newRot = 3;
			else
				newRot = currentAntRot.id - 1;
		}
		else
			System.err.println("Error rotating Ant: only Left ot Right rotations!");
		
		switch (newRot){
			case 0: 
				currentAntRot = AntRotation.UP;
				break;
			case 1: 
				currentAntRot = AntRotation.RIGHT;
				break;
			case 2: 
				currentAntRot = AntRotation.DOWN;
				break;
			case 3: 
				currentAntRot = AntRotation.LEFT;
				break;
			default:
				System.err.println("Error rotating Ant: new rotation is wrong");
		}
	}
	
	public void advanceAnt() {				
		ateFood = foodInfront();
				
		switch (currentAntRot){
			case RIGHT :
				if (antPosY == size-1)
					{if (snakeMode) setAntPosGoodCoord(antPosX, 0); else return;}
				else
					setAntPosGoodCoord(antPosX, antPosY+1);
				break;
			case DOWN :
				if (antPosX == size-1)
					{if (snakeMode) setAntPosGoodCoord(0, antPosY); else return;}
				else
					setAntPosGoodCoord(antPosX+1, antPosY);
				break;
			case LEFT :
				if (antPosY == 0)
					{if (snakeMode) setAntPosGoodCoord(antPosX, size-1);}
				else
					setAntPosGoodCoord(antPosX, antPosY-1);
				break;
			case UP :
				if (antPosX == 0)
					{if (snakeMode) setAntPosGoodCoord(size-1, antPosY); else return;}					
				else
					setAntPosGoodCoord(antPosX-1, antPosY);
				break;
			default:
				System.err.println("Error avanzando hormiga");
		}
				
		oldX = antPosX;
		oldY = antPosY;

		if (ateFood){
			oldFoodX = antPosX;
			oldFoodY = antPosY;
			eatenFood++;
			board.setFoodText(eatenFood);
		}

	}
	
	public boolean foodInfront(){
		switch (currentAntRot){
			case RIGHT :
				if (antPosY == size-1)
					return false;
				else
					return getPosGoodCoord(antPosX, antPosY+1) == PieceType.FOOD;
			case DOWN :
				if (antPosX == size-1)
					return false;
				else
					return getPosGoodCoord(antPosX+1, antPosY) == PieceType.FOOD;
			case LEFT :
				if (antPosY == 0)
					return false;
				else
					return getPosGoodCoord(antPosX, antPosY-1) == PieceType.FOOD;
			case UP :
				if (antPosX == 0)
					return false;
				else
					return getPosGoodCoord(antPosX-1, antPosY) == PieceType.FOOD;
		}
		return false;
	}
	
	public void loadMapFromFile(String fileName){
		try{

			if (fileName.isEmpty())
				fileName  = "Santa Fe";

			// Open the file
			FileInputStream fstream = new FileInputStream("maps/" + fileName);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine, data[];

			// Read each row of data
			for (int i=0; i<size; i++) {
				strLine = br.readLine();
				data = strLine.split(" ");
					
				for (int j=0; j<size; j++){
					if (data[j].compareTo("0") == 0)
						move(i, j, PieceType.NOTHING);
					else if (data[j].compareTo("#") == 0)
						move(i, j, PieceType.FOOD);
					else if (data[j].compareTo("@") == 0)
						setAntPosGoodCoord(i, j);
					else
						move(i, j, PieceType.NOTHING);
				}
			}

			// Close the input stream
			in.close();
		}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
		eatenFood = 0;
		board.setFoodText(0);
		currentAntRot = AntRotation.RIGHT;
		System.arraycopy(state,0,initialState,0,state.length); 
	}
	
	public void saveMapToFile(String fileName){
		try{
			if (fileName.isEmpty())
				fileName  = "untitledMap";
			
			// Open the file
			FileWriter fstream = new FileWriter("maps/" + fileName, false);
			BufferedWriter out = new BufferedWriter(fstream);

			// Save each row of data
			for (int i=0; i<size; i++) {
				for (int j=0; j<size; j++){					
					if (getPosGoodCoord(i, j) == PieceType.NOTHING)
						fstream.write('0');
					else if (getPosGoodCoord(i, j) == PieceType.FOOD)
						fstream.write('#');
					else if (getPosGoodCoord(i, j) == PieceType.ANT)
						fstream.write('@');
					else
						fstream.write('0');
					
					fstream.write(' ');
				}
				fstream.write('\n');
			}

			// Close the output stream
			out.close();
		}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}
	
	public void resetBoard(){
		for (int i = 0; i < state.length; i++)
			state[i] = PieceType.NOTHING;
		resetAntPos();
	}
	
	public void randomizeBoard(){
		for (int i = 0; i < state.length; i++)
			state[i] = IGARandom.getRDouble() > 0.9 ? PieceType.FOOD : PieceType.NOTHING;
		setAntPosGoodCoord(0,0);
		resetAntPos();
		System.arraycopy(state,0,initialState,0,state.length);
	}
	
	public void restoreInitialState(){
		System.arraycopy(initialState,0,state,0,initialState.length);
		resetAntPos();
	}

	public void setBoardRef(AntBoard antBoard) {
		board = antBoard;
	}
	
	public void forceUpdateBoard() {
		board.redraw();
		board.refresh();
	}
	
	//------- Getters & Setters -------------------------/
	public AntRotation getCurrentAntRot() {
		return currentAntRot;
	}
	public int getEatenFood() {
		return eatenFood;
	}
}