package daily.y2016.m06.d16.a1;

import daily.template.datastructures.ch04.UnderflowException;

public class BinaryHeap<T extends Comparable<? super T>> {

	private static final int DEFAULT_CAPACITY = 10;
	
	private int currentSize;
	private T [] array;
	
	public BinaryHeap() {
		this(DEFAULT_CAPACITY);
	}
	
	public BinaryHeap(int capacity) {
		currentSize = 0;
		array = (T[]) new Comparable[capacity+1];
	}
	
	private void percolateDown(int hole) {
		int child ;
		T tmp = array[hole];
		
		for(;hole*2<=currentSize;hole=child) {
			child = hole*2;
			if(child!=currentSize&&array[child+1].compareTo(array[child])<0)
				child++;
			
			if(array[child].compareTo(tmp)<0)
				array[hole] = array[child];
			else 
				break;
		}
		array[hole] = tmp;
	}
	
	private void buildHeap() {
		for(int i=currentSize/2;i>0;i--)
			percolateDown(i);
	}
	
	public BinaryHeap(T[] items){
		currentSize = items.length;
		array = (T[]) new Comparable[(currentSize + 2) * 11 /10];
		
		int i = 1;
		for(T item: items) 
			array[i++] = item;
		
		buildHeap();
	}
	
	public boolean isEmpty() {
		return currentSize == 0;
	}
	
	public void makeEmpty() {
		currentSize = 0;
	}
	
	public T findMin() {
		if(isEmpty()) 
			throw new UnderflowException();
		
		return array[1];
	}
	
	public T deleteMin() {
		if(isEmpty())
			throw new UnderflowException();
		
		T minItem = findMin();
		array[1] = array[currentSize--];
		percolateDown(1);
		
		return minItem;
	}
	
	private void enlargeArray(int newSize) {
		T [] old = array;
		array = (T[])new Comparable[newSize];
		for(int i=0;i<old.length;i++)
			array[i] = old[i];
	}
	
	public void insert(T x) {
		if(currentSize ==array.length-1)
			enlargeArray(array.length*2+1);
		
		int hole = ++currentSize;
		for(array[0] = x;x.compareTo(array[hole/2])<0;hole/=2)
			array[hole] = array[hole/2];
		array[hole] = x;
	}
}
