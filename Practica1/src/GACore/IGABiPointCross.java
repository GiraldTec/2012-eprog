package GACore;

public abstract class IGABiPointCross<T> extends IGACross<T>{

	@Override
	public IGACromosome<T>[] crossGenerico(IGACromosome<T>[] parents) {
		int point_CrossA = IGARandom.getRInt(parents[0].getLength()/2);
		int point_CrossB = IGARandom.getRInt(parents[0].getLength()/2)+(parents[0].getLength()/2);
		int cromosome_Lenght = parents[0].getLength();
		int nGenes= parents[0].getNumberGenes();
		
		descendientes[0].initCromosome(parents[0].getLength());
		descendientes[1].initCromosome(parents[0].getLength());
		
		for(int j=0;j<nGenes;j++){
		// primera parte del intercambio: 1 a 1 y 2 a 2
			for (int i=0; i<point_CrossA; i++) {
				((IGAGene)descendientes[0].getGene(j)).replace(i,((IGAGene)parents[0].getGene(j)));
				((IGAGene)descendientes[1].getGene(j)).replace(i,((IGAGene)parents[1].getGene(j)));
			}		
			// segunda parte: 1 a 2 y 2 a 1
			for (int i=point_CrossA; i<point_CrossB; i++) {
				((IGAGene)descendientes[0].getGene(j)).replace(i,((IGAGene)parents[1].getGene(j)));
				((IGAGene)descendientes[1].getGene(j)).replace(i,((IGAGene)parents[0].getGene(j)));
			}		
			for (int i=point_CrossB; i<cromosome_Lenght; i++) {
				((IGAGene)descendientes[0].getGene(j)).replace(i,((IGAGene)parents[0].getGene(j)));
				((IGAGene)descendientes[1].getGene(j)).replace(i,((IGAGene)parents[1].getGene(j)));
			}
		}
		// se evalúan
		descendientes[0].calcFenotype();
		descendientes[0].evaluate();
		descendientes[1].calcFenotype();
		descendientes[1].evaluate();
		return descendientes;
	}

}
