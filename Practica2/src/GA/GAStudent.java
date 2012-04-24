package GA;

import java.util.ArrayList;

public class GAStudent {
	private int id;
	private double result;
	private ArrayList<Integer> haters;
		
	public GAStudent(int id, double result){
		this.id = id;
		this.result = result;
	}
		
	public double getResult(){
		return result;
	}
	
	public ArrayList<Integer> getHaters(){
		return haters;
	}

	public Integer getId() {
		return id;
	}
}
