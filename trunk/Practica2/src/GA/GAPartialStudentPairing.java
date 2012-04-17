package GA;

import GACore.IGACromosome;
import GACore.IGACross;

public class GAPartialStudentPairing extends IGACross{

	@Override
	public IGACromosome[] cross(IGACromosome[] parents) {
		
		
		GAStudentCromosome parent1 = (GAStudentCromosome) parents[0];
		GAStudentCromosome parent2 = (GAStudentCromosome) parents[1];
		
		// TODO Auto-generated method stub
		
		
		return null;
	}

	@Override
	public IGACromosome[] crossGenerico(IGACromosome[] parents) {
		// TODO Auto-generated method stub
		return null;
	}

}
