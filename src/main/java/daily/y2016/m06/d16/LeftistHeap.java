package daily.y2016.m06.d16;

import daily.template.datastructures.ch04.UnderflowException;

public class LeftistHeap<T extends Comparable<? super T>> {

	private LeftistNode<T> root ;
	
	private static class LeftistNode<T> {
		LeftistNode(T theElement) {
			this(theElement, null, null);
		}
		
		LeftistNode(T theElement, LeftistNode<T> lt, LeftistNode<T> rt) {
			element = theElement;
			left = lt;
			right = rt;
			npl = 0;
		}
		
		T element;
		LeftistNode<T> left;
		LeftistNode<T> right;
		int npl;
	}
	
	public LeftistHeap() {
		root = null;
	}
	
	public void merge(LeftistHeap<T> rhs) {
		if(this==rhs) 
			return ;
	
		
		root = merge(root, rhs.root);
		rhs.root = null;
	}
	
	private LeftistNode<T> merge(LeftistNode<T> h1, LeftistNode<T> h2) {
		if(h1 ==null) 
			return h2;
		if(h2==null) 
			return h1;
		
		if(h1.element.compareTo(h2.element)<0)
			return merge1(h1, h2);
		else 
			return merge1(h2, h1);
	}
	
	private LeftistNode<T> merge1(LeftistNode<T> h1, LeftistNode<T> h2) {
		
		if(h1.left ==null)
			h1.left = h2;
		else {
			h1.right = merge(h1.right, h2);
			if(h1.left.npl<h1.right.npl)
				swapChildren(h1);
			h1.npl = h1.right.npl + 1;
		}
		return h1;
	}
	
	private static <T> void swapChildren(LeftistNode<T> t) {
		LeftistNode<T> tmp = t.left;
		t.left = t.right;
		t.right = tmp;
	}
	
	public void insert(T x) {
		root = merge(new LeftistNode<>(x), root);
	}
	
	public boolean isEmpty(){
		return root ==null;
	}
	
	public void makeEmpty() {
		root = null;
	}
	
	public T findMin() {
		if(isEmpty()) 
			throw new UnderflowException();
		
		return root.element;
	}
	
	public T deleteMin() {
		if(isEmpty())
			throw new UnderflowException();
		
		T minItem = root.element;
		root = merge(root.left, root.right);
		
		return minItem;
	}
	
	
}
