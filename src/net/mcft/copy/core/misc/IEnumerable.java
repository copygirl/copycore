package net.mcft.copy.core.misc;

public interface IEnumerable<T> extends Iterable<T> {
	
	@Override
	public Enumerator<T> iterator();
	
}
