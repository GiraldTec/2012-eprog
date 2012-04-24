package GACore;

public abstract class IGAEvalFunction {
	protected int type;
	public abstract Object evaluate(int numberParams,Double[] params); //devuelve el valor de la funci�n evaluaci�n
	public abstract int calcCromLenghtGeneric(double precision);
	public abstract int calcCromLenght(double precision);
	public abstract double getType();
}
