package idh.java;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class MyLinkedList<T> implements List<T> {

	/**
	 * We only need to store a dummy -1th element of our list.
	 * It nows whether there is a next element.
	 */
	ListElement prefirst = new ListElement(null);
	
	
	@Override
	public int size() {
		return prefirst.size() - 1;
	}

	@Override
	public boolean isEmpty() {
		return prefirst.next == null;
	}

	@Override
	public boolean contains(Object o) {
		// TODO Implement!
		for (T x : this)
			if (o.equals(x))
				return true;
		return false;
	}

	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			ListElement next = prefirst.next;
			
			@Override
			public boolean hasNext() {
				return next != null;
			}

			@Override
			public T next() {
				T ret = next.value;
				next = next.next;
				return ret;
			}
		};
	}

	@Override
	public Object[] toArray() {
		// TODO Implement!
		return toArray(new Object[size()]);
	}

	@Override
	public <E> E[] toArray(E[] a) {
		if (a.length < size()) {
			a = (E[]) new Object[size()];
		}
		int i = 0;
		for (T t : this) {
			a[i++] = (E) t;
		}
		return a;
	}

	@Override
	public boolean add(T e) {
		ListElement newListElement = new ListElement(e);
		last().next = newListElement;
		return true;
	}

	@Override
	public boolean remove(Object o) {
		// TODO Implement!
		ListIterator<T> li = this.listIterator();
		boolean r = false;
		while(li.hasNext()) {
			T element = li.next();
			if (!r && element.equals(o)) {
				li.remove();
				r = true;
			}
		}
		return r;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		for (Object o : c)
			if (! contains(o))
				return false;
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		for (T t : c) 
			this.add(t);
		return true;
	}

	@Override
	public boolean addAll(int index, Collection<? extends T> c) {
		// TODO Implement!

		// Create a new linked list for the collection
		ListElement first=null, previous=null,current=null;
		for (T x : c) {
			current = new ListElement(x);
			if (first == null) {
				first = current;
			} else {
				previous.next = current;
			}
			previous = current;
		}
		
		// insert the new list at the position
		ListElement atPosition = getElement(index-1);
		if (atPosition == null) {
			return false;
		}
		current.next = atPosition.next;
		atPosition.next = first;
		
		return true;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		boolean r = true;
		for (Object o : c) 
			r = r || this.remove(o);
		return r;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		prefirst.next = null;
	}

	@Override
	public T get(int index) {
		try {
			return getElement(index).value;
		} catch (NullPointerException e) {
			throw new IndexOutOfBoundsException();
		}
	}

	@Override
	public T set(int index, T element) {
		// TODO Implement!

		ListElement le = getElement(index);
		T ret = le.value;
		le.value = element;
		return ret;
	}

	@Override
	public void add(int index, T element) {
		// TODO Implement!

		ListElement atPosition = getElement(index-1);
		ListElement newElement = new ListElement(element);
		newElement.next = atPosition.next;
		atPosition.next = newElement;
	}

	@Override
	public T remove(int index) {
		// TODO Implement!

		ListElement atPreviousPosition = getElement(index-1);
		T ret = atPreviousPosition.next.value;
		atPreviousPosition.next  = atPreviousPosition.next.next;
		return ret;
	}

	@Override
	public int indexOf(Object o) {
		// TODO Implement!

		int i = 0;
		for (T x : this) {
			if (o.equals(x))
				return i;
			i++;
		}
		return -1;
	}

	@Override
	public int lastIndexOf(Object o) {
		// TODO Implement!

		int lastIndex = -1;
		int index = 0;
		for (T x : this)  {
			if (o.equals(x))
				lastIndex = index;
			index++;
		}
		return lastIndex;
	}

	@Override
	public ListIterator<T> listIterator() {
		return new ListIterator<T>() {

			ListElement previous = null;
			ListElement next = prefirst;
			int index;
			
			@Override
			public boolean hasNext() {
				return next != null;
			}

			@Override
			public T next() {
				previous = next;
				T ret = next.value;
				next = next.next;
				index++;
				return ret;
			}

			@Override
			public boolean hasPrevious() {
				return false;
			}

			@Override
			public T previous() {
				throw new UnsupportedOperationException();
			}

			@Override
			public int nextIndex() {
				return index+1;
			}

			@Override
			public int previousIndex() {
				return index-1;
			}

			@Override
			public void remove() {
				previous.next = next.next;
			}

			@Override
			public void set(T e) {
				next.value = e;
			}

			@Override
			public void add(T e) {
				throw new UnsupportedOperationException();				
			}
			
		};
	}

	@Override
	public ListIterator<T> listIterator(int index) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<T> subList(int fromIndex, int toIndex) {
		throw new UnsupportedOperationException();
	}
	
	public String toString() {
		StringBuilder b = new StringBuilder();
		Iterator<T> iter = this.iterator();
		b.append('[');
		if (iter.hasNext())
			b.append(iter.next().toString());
		while(iter.hasNext()) {
			b.append(",");
			b.append(iter.next().toString());
		}
		b.append(']');
		return b.toString();
		
	}
	
	private class ListElement {
		T value;
		ListElement next;
		
		ListElement(T value) {
			this.value = value;
		}
		
		public int size() {
			if (next == null)
				return 1;
			return next.size() + 1;
		}
	}
	
	/**
	 * Internal method that iterates over the list, returning the last element (i.e., the one whose next field is null)
	 * @return
	 */
	private ListElement last() {
		ListElement current = prefirst;
		
		while(current.next != null) {
			current = current.next;
		}
		return current;
	}
	
	/** 
	 * Internal method to get the list element (not the value) of the list at the specified index position.
	 * @param index
	 * @return
	 */
	private ListElement getElement(int index) {
		if (isEmpty()) 
			return null;
		if (index == -1)
			return prefirst;
		ListElement current = prefirst.next;
		while(current != null) {
			if (index == 0) 
				return current;
			index--;
			current = current.next;
		}
		return null;
	}
	
	/**
	 * This method is used in the main function to verify that different methods return the correct result.
	 * @param message A comment for the test
	 * @param expected The expected value
	 * @param actual The actual value
	 */
	private static int testReturn(String message, Object expected, Object actual) {
		StringBuilder b = new StringBuilder();
		
		b.append(message).append('\n');
		b.append(" Expected: ").append(expected.toString()).append('\n');
		b.append(" Actual: ").append(actual.toString());
		System.out.println(b.toString());
		if (expected.equals(actual))
			return 0;
		return 1;
	}
		

	public static void main(String[] args) {
		MyLinkedList<String> list = new MyLinkedList<String>();
		
		int errors = 0;
		
		errors += testReturn("size() with an empty list", 0, list.size());
		errors += testReturn("add()", true, list.add("Hallo"));
		errors += testReturn("size() after add()", 1, list.size());
		errors += testReturn("get(0)", "Hallo", list.get(0));
		errors += testReturn("toString()", "[Hallo]", list.toString());
		errors += testReturn("add()", true, list.add("Welt"));
		errors += testReturn("toString()", "[Hallo,Welt]", list.toString());
		errors += testReturn("get(1)", "Welt", list.get(1));
		list.add(0, "Achtung");
		errors += testReturn("toString()", "[Achtung,Hallo,Welt]", list.toString());
		errors += testReturn("set()", "Hallo", list.set(1, "Hello"));
		errors += testReturn("toString()", "[Achtung,Hello,Welt]", list.toString());
		errors += testReturn("remove()", "Hello", list.remove(1));
		errors += testReturn("toString()", "[Achtung,Welt]", list.toString());
		errors += testReturn("addAll()", true, list.addAll(1, Arrays.asList("I", "am", "an", "example")));
		errors += testReturn("toString()", "[Achtung,I,am,an,example,Welt]", list.toString());
		
		list.clear();
		errors += testReturn("size() after clear()", 0, list.size());
		
		System.out.println();
		System.out.println("========================");
		System.out.println("Summary: " + String.valueOf(errors) +" errors.");
		

	}
}
