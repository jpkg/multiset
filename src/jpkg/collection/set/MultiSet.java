package jpkg.collection.set;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import jpkg.mutable.MutableInteger;

/**
 * A multiset implementation backed by a HashMap. 
 * 
 * @author minerguy31
 *
 * @param <T> Type held in this multiset
 */
public class MultiSet<T> implements Collection<T>, Serializable {

	private static final long serialVersionUID = -64069305605773622L;
	
	private HashMap<T, MutableInteger> elements;
	private int count = 0;
	
	/**
	 * Constructs a new MultiSet
	 */
	public MultiSet() {
		elements = new HashMap<>();
	}
	
	/**
	 * Constructs a new MultiSet with specified initial capacity
	 * @param initialCapacity
	 */
	public MultiSet(int initialCapacity) {
		elements = new HashMap<>(initialCapacity);
	}

	@Override
	public boolean add(T arg0) {
		count++;
		
		MutableInteger i = elements.get(arg0);
		if(i == null)
			elements.put(arg0, new MutableInteger(1));
		else
			i.increment();
		
		return true;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean addAll(Collection arg0) {
		arg0.forEach((obj) -> { add((T) obj); });
		return true;
	}

	@Override
	public void clear() {
		elements.clear();
	}

	@Override
	public boolean contains(Object arg0) {
		return elements.containsKey(arg0);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean containsAll(Collection arg0) {
		Iterator<?> i = arg0.iterator();
		
		while(i.hasNext()) {
			if(!elements.containsKey(i.next()))
					return false;
		}
		
		return true;
	}

	@Override
	public boolean isEmpty() {
		return elements.isEmpty();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Iterator iterator() {
		return new MultiSetIterator(elements);
	}

	@Override
	public boolean remove(Object arg0) {
		
		MutableInteger i = elements.get(arg0);
		if(i == null) {
			return false;
		} else {
			count--;
			if(i.get() <= 1)
				elements.remove(arg0);
			else i.decrement();
			
			return true;
		}
	}
	
	public boolean removeAllOf(Object arg0) {
		MutableInteger i = elements.get(arg0);
		if(i == null) {
			return false;
		} else {
			elements.remove(arg0);
			count -= i.get();
			return true;
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean removeAll(Collection arg0) {
		boolean ret = false;
		
		Iterator<?> i = arg0.iterator();
		while(i.hasNext()) {
			ret |= removeAllOf(i.next());
		}
		
		return ret;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public boolean retainAll(Collection arg0) {
		HashMap<T, MutableInteger> newmap = new HashMap<>();
		int newcount = 0;
		@SuppressWarnings("unchecked")
		Iterator<T> i = arg0.iterator();
		while(i.hasNext()) {
			T o = i.next();
			
			MutableInteger e = elements.get(o);
			
			if(e != null) {
				newmap.put(o, e);
				newcount += e.get();
			}
		}
		
		if(newcount != count) {
			elements = newmap;
			count = newcount;
			return true;
		}
		
		return false;
	}

	@Override
	public int size() {
		return count;
	}

	@Override
	public Object[] toArray() {
		final ArrayList<Object> ret = new ArrayList<>(count);
		elements.forEach((a, b) -> {
			for(int i = 0; i < ((MutableInteger) b).get(); i++)
				ret.add(a);
			}
		);
		
		return ret.toArray();
	}

	@Override
	public <U> U[] toArray(U[] arg0) {
		final ArrayList<Object> ret = new ArrayList<>(count);
		elements.forEach((a, b) -> {
			for(int i = 0; i < ((MutableInteger) b).get(); i++)
				ret.add(a);
			}
		);
		
		return ret.toArray(arg0);
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	public boolean equals(Object another) {
		MultiSet m;
		
		if(another instanceof MultiSet)
			m = (MultiSet) another;
		else return false;
		
		if(m.count == this.count && m.elements.equals(this.elements))
			return true;
		return false;
	}
	
	@Override
	public String toString() {
		return this.elements.toString();
	}
	
	@Override
	public int hashCode() {
		return elements.hashCode() ^ (count * 31);
	}
}
