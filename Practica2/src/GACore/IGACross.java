package GACore;

public abstract class IGACross<T> {
	protected IGACromosome<T>[] descendientes;
	public abstract IGACromosome<T>[] cross(IGACromosome<T>[] parents);
	public abstract IGACromosome<T>[] crossGenerico(IGACromosome<T>[] parents);
}
