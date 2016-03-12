package daily.y2016.m3.d12.a1;

import java.util.Iterator;

public class MyLinkedList<T> implements Iterable<T> {
	
	private int theSize;
	private Node<T> beginMarker;
	private Node<T> endMarker;
	
	private class Node<T> {
		public Node<T> prev;
		public Node<T> next;
		public T data;
		
		public Node(T d, Node<T> p, Node<T> n) {
			this.data = d;
			this.prev = p;
			this.next = n;
		}
	}
	
	public MyLinkedList() {
		beginMarker = new Node<T>(null, null, null);
		endMarker = new Node<T>(null, beginMarker, null);
		beginMarker.next = endMarker;
		
		theSize = 0;
	}
	public int size() {
		return theSize;
	}
	
	public boolean isEmpty() {
		return size()==0;
	}
	
	public boolean add(T t) {
		addBefore(endMarker, t);
		return true;
	}
	
	public boolean add(int idx, T t) {
		addBefore(getNode(idx), t);
		return true;
	}
	
	public Node<T> getNode(int idx) {
		if(idx<0||idx>size()-1) {
			throw new RuntimeException();
		}
		
		Node<T> marker = null;
		if(idx>size()/2) {
			marker = endMarker;
			for(int i=size();i>idx;i--) {
				marker = marker.prev;
			}
		} else {
			marker = beginMarker.next;
			for(int i=0;i<idx;i++) {
				marker = marker.next;
			}
			
		}

		return marker;
	}
	
	public void addBefore(Node<T> prev, T t) {
		
		Node<T> newNode = new Node(t, prev.prev, prev);
		newNode.prev.next = newNode;
		newNode.next.prev = newNode;
		
		theSize ++;
	}
	
	public T get(int idx) {
		return getNode(idx).data;
	}
	
	public void remove(int idx) {
		remove(getNode(idx));
	}
	
	public void remove(Node<T> d) {
		
		d.prev.next = d.next;
		d.next.prev = d.prev;
		
		theSize --;
	}
	
	public Iterator<T> iterator() {
		return new MyLinkedListIterator();
	}
	
	private class MyLinkedListIterator implements Iterator<T> {
		
		public Node<T> current;
		
		public MyLinkedListIterator() {
			current = beginMarker.next;
		}
		
		public boolean hasNext() {
			return current != endMarker;
		}
		
		public T next() {
			if(!hasNext()) {
				throw new RuntimeException();
			}
			
			Node<T> old = current;
			current = current.next;
			return old.data;
		}
		public void remove() {
			MyLinkedList.this.remove(current.prev);
		}
		
	}
	
}
