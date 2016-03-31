package daily.y2016.m3.d31.a1;

public class AvlBinarySearchTree<T extends Comparable<? super T>> {

	public static class AvlNode<T> {
		T element;
		AvlNode<T> left;
		AvlNode<T> right;
		int height;
		
		AvlNode(T theElement) {
			this(theElement, null, null);
		}
		
		AvlNode(T theElement, AvlNode<T> lt, AvlNode<T> rt) {
			element = theElement;
			left = lt;
			right = rt;
			height = 0;
		}
	}
	
	private int height(AvlNode<T> t) {
		return t==null?-1:t.height;
	}
	
	public AvlNode<T> root;
	
	public AvlBinarySearchTree() {
		root = null;
	}
	
	public void insert(T x) {
		root = insert(x, root);
	}
	
	private AvlNode<T> insert(T x, AvlNode<T> t) {
		if(t==null) 
			return new AvlNode<T>(x, null, null);
		
		int compareResult = x.compareTo(t.element);
		
		if(compareResult<0) {
			t.left = insert(x, t.left);
			if(height(t.left) - height(t.right) ==2) 
				if(x.compareTo(t.left.element) <0)
					t = rotateWithLeftChild(t);
				else 
					t = doubleWithLeftChild(t);
		} else if(compareResult>0) {
			t.right = insert(x, t.right);
			if(height(t.right) -height(t.left) ==2) 
				if(x.compareTo(t.right.element) >0) 
					t = rotateWithRightChild(t);
				else
					t = doubleWithRightChild(t);
		} else 
			;
		
		t.height = Math.max(height(t.left), height(t.right)) + 1;
		return t;
	}
	
	private AvlNode<T> rotateWithLeftChild(AvlNode<T> k2) {
		AvlNode<T> k1 = k2.left;
		k2.left = k1.right;
		k1.right = k2;
		k2.height = Math.max(height(k2.left), height(k2.right)) + 1;
		k1.height = Math.max(height(k1.left), k2.height) + 1;
		return k1;
	}
	
	private AvlNode<T> rotateWithRightChild(AvlNode<T> k2) {
		AvlNode<T> k1 = k2.right;
		k2.right = k1.left;
		k1.left = k2;
		k2.height = Math.max(height(k2.left), height(k2.right)) + 1;
		k1.height = Math.max(k2.height, height(k1.right)) + 1;
		return k1;
	}
	
	private AvlNode<T> doubleWithLeftChild(AvlNode<T> k3) {
		k3.left = rotateWithRightChild(k3.left);
		return rotateWithLeftChild(k3);
	}
	
	private AvlNode<T> doubleWithRightChild(AvlNode<T> k3) {
		k3.right = rotateWithLeftChild(k3.right);
		return rotateWithRightChild(k3);
	}
	
	
}
