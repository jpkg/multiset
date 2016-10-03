package jpkg.collection.set;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;

import org.junit.Test;

import jpkg.test.TestManager;

public class MultiSetTest {
	
	public static void main(String[] args) {
		test();
		TestManager.runTests();
	}
	
	public static void test() {
		TestManager.addTest(MultiSetTest.class);
	}
	
	@Test
	public void testMultiSet() {
		MultiSet<String> test_set = new MultiSet<>();
		test_set.add("hi");
		test_set.add("hi");
		
		assertEquals(test_set.size(), 2);
		
		test_set.add("hello");

		assertEquals(test_set.size(), 3);
	}
	
	@Test
	public void testIterator() {
		ArrayList<String> lines = new ArrayList<>();
		MultiSet<String> test_set = new MultiSet<>();

		lines.add("hi");
		lines.add("hi");
		lines.add("hi");
		lines.add("hello");
		lines.add("hello!");
		lines.add("i am a test");
		
		test_set.addAll(lines);
		
		@SuppressWarnings("unchecked")
		Iterator<String> i = test_set.iterator();
		
		int linecnt = test_set.size();
		
		assertEquals(6, linecnt);
		
		while(i.hasNext()) {
			String s = i.next();
			assertTrue(lines.remove(s));
			assertEquals(--linecnt, lines.size());
		}
		
		assertEquals(lines.size(), 0);
	}
	
	@Test
	public void testRemove() {
		MultiSet<String> test_set = new MultiSet<>();
		
		test_set.add("hi");
		test_set.add("hi");
		test_set.add("hi");
		test_set.add("hi");
		test_set.add("hello");
		
		assertEquals(5, test_set.size());
		
		assertTrue(test_set.remove("hi"));
		
		assertEquals(4, test_set.size());
		
		assertTrue(test_set.remove("hello"));
		
		assertEquals(3, test_set.size());
		
		assertFalse(test_set.remove("hello"));
		
		assertEquals(3, test_set.size());
		
		assertTrue(test_set.removeAllOf("hi"));
		
		assertEquals(0, test_set.size());
		
	}
}
