package GA;

import GACore.IGAEvalFunction;
import GACore.IGAEvalFunctionNum;

public class GANumericEvalFuncs {
	private static int NUMFUNCTIONS = 5;
	private int fun5_N;
	
	public GANumericEvalFuncs(int fun5_N)
	{
		this.fun5_N = fun5_N;
	}

	public IGAEvalFunction getEvaluatorFunction(int numFunct) {
		if (numFunct > 0 && numFunct <= NUMFUNCTIONS)
		{
			switch (numFunct) {
				case 1: return new GAFunction1();
				case 2: return new GAFunction2();
				case 3: return new GAFunction3();
				case 4: return new GAFunction4();
				case 5: return new GAFunction5(fun5_N);
			}
		}		
		return null;
	}

	private final class GAFunction1 extends IGAEvalFunctionNum {		
		public GAFunction1()
		{
			xmax = 1.0;
			xmin = 0.0;
			type = 1;
			numVars = 1;
		}
		
		public Double evaluate(int numberParams,Double[] params) {
			if (params[0] != null)
				return calcFunction((params[0]));
			else
				return Double.NEGATIVE_INFINITY;
		}
  
		private double calcFunction(double x) {
			if (x >= 0 && x <= 1)
				return x + Math.abs(Math.sin(32*Math.PI*x));
			else 
				return Double.NEGATIVE_INFINITY;
		}
		public int calcCromLenght(double precision){
			return calcCromLenghtGeneric(precision);
		}
	}
	
	private final class GAFunction2 extends IGAEvalFunctionNum {
		
		public GAFunction2(){
			xmax = 12.1;
			xmin = -3.0;
			ymax = 5.8;
			ymin = 4.1;
			type = 2;
			numVars = 2;
		}
		public Double evaluate(int numberParams,Double[] params) {
			if (params[0] != null && params[1] != null)
				return calcFunction((Double) params[0], (Double) params[1]);
			else
				return Double.NEGATIVE_INFINITY;
		}

		private double calcFunction(double x, double y) {
			if (x >=xmin && x <= 12.1 && y >= 4.1 && y <= 5.8)
				return 21.5 + x*Math.sin(4*Math.PI*x) + y*Math.sin(20*Math.PI*y);
			else
				return Double.NEGATIVE_INFINITY;
		}
		
		public int calcCromLenght(double precision){
			return calcCromLenghtGeneric(precision/2);
		}
	}
	
	private final class GAFunction3 extends IGAEvalFunctionNum {
		public GAFunction3(){
			xmax = 25.0;
			xmin = 0.0;
			type = 3;
			numVars = 1;
		}
		public Double evaluate(int numberParams,Double[] params) {
			if (params[0] != null)
				return calcFunction((Double) (params[0]));
			else
				return Double.NEGATIVE_INFINITY;
		}

		private double calcFunction(double x) {
			if(x<=xmax && x>=xmin)
				return Math.sin(x)/(1+Math.sqrt(x)+(Math.cos(x)/(1+x)));
			else
				return Double.NEGATIVE_INFINITY;
		}
		public int calcCromLenght(double precision){
			return calcCromLenghtGeneric(precision);
		}
	}
	
	private final class GAFunction4 extends IGAEvalFunctionNum {
		public GAFunction4(){
			xmax = 10.0;
			xmin = -10.0;
			ymax = 10.0;
			ymin = -10.0;
			type = 4;
			numVars = 2;
		}
		public Double evaluate(int numberParams,Double[] params) {
			if (params[0] != null && params[1] != null)
				return calcFunction((Double) (params[0]),(Double) (params[1]));
			else
				return Double.NEGATIVE_INFINITY;
		}

		private double calcFunction(double x1, double x2) {
			double firstHalf = 0;
				for(int i=1;i<=5;i++){
					firstHalf += i*Math.cos((1+i)*x1 +i);
				}
			double secondHalf = 0;
				for(int i=1;i<=5;i++){
					secondHalf += i*Math.cos((1+i)*x2 +i);
				}
			return firstHalf*secondHalf;
		}
		
		public int calcCromLenght(double precision){
			return calcCromLenghtGeneric(precision/2);
		}
	}
	
	private final class GAFunction5 extends IGAEvalFunctionNum{
        public GAFunction5(int numVars){
                xmax = Math.PI;
                xmin = 0;
                type = 5;
                this.numVars = numVars;
        }
        public Object evaluate(int numberParams,Double[] params) {
                boolean correctParams=true;
                for(int i=0;(i<numberParams && correctParams);i++){
                        correctParams = (params[i]!=null);
                }
                if(!correctParams) 
                	return Double.NEGATIVE_INFINITY;
                else
                    return calcFunction(numberParams,params);
        }
        private double calcFunction(int numberParams,Double[] params){
                double resultado =0;
                for(int i=0;i<numberParams;i++){
                        resultado += Math.sin(params[i])*Math.pow(Math.sin(((i+2)*Math.pow(params[i],2))/Math.PI),20);
                }
                return -resultado;
        }
        public int calcCromLenght(double precision) {
                return calcCromLenghtGeneric(precision);
        }
}
	
}
