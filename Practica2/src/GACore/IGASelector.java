package GACore;

public abstract class IGASelector{
	public abstract IGACromosome[] select (IGACromosome[] pop, int pop_size) throws InstantiationException, IllegalAccessException;
}
