package daily.y2016.m3.d12.a1;

import java.util.Iterator;

public class MyArrayList<T> implements Iterable<T> {

	private int theSize;
	private T[] theItems;
	private static int DEFAULT_CAPACITY = 10;
	
	public MyArrayList() {
		theSize = 0;
		ensureCapacity(size());
	}
	
	public int size() {
		return theSize;
	}
	
	public boolean isEmpty() {
		return size()==0;
	}
	
	public void ensureCapacity(int size) {
		if(theItems.length>size) 
			return;
		T[] oldItems = theItems;
		theItems = (T[]) new Object[size];
	}
	
	public boolean add(T t) {
		add(size(), t);
		return true;
	}
	public boolean add(int idx, T t) {
		if(idx<0||idx>theItems.length) {
			throw new ArrayIndexOutOfBoundsException();
		}
		
		for(int i=size();i>idx;i--) {
			theItems[i] = theItems[i-1];
		}
		theItems[idx] = t;
		theSize++;
		return true;
	}
	
	public T get(int idx) {
		if(idx<0||idx>size()-1) {
			throw new ArrayIndexOutOfBoundsException();
		}
		
		return theItems[idx];
	}
	public T set(int idx, T newVal) {
		if(idx<0||idx>size()-1) {
			throw new ArrayIndexOutOfBoundsException();
		}
		T oldVal = theItems[idx];
		theItems[idx] = newVal;
		return oldVal;
	}
	
	public T remove(int idx) {
		if(idx<0||idx>size()-1) {
			throw new ArrayIndexOutOfBoundsException();
		}
		T removedItem = theItems[idx];
		for(int i=idx;i<size()-1;i++) {
			theItems[idx] = theItems[i+1];
		}
		theSize--;
		return removedItem;
	}
	
	public Iterator<T> iterator() {
		return new MyArrayListIterator();
	}
	
	private class MyArrayListIterator implements Iterator<T> {
		private int current = 0;
		
		public boolean hasNext() {
			return current < size();
		}
		
		public T next() {
			if(!hasNext()) 
				throw new RuntimeException();
			
			return theItems[current++];
			
		}
		
		public void remove() {
			MyArrayList.this.remove(--current);
		}
		
	}
}
