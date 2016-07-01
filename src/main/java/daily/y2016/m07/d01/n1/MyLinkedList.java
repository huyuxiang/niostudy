package daily.y2016.m07.d01.n1;

public class MyLinkedList<T> implements Iterable<T> {

	private int theSize;
	private int modCount = 0;
	private Node<T> beginMarker;
	private Node<T> endMarker;
	
	private static class Node<T> {
		public T data;
		public Node<T> prev;
		public Node<T> next;
		
		public Node(T d, Node<T> p, Node<T> n){
			data = d;
			prev = p;
			next = n;
		}
	}
}
