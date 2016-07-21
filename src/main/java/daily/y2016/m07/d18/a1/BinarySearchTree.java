package daily.y2016.m07.d18.a1;

public class BinarySearchTree<T extends Comparable<? super T>> {

	private static class BinaryNode<T> {
		T element;
		BinaryNode<T> left;
		BinaryNode<T> right;
		
		BinaryNode(T theElement) {
			this(theElement, null, null);
		}
		BinaryNode(T theElement, BinaryNode<T> lt, BinaryNode<T> rt) {
			element = theElement;
			left = lt;
			right = rt;
		}
	}
	
	public BinaryNode<T> root;
	
	public BinarySearchTree() {
		root = null;
	}
	
	public void makeEmpty() {
		root = null;
	}
	public boolean isEmpty() {
		return root ==null;
	}
	
	public boolean contains(T x) {
		return contains(x, root);
	}
	
	public T findMin(){
		if(isEmpty())
			throw new UnderflowException();
		return findMin(root).element;
	}
	public T findMax() {
		if(isEmpty())
			throw new UnderflowException();
		return findMax(root).element;
	}
	public void insert(T x) {
		root = insert(x, root);
	}
	public void remove(T x) {
		root = remove(x, root);
	}
	
	private boolean contains(T x, BinaryNode<T> t) {
		if(t==null)
			return false;
		
		int compareResult = x.compareTo(t.element);
		
		if(compareResult<0)
			return contains(x, t.left);
		else if(compareResult>0)
			return contains(x, t.right);
		else 
			return true;
	}
	private BinaryNode<T> findMin(BinaryNode<T> t) {
		if(t==null)
			return null;
		else if(t.left==null)
			return t;
		
		return findMin(t.left);
	}
	
	
	
	
}
