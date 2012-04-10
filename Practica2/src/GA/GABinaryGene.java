package GA;

import GACore.IGAGene;
import GACore.IGARandom;

public class GABinaryGene extends IGAGene{

	public GABinaryGene(Boolean[] b){
		super(b);
	}
	
	@Override
	public void replace(int pos, IGAGene other) {
		((Boolean[])this.gen)[pos]=((GABinaryGene)other).getBit(pos);
	}
	
	public Boolean mutate(double prob,int genLenght){
		boolean hasmutated = false;
		for(int pos=0;pos<genLenght;pos++){
			double rand_prob_Mutate = IGARandom.getRDouble();
			if(rand_prob_Mutate<prob){
				hasmutated=true;
				((Boolean[])this.gen)[pos]=!((Boolean[])this.gen)[pos];
			}
		}
		return hasmutated;
	}
	
	public Boolean getBit(int pos){
		return ((Boolean[])this.gen)[pos];
	}

}
