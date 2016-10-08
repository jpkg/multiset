package jpkg.collection.set;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.koloboke.collect.ObjCursor;
import com.koloboke.collect.map.hash.HashObjIntMap;

import jpkg.mutable.MutableInteger;

/**
 * An iterator for the MultiSet.
 * @author minerguy31
 *
 */
@SuppressWarnings("rawtypes")
public class MultiSetIterator implements Iterator {

	
	private ObjCursor<?> elements;
	private Object cur;
	private int num = 0;
	private boolean hasNext;
	
	
	protected MultiSetIterator(HashObjIntMap<?> elements) {
		this.elements = elements.entrySet().cursor();
		hasNext = this.elements.moveNext();
	}
	
	@Override
	public boolean hasNext() {
		return (num != 0 || hasNext);
	}

	@Override
	public Object next() {
		if(num == 0) {	// If we're out of elements, grab another one
			Entry<?, ?> e = (Entry<?, ?>) elements.elem();
			hasNext = elements.moveNext();
			
			cur = e.getKey();	// Set cur to be the next element
			num = ((Integer) e.getValue()).intValue() - 1;	// Set number of elements
		} else num--;	// If theres still elements left just use one and decrement the counter
		return cur;
	}

}
