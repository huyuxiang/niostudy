package daily.y2016.m07.d05.struct.a1;

public class MyArrayStack<T> {

	private static final int DEFAULT_CAPACITY = 10;
	
	private int theSize;
	private T[] theArray;
	
	private int topOfStack;
	
	public MyArrayStack() {
		clear();
	}
	public void clear() {
		topOfStack = -1;
		theSize = 0;
		theArray = (T[]) new Object[DEFAULT_CAPACITY];
	}
	public int size() {
		return theSize;
	}
	public boolean isEmpty() {
		return topOfStack ==-1;
	}
	
	public T top() {
		if(topOfStack <0)
			throw new ArrayIndexOutOfBoundsException();
		T x = theArray[topOfStack];
		return x;
	}
	
	public T pop() {
		if(topOfStack<0)
			throw new ArrayIndexOutOfBoundsException();
		T x = theArray[topOfStack--];
		theSize--;
		return x;
	}
	
	public boolean push(T x) {
		if(topOfStack>=theArray.length-1)
			throw new ArrayIndexOutOfBoundsException();
		theArray[++topOfStack] = x;
		theSize++;
		return true;
	}
}
