package GACore;

public abstract class IGACross {
	public abstract IGACromosome[] cross(IGACromosome[] parents);
	
	protected boolean pertenece (int x,int[] l){
		int i=0;
		boolean res=false;
		while(!res && i<l.length) 
			if(l[i]!=x)i++;
			else res=true;
		return res;
	}
	
	protected int getIndex(int x, int[] l){
		int i=0;
		boolean res = false;
		while(!res && i<l.length){
			if(l[i]!=x)i++;
			else res= true;
		}
		return i;
	}
}
