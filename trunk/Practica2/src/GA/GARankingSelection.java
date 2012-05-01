package GA;

import java.util.Arrays;
import java.util.Collections;
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
		int probAcumulada=0;
		while(pilaiterator.hasNext()){
			double nuevaprobabilidad=nuevaProb(pop_size,posicionRank);
			nodoRank nodoDelRank = pilaiterator.next();
			nodoDelRank.setPunt(nuevaprobabilidad);
			probAcumulada+=nuevaprobabilidad;
			nodoDelRank.setAcu_punt(probAcumulada);
			posicionRank++;
		}
		
		GAStudentCromosome[] pobAux = new GAStudentCromosome[pop_size];
		
		for(int i=0;i<pop_size;i++){
			double probAleatoria = IGARandom.getRDouble();
			boolean found = false;
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
	
	
	public static double nuevaProb(int numCroms, int pos){
		double betta=1.5;
		return (betta-(2*(betta - 1)*((pos - 1)/(numCroms - 1))))/numCroms;
	}
	
	public class nodoRank implements Comparable<nodoRank>{
		
		private double acu_punt;
		private double punt;
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

		public void setAcu_punt(double acu_punt) {
			this.acu_punt = acu_punt;
		}

		public double getAcu_punt() {
			return acu_punt;
		}

		public void setPunt(double punt) {
			this.punt = punt;
		}

		public double getPunt() {
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
