package daily.y2016.m3.d20.a2;

public class BinarySearchTree<T extends Comparable <? super T>> {
	
	private static class BinaryNode<T> {
		T element;
		BinaryNode<T> left;
		BinaryNode<T> right;
		
		BinaryNode(T theElement) {
			this(theElement, null, null);
		}
		
		BinaryNode(T theElement, BinaryNode<T> left, BinaryNode<T> right) {
			element = theElement;
			this.left = left;
			this.right = right;
		}
	}
	
	private BinaryNode<T> root;
	
	public BinarySearchTree() {
		clear();
	}
	
	public void clear() {
		root =null;
	}
	
	public boolean isEmpty() {
		return root ==null;
	}
	
	public boolean add(T t) {
		root = add(root, t);
		return true;
	}
	
	public BinaryNode<T> add(BinaryNode<T> node, T t) {
		if(node==null) 
			return new BinaryNode<T>(t, null, null);
		
		int compareResult = node.element.compareTo(t);
		if(compareResult>0) {
			node.right = add(node.left, t);
		} else if(compareResult<0){
			node.left = add(node.right, t);
		} else 
			;
		return node;
	}
	
	public boolean contains(T x) {
		if(root==null)
			return false;
		return contains(root, x);
	}
	
	public boolean contains(BinaryNode<T> node, T x) {
		if(node==null) {
			return false;
		}
		
		int compareResult = node.element.compareTo(x);
		if(compareResult>0) {
			return contains(node.left, x);
		} else if(compareResult<0) {
			return contains(node.right, x);
		} else 
			return true;
	}
	
	public T findMin() {
		if(root==null) 
			throw new RuntimeException("root = null");
		return findMin(root);
	}
	
	private T findMin(BinaryNode<T> node) {
		if(node==null)
			return null;
		if(node.left!=null){
			return findMin(node.left);
		}
		return node.element;
	}

	public T findMax() {
		if(root==null) 
			throw new RuntimeException("root = null");
		return findMax(root);
	}
	
	private T findMax(BinaryNode<T> node) {
		while(node.right!=null) {
			node = node.right;
		}
		return node.element;
	}
	
	public boolean remove(T x) {
		if(root==null)
			return false;
		root= remove(root, x);
		return true;
	}
	
	private BinaryNode<T> remove(BinaryNode<T> node, T x) {
		
		if(node==null) {
			throw new RuntimeException("cannot remove it, because it not contains");
		}
		int compareResult = node.element.compareTo(x);
		
		if(compareResult>0) {
			node.left = remove(node.left, x);
		} else if(compareResult<0) {
			node.right = remove(node.right, x);
		} else if(node.left!=null&& node.left!=null) {
			node.element = findMin(node.right);
			node.right = remove(node.right, node.element);
		} else {
			node = (node.right!=null) ?node.right:node.left;
		}
		return node;
	}
	
	public void printTree() {
		printTree(root);
	}
	
	public void printTree(BinaryNode<T> node) {
		if(node!=null) {
			printTree(node.left);
			System.out.println(node.element);
			printTree(node.right);
		}
	}
	
	
}
