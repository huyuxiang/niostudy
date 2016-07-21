package daily.template.datastructures.ch03;

public class MyLinkedStack<T> {

	private static final int DEFAULT_CAPACITY = 10;
	
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
			throw new RuntimeException("stack is empty");
		return top.data;
	}
	
	public T pop() {
		if(isEmpty())
			throw new RuntimeException("stack is empty");
		T t = top.data;
		top = top.next;
		theSize--;
		return t;
	}
	
	public boolean push(T x) {
		Node newTop = new Node(top, x);
		top = newTop;
		theSize ++;
		return true;
	}
	
	public void print() {
		
		for(Node current = top; current!=null;current = current.next) {
			System.out.println(current.data);
		}
	}
	
	public static void main(String args[]) {
		MyLinkedStack stack = new MyLinkedStack();
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
