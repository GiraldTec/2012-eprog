package GACore;

public abstract class IGAGene {
	protected Object gen;
	
	public abstract void replace(int pos, IGAGene other);
	public abstract Boolean mutate(double prob,int length);
	
	public IGAGene(Object o) {
		gen = o;
	}
	
	public Object getGen() {
		return gen;
	}
	public void setGen(Object value) {
		this.gen = value;
	}

	
	
}
