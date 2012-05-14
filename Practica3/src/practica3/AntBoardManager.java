package practica3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import GA.GAStudent;
import GACore.IGARandom;

public class AntBoardManager {
	private int size;
	public enum PieceType {
		NOTHING(0),	PATH(1), FOOD(2), EATENFOOD(3), ANT(4);		
		public int id;		
		PieceType(int i){id = i;}
	};
	private PieceType[] state;

	public AntBoardManager(int newSize) {
		size = newSize;

		state = new PieceType[size * size];
		for (int i = 0; i < state.length; i++)
			state[i] = Math.random() > 0.9 ? PieceType.FOOD : PieceType.NOTHING;
		
		move(0,0,PieceType.PATH);
		move(0,1,PieceType.PATH);
		move(0,2,PieceType.PATH);
		move(0,3,PieceType.PATH);
		move(0,4,PieceType.PATH);
		move(1,4,PieceType.EATENFOOD);
		move(2,4,PieceType.PATH);
		move(3,4,PieceType.PATH);
		move(4,4,PieceType.ANT);
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

	public boolean isLegalPos(int i, int j) {
		return state[i * size + j] == PieceType.NOTHING;
	}
	
	public void move(int i, int j, PieceType p) {
		i = Math.abs(i-31);
		state[i * size + j] = p;
	}
	
	public PieceType getPosGoodCoord(int i, int j) {
		i = Math.abs(i-31);
		return state[i * size + j];
	}

	public void setAntPos(int i, int j) {
		for (int t = 0; t < state.length; t++){
			if (state[t] == PieceType.ANT){
				state[t] = PieceType.NOTHING;
				break;
			}
		}
		setPosValue(i, j, PieceType.ANT);	
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
						move(i, j, PieceType.ANT);
					else
						move(i, j, PieceType.NOTHING);
				}
			}

			// Close the input stream
			in.close();
		}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
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
		move(0,0, PieceType.ANT);
	}
	
	public void randomizeBoard(){
		for (int i = 0; i < state.length; i++)
			state[i] = IGARandom.getRDouble() > 0.9 ? PieceType.FOOD : PieceType.NOTHING;
		move(0,0, PieceType.ANT);
	}
	
}