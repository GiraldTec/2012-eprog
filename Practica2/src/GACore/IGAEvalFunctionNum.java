package GACore;

public abstract class IGAEvalFunctionNum extends IGAEvalFunction {
	// extremos del intervalo considerado para los valores del dominio
	protected double xmax, xmin, ymax, ymin;
	protected int numVars=1;
	
	public int calcCromLenghtGeneric(double precision) {
		return (int) Math.ceil((Math.log(1 + (Double)(xmax - xmin)/precision))/Math.log(2));
	}
	
	//----------  Getters  ------------
	public double getXmax() {
		return xmax;
	}
	public double getXmin() {
		return xmin;
	}
	public double getYmax() {
		return ymax;
	}
	public double getYmin() {
		return ymin;
	}
	public double getType() {
		return type;
	}
	public int getNumVars() {
		return numVars;
	}
	
}
