package GA;

import GACore.IGACromosome;

public class GAAntPathCromosome extends IGACromosome{
	GAProgramTree tree; // estrategia de rastreo	
	
	public void initCromosome(int minD, int maxD) {
		tree = new GAProgramTree();
		tree.initTree(tree, minD, maxD);
	}

	public void evaluate() {
		
	}
	
	public IGACromosome clone() {
		GAAntPathCromosome clone = new GAAntPathCromosome();
		//GAStudentGene newGene = ((GAStudentGene) gen).clone();
		//clone.setGene(newGene);

		clone.setEvaluatedValue(evaluatedValue);

		return clone;
	}
	
	public boolean equals(IGACromosome c) {
		/*int[] myGenes, theirGenes;
		int groupSize = ((GAStudentGene) gen).getGroupSize();
		
		myGenes = ((GAStudentGene) gen).getGen();
		theirGenes = ((GAStudentGene)((GAStudentCromosome) c).getGene()).getGen();
		
		for(int i=0; i < myGenes.length;i=+groupSize){
				if (! checkGroup(Arrays.copyOfRange(myGenes, i, i+groupSize), Arrays.copyOfRange(theirGenes, i, i+groupSize)))
					return false;
		}		*/
		return true;
	}
}
