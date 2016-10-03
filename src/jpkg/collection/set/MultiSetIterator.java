package jpkg.collection.set;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import jpkg.mutable.MutableInteger;

/**
 * An iterator for the MultiSet.
 * @author minerguy31
 *
 */
@SuppressWarnings("rawtypes")
public class MultiSetIterator implements Iterator {

	
	private Iterator<?> elements;
	private Object cur;
	private int num = 0;
	
	
	protected MultiSetIterator(HashMap<?, MutableInteger> elements) {
		this.elements = elements.entrySet().iterator();
	}
	
	@Override
	public boolean hasNext() {
		return (num != 0 || elements.hasNext());
	}

	@Override
	public Object next() {
		if(num == 0) {	// If we're out of elements, grab another one
			Entry<?, ?> e = (Entry<?, ?>) elements.next();
			
			cur = e.getKey();	// Set cur to be the next element
			num = ((MutableInteger) e.getValue()).get() - 1;	// Set number of elements
		} else num--;	// If theres still elements left just use one and decrement the counter
		return cur;
	}

}
