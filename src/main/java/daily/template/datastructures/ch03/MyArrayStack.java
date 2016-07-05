package daily.template.datastructures.ch03;

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
		return topOfStack == -1;
	}
	
	public T top() {
		if(topOfStack <0) 
			throw new ArrayIndexOutOfBoundsException();
		T x = theArray[topOfStack];
		return x;
	}
	public T pop() {
		if(topOfStack <0) 
			throw new ArrayIndexOutOfBoundsException();
		T x = theArray[topOfStack--];
		theSize--;
		return x;
	}
	
	public boolean push(T x) {
		if(topOfStack >=theArray.length-1) 
			throw new ArrayIndexOutOfBoundsException();
		theArray[++topOfStack] = x;
		theSize++;
		return true;
	}
	
	public void print() {
		if(isEmpty())
			return ;
		for(int i=topOfStack;i>=0;i--){
			System.out.println(theArray[i]);
		}
	}
	
	public static void main(String args[]) {
		MyArrayStack stack = new MyArrayStack();
		stack.push (5);
		stack.push (4);
		stack.push (3);
		stack.push (2);
		stack.push (1);
		stack.push (0);
	//	stack.push (0);stack.push (0);stack.push (0);stack.push (0);stack.push (0);stack.push (0);
		
		stack.print();
		System.out.println("....");
		stack.pop();
		stack.print();
		stack.pop();
		stack.print();
		stack.pop();
		stack.print();
		stack.pop();
		stack.print();
		stack.pop();
		stack.print();
		stack.pop();
		stack.print();
		stack.pop();
		stack.print();
		
	}
}
