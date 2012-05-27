package GA;


public class GAProgramTree {

	private byte operator; 
	/* **************
	 * 	Tipos:
	 * **************
	 * 
	 *  -1 -- > Nothing
	 * 	 0 -- > Avanza
	 *   1 -- > Izquierda
	 *   2 -- > Derecha
	 *   3 -- > SiComidaDelante
	 *   4 -- > ProgN2
	 *   5 -- > ProgN3
	 *   
	 * **************
	 * */
	private GAProgramTree leftSon,centerSon,rigthSon,father;
		
	public GAProgramTree(){
		setOperator((byte) -1);
		leftSon = null;
		centerSon = null;
		rigthSon = null;
		setFather(null);
	}
	
	public GAProgramTree clone(){
		GAProgramTree clon = new GAProgramTree();
		
		clon.setOperator((byte)this.getOperator());
		clon.setLeftSon(this.getLeftSon().clone());
		clon.setCenterSon(this.getCenterSon().clone());
		clon.setRigthSon(this.getRigthSon().clone());
		clon.setFather(this.getFather().clone());
		
		return clon;
	}
	
	public void setOperator(byte operator) {
		this.operator = operator;
	}

	public GAProgramTree getLeftSon() {
		return leftSon;
	}

	public void setLeftSon(GAProgramTree leftSon) {
		this.leftSon = leftSon;
	}

	public GAProgramTree getCenterSon() {
		return centerSon;
	}

	public void setCenterSon(GAProgramTree centerSon) {
		this.centerSon = centerSon;
	}

	public GAProgramTree getRigthSon() {
		return rigthSon;
	}

	public void setRigthSon(GAProgramTree rigthSon) {
		this.rigthSon = rigthSon;
	}

	public short getOperator() {
		return operator;
	}

	public void setFather(GAProgramTree father) {
		this.father = father;
	}
	public GAProgramTree getFather() {
		return father;
	}


}
