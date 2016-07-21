package daily.y2016.m07.d08.struct.a3;

public class MyLinkedStack<T> {

	private static final int DEFAULT_CAPACITY = 0;
	
	private static class Node<T> {
		private Node<T> next;
		private T data;
		
		public Node(Node next, T x) {
			this.next = next;
			data = x;
		}
	}
	
	private int theSize;
	private Node<T> top;
	
	public MyLinkedStack() {
		clear();
	}
	public void clear() {
		top = null;
		theSize = 0;
	}
	public boolean isEmpty() {
		return top == null;
	}
	
	public T top() {
		if(isEmpty())
			throw new RuntimeException("stack is Empty");
		return top.data;
	}
	
	public T pop() {
		if(isEmpty())
			throw new RuntimeException("Stack isempty");
		T t = top.data;
		top = top.next;
		theSize--;
		return t;
	}
	
	public boolean push(T x) {
		Node newTop = new Node(top, x);
		top = newTop;
		theSize++;
		return true;
	}
}
