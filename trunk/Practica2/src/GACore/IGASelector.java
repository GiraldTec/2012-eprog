package GACore;

public abstract class IGASelector<T>{
	public abstract IGACromosome<T>[] select (IGACromosome<T>[] pop, int pop_size) throws InstantiationException, IllegalAccessException;
}
