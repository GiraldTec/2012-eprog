package GA;

public class GAStudent {
	
	protected int[] haters;
	protected double result;
	protected int people;  //#de gente que odia
	
	public GAStudent(int populationSize, double result){
		haters = new int[populationSize];
		people = 0;
		this.result = result;
	}
	
	public void addHater(int haterID){
		people++;
		haters[people] = haterID;
	}
	
	public double getResult(){
		return result;
	}
	
	public int[] getHaters(){
		return haters;
	}
	
	public int getPeople(){
		return people;
	}

}
