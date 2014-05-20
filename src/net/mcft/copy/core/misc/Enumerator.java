package net.mcft.copy.core.misc;

import java.util.Iterator;
import java.util.NoSuchElementException;

/** C#-like Enumerator base class, makes life much easier. */
public abstract class Enumerator<T> implements Iterator<T> {
	
	private boolean checkNext = true;
	
	@Override
	public final boolean hasNext() {
		if (!checkNext) return true;
		if (moveNext()) {
			checkNext = false;
			return true;
		} else return false;
	}
	
	@Override
	public final T next() {
		if (!hasNext())
			throw new NoSuchElementException();
		checkNext = true;
		return current();
	}
	
	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
	
	/** Attempts to move to the next element in the enumeration.
	 *  Returns if there is a next element. */
	public abstract boolean moveNext();
	
	/** Returns the current element in the enumeration. */
	public abstract T current();
	
	
	/** Returns an enumerator version of the iterator. */
	public static <T> Enumerator<T> of(Iterator<T> iterator) {
		return ((iterator instanceof Enumerator)
				? (Enumerator<T>)iterator
				: new EnumerableWrapper<T>(iterator));
	}
	/** Returns an enumerator version of the iterable's iterator. */
	public static <T> Enumerator<T> of(Iterable<T> iterable) {
		return of(iterable.iterator());
	}
	
	
	private static class EnumerableWrapper<T> extends Enumerator<T> {
		
		private final Iterator<T> iterator;
		
		private T current = null;
		private boolean reachedEnd = false;
		
		public EnumerableWrapper(Iterator<T> iterator) {
			this.iterator = iterator;
		}
		
		@Override
		public boolean moveNext() {
			if (iterator.hasNext() && !reachedEnd) {
				current = iterator.next();
				return true;
			} else {
				reachedEnd = true;
				return false;
			}
		}
		@Override
		public T current() {
			if (reachedEnd)
				throw new NoSuchElementException();
			return current;
		}
		
	}
	
}
