package idh.java;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MyLinkedList<T> implements List<T> {

    /**
     * Helper class for the list elements
     */
    private class ListElement {
	T payload;
	ListElement next = null;

	ListElement(T value) {
	    this.payload = value;
	}
    }

    /**
     * We only need to store the very first element of our list, because it will
     * know whether there is a next element.
     */
    ListElement first;

    @Override
    public int size() {
	// TODO: Implement
	int i = 0;
	// this works, because the List<T> interface inherits from Iterable<T>
	for (T x : this)
	    i++;
	return i;
    }

    @Override
    public boolean contains(Object o) {
	// TODO: Implement

	// this works, because the List<T> interface inherits from Iterable<T>
	for (T x : this)
	    if (o.equals(x)) // o == x
		return true;
	return false;
    }

    @Override
    public boolean remove(Object o) {
	// TODO: Implement
	ListIterator<T> li = this.listIterator();
	boolean r = false;
	while (li.hasNext()) {
	    T element = li.next();
	    if (!r && element.equals(o)) {

		// because the list iterator supports remove(), we can just use it.
		li.remove();
		r = true;
	    }
	}
	return r;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
	// TODO: Implement

	// Create new list elements for the collection to be added
	ListElement firstOfNewList = null, previous = null, currentOfNewList = null;
	// this works because Collection<T> inherits from Iterable<T>
	for (T x : c) {
	    currentOfNewList = new ListElement(x);
	    if (firstOfNewList == null) {
		firstOfNewList = currentOfNewList;
	    } else {
		previous.next = currentOfNewList;
	    }
	    previous = currentOfNewList;
	}
	// Now firstOfNewList is the first element of the list to be inserted, and
	// currentOfNewList is the last one

	// insert the new list at the position
	if (index == 0) {
	    currentOfNewList.next = first;
	    this.first = firstOfNewList;
	} else {
	    ListElement atPosition = getElement(index - 1);
	    if (atPosition == null) {
		return false;
	    }

	    // set the pointers correctly
	    currentOfNewList.next = atPosition.next;
	    atPosition.next = firstOfNewList;
	}
	return true;
    }

    @Override
    public T set(int index, T element) {
	// TODO: Implement
	ListElement listElement = getElement(index);
	// Since we want to return the old value, we need to store it
	T returnValue = listElement.payload;

	// Because we're overwriting, we just have to change the payload of the existing
	// element, without touching any of the next fields
	listElement.payload = element;
	return returnValue;
    }

    @Override
    public void add(int index, T element) {
	// TODO: Implement
	ListElement newElement = new ListElement(element);

	// special case if we want to add something at the beginning of the list
	if (index == 0) {
	    newElement.next = first;
	    first = newElement;
	} else {
	    ListElement atPosition = getElement(index);
	    newElement.next = atPosition.next;
	    atPosition.next = newElement;
	}
    }

    @Override
    public T remove(int index) {
	// TODO: Implement
	T payloadAtFormerPosition;
	if (index == 0) {
	    payloadAtFormerPosition = first.payload;
	    first = first.next;
	} else {
	    ListElement atPreviousPosition = getElement(index - 1);

	    // store the payload of the element (because we want to return it)
	    payloadAtFormerPosition = atPreviousPosition.next.payload;

	    // "Reroute" the pointer around the element to be removed
	    atPreviousPosition.next = atPreviousPosition.next.next;
	}
	return payloadAtFormerPosition;
    }

    @Override
    public boolean isEmpty() {
	return first == null;
    }

    @Override
    public Iterator<T> iterator() {
	return new Iterator<T>() {
	    ListElement next = first;

	    @Override
	    public boolean hasNext() {
		return next != null;
	    }

	    @Override
	    public T next() {
		T ret = next.payload;
		next = next.next;
		return ret;
	    }

	};
    }

    @Override
    public Object[] toArray() {
	return this.toArray(new Object[this.size()]);
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
	if (first == null)
	    first = newListElement;
	else
	    last().next = newListElement;
	return true;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
	for (Object o : c)
	    if (!contains(o))
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
	first = null;
    }

    @Override
    public T get(int index) {
	return getElement(index).payload;
    }

    @Override
    public int indexOf(Object o) {
	if (isEmpty())
	    return -1;
	ListElement current = first;
	int index = 0;
	while (current != null) {
	    if (current.payload.equals(o))
		return index;
	    index++;
	    current = current.next;
	}
	return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
	if (isEmpty())
	    return -1;
	ListElement current = first;
	int index = 0;
	int lastFoundIndex = -1;
	while (current != null) {
	    if (current.payload.equals(o))
		lastFoundIndex = index;
	    index++;
	    current = current.next;
	}
	return lastFoundIndex;
    }

    @Override
    public ListIterator<T> listIterator() {
	return new ListIterator<T>() {

	    ListElement previous = null;
	    ListElement next = first;
	    int index;

	    @Override
	    public boolean hasNext() {
		return next != null;
	    }

	    @Override
	    public T next() {
		previous = next;
		T ret = next.payload;
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
		return index + 1;
	    }

	    @Override
	    public int previousIndex() {
		return index - 1;
	    }

	    @Override
	    public void remove() {
		previous.next = next.next;
	    }

	    @Override
	    public void set(T e) {
		next.payload = e;
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

    /**
     * Internal method that iterates over the list, returning the last element
     * (i.e., the one whose next field is null)
     * 
     * @return
     */
    private ListElement last() {
	if (first == null)
	    return null;
	ListElement current = first;

	while (current.next != null) {
	    current = current.next;
	}
	return current;
    }

    /**
     * Internal method to get the list element (not the value) of the list at the
     * specified index position.
     * 
     * @param index
     * @return
     */
    private ListElement getElement(int index) {
	if (isEmpty())
	    return null;
	ListElement current = first;
	while (current != null) {
	    if (index == 0)
		return current;
	    index--;
	    current = current.next;
	}
	return null;
    }

    /**
     * This wasn't required, but it's very helpful
     */
    @Override
    public String toString() {
	StringBuilder b = new StringBuilder();
	Iterator<T> iter = this.iterator();
	b.append('[');
	if (iter.hasNext())
	    b.append(iter.next().toString());
	while (iter.hasNext()) {
	    b.append(",");
	    b.append(iter.next().toString());
	}
	b.append(']');
	return b.toString();

    }

    /**
     * This method is used in the main function to verify that different methods
     * return the correct result.
     * 
     * @param message  A comment for the test
     * @param expected The expected value
     * @param actual   The actual value
     */
    private static void testReturn(String message, Object expected, Object actual) {
	StringBuilder b = new StringBuilder();

	b.append(message).append('\n');
	b.append(" Expected: ").append(expected.toString()).append('\n');
	b.append(" Actual: ").append(actual.toString());
	System.out.println(b.toString());
    }

    public static void main(String[] args) {
	MyLinkedList<String> list = new MyLinkedList<String>();

	testReturn("size() with an empty list", 0, list.size());
	testReturn("add()", true, list.add("Hallo"));
	testReturn("size() after add()", 1, list.size());
	testReturn("get(0)", "Hallo", list.get(0));
	testReturn("toString()", "[Hallo]", list.toString());
	testReturn("add()", true, list.add("Welt"));
	testReturn("toString()", "[Hallo,Welt]", list.toString());
	testReturn("get(1)", "Welt", list.get(1));

	list.add(0, "Achtung");

	testReturn("toString()", "[Achtung,Hallo,Welt]", list.toString());
	testReturn("set()", "Hallo", list.set(1, "Hello"));
	testReturn("toString()", "[Achtung,Hello,Welt]", list.toString());
	testReturn("remove()", "Hello", list.remove(1));
	testReturn("toString()", "[Achtung,Welt]", list.toString());
	testReturn("addAll()", true, list.addAll(1, Arrays.asList("I", "am", "an", "example")));
	testReturn("toString()", "[Achtung,I,am,an,example,Welt]", list.toString());
	testReturn("addAll()", true, list.addAll(0, Arrays.asList("I", "am", "an", "example")));
	testReturn("toString()", "[Achtung,I,am,an,example,Welt]", list.toString());

	list.clear();
	testReturn("size() after clear()", 0, list.size());

    }

}
