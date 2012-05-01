package GA;

import java.util.Iterator;
import java.util.PriorityQueue;

import GACore.IGACromosome;
import GACore.IGARandom;
import GACore.IGASelector;

public class GARankingSelection extends IGASelector{

	@Override
	public IGACromosome[] select(IGACromosome[] pop, int pop_size)
			throws InstantiationException, IllegalAccessException {
		
		PriorityQueue<nodoRank> nuevoRank = new PriorityQueue<nodoRank>();
		
		for(int i=0;i<pop_size;i++){
			nuevoRank.add(new nodoRank(pop[i],i));
		}
		
		Iterator<nodoRank> pilaiterator = nuevoRank.iterator();
		int posicionRank = 1;
		float probAcumulada=0;
		while(pilaiterator.hasNext()){
			float nuevaprobabilidad=nuevaProb(pop_size,posicionRank);
			nodoRank nodoDelRank = pilaiterator.next();
			nodoDelRank.setPunt(nuevaprobabilidad);
			probAcumulada= probAcumulada + nuevaprobabilidad;
			nodoDelRank.setAcu_punt(probAcumulada);
			posicionRank++;
		}
		
		GAStudentCromosome[] pobAux = new GAStudentCromosome[pop_size];
		
		for(int i=0;i<pop_size;i++){
			double probAleatoria = IGARandom.getRDouble();
			boolean found = false;
			pilaiterator = nuevoRank.iterator();
			while(!found && pilaiterator.hasNext()){
				nodoRank nodoDelRank=pilaiterator.next();
				if(probAleatoria<nodoDelRank.getAcu_punt() &&
						probAleatoria>=(nodoDelRank.getAcu_punt()-nodoDelRank.getPunt())){
					// Si hemos dado en el clavo con la probabilidad aleatoria
					pobAux[i]=(GAStudentCromosome) pop[nodoDelRank.getPos()];
					found=true;
				}
			}
		}
		
		return pobAux;
	}
	
	
	public static float nuevaProb(int numCroms, int pos){
		float betta=(float) 1.5;
		float res= (pos - 1);
		res = res /(numCroms - 1);
		res = 2*(betta - 1)* res;
		res= (betta-(res))*1/numCroms;
		return res;
	}
	
	public class nodoRank implements Comparable<nodoRank>{
		
		private float acu_punt;
		private float punt;
		private int pos;
		
		private IGACromosome cromosoma;
				
		public nodoRank(IGACromosome c,int p){
			cromosoma = c;
			setPos(p);
		}
		
		public int compareTo(nodoRank o) {
			if(this.cromosoma.getAcum_Score()<o.cromosoma.getAcum_Score()) return -1;
			if(this.cromosoma.getAcum_Score()==o.cromosoma.getAcum_Score()) return 0;
			else 
				return 1;
		}

		public void setAcu_punt(float acu_punt) {
			this.acu_punt = acu_punt;
		}

		public float getAcu_punt() {
			return acu_punt;
		}

		public void setPunt(float punt) {
			this.punt = punt;
		}

		public float getPunt() {
			return punt;
		}

		public void setPos(int pos) {
			this.pos = pos;
		}

		public int getPos() {
			return pos;
		}
		
		
	}
	
}
