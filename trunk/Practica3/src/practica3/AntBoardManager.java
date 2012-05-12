package practica3;

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

	public void setAntPos(int i, int j) {
		for (int t = 0; t < state.length; t++){
			if (state[t] == PieceType.ANT){
				state[t] = PieceType.NOTHING;
				break;
			}
		}
		setPosValue(i, j, PieceType.ANT);		
	}	
}