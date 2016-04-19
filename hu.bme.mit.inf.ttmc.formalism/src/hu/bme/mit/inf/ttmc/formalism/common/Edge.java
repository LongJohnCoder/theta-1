package hu.bme.mit.inf.ttmc.formalism.common;

public interface Edge<L extends Loc<L, E>, E extends Edge<L, E>> {

	public L getSource();

	public L getTarget();

}