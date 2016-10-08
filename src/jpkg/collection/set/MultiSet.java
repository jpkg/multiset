package jpkg.collection.set;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;

import com.koloboke.collect.ObjCursor;
import com.koloboke.collect.map.hash.HashObjIntMap;
import com.koloboke.collect.map.hash.HashObjIntMapFactory;
import com.koloboke.collect.map.hash.HashObjIntMaps;
import com.koloboke.function.ObjIntConsumer;

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
	
	private HashObjIntMap<T> elements;
	private int count = 0;
	
	/**
	 * Constructs a new MultiSet
	 */
	public MultiSet() {
		elements = HashObjIntMaps.newMutableMap();
	}
	
	/**
	 * Constructs a new MultiSet with specified initial capacity
	 * @param initialCapacity
	 */
	public MultiSet(int initialCapacity) {
		elements = HashObjIntMaps.newMutableMap(initialCapacity);
	}

	@Override
	public boolean add(T arg0) {
		count++;
		
		elements.addValue(arg0, 1, 0);
		
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

	@SuppressWarnings("unchecked")
	@Override
	public boolean remove(Object arg0) {

		if(!elements.containsKey(arg0)) {
			return false;
		} else {
			int i = elements.getInt(arg0);
			count--;
			if(i <= 1)
				elements.removeAsInt(arg0);
			else
				elements.replace((T) arg0, i - 1);
		}
		
		return true;
	}
	
	public boolean removeAllOf(Object arg0) {
		count -= elements.getOrDefault(arg0, 0);
		elements.removeAsInt(arg0);
		return true;
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
		HashObjIntMap<T> newmap = HashObjIntMaps.newMutableMap(elements.size());
		int newcount = 0;
		@SuppressWarnings("unchecked")
		Iterator<T> i = arg0.iterator();
		while(i.hasNext()) {
			T o = i.next();
			
			
			if(elements.containsKey(o)) {
				int e = elements.getInt(o);
				newmap.put(o, e);
				newcount += e;
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

	@SuppressWarnings("deprecation")
	@Override
	public Object[] toArray() {
		final ArrayList<Object> ret = new ArrayList<>(count);
		elements.forEach((ObjIntConsumer<T>) (arg0, arg1) -> {
			for(int i = 0; i < arg1; i++)
				ret.add(arg0);
		});
		
		return ret.toArray();
	}

	@Override
	public <U> U[] toArray(U[] arg0) {
		final ArrayList<U> ret = new ArrayList<>(count);
		elements.forEach((ObjIntConsumer<T>) (a, b) -> {
			for(int i = 0; i < b; i++)
				ret.add((U) a);
		});
		
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
	
	/**
	 * Pick a random element from this multiset.
	 * 
	 * @param rnd
	 * @return
	 */
	public T getRandomElement(Random rnd) {
		int index = rnd.nextInt(count);
		
		ObjCursor<Entry<T, Integer>> iter = elements.entrySet().cursor();
		
		while(iter.moveNext()) {
			Entry<T, Integer> elem = iter.elem();
			
			index -= elem.getValue();
			
			if(index <= 0)
				return elem.getKey();
		}
		
		throw new AssertionError();
	}
}
