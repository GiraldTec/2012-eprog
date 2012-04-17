package GACore;

public abstract class IGACross {
	protected IGACromosome[] descendientes;
	public abstract IGACromosome[] cross(IGACromosome[] parents);
	public abstract IGACromosome[] crossGenerico(IGACromosome[] parents);
}
