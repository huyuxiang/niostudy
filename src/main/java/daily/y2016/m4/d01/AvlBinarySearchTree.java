package daily.y2016.m4.d01;

public class AvlBinarySearchTree <T extends Comparable<? super T>> {
	
	private class AvlNode<T> {
		T element;
		AvlNode<T> left;
		AvlNode<T> right;
		public int height;
		
		public AvlNode(T theElement) {
			this(theElement, null, null) ;
		}
		
		public AvlNode(T theElement, AvlNode<T> left, AvlNode<T> right) {
			this.element = theElement;
			this.left = left;
			this.right = right;
		}
	}
	
	AvlNode<T> root;
	
	public AvlBinarySearchTree() {
		root = null;
	}
	
	public boolean insert(T x) {
		root = insert(x, root);
		return true;
	}
	
	private int height(AvlNode<T> node) {
		if(node==null) {
			return -1;
		}
		int height = Math.max(height(node.left), height(node.right)) + 1;
		return height;
	}
	
	private AvlNode<T> insert(T x, AvlNode<T> node) {
		if(node==null) {
			return new AvlNode(x, null, null);
		}
		
		int compareResult = x.compareTo(node.element);
		
		if(compareResult>0) {
			node.right = insert(x, node.right);
			
			
			int height = Math.max(height(node.right) , height(node.left)) + 1;
			node.height = height;
			if( height(node.right.left) - height(node.right.right) == 2 ) {
				node.right = rotateWithRight(node);
			} else if( height(node.right.right) - height(node.right.left) ==2) {
				node.right = doubleWithRight(node.right);
			}
			return node;
		}
		
	}

	private AvlBinarySearchTree<T>.AvlNode<T> doubleWithRight(AvlBinarySearchTree<T>.AvlNode<T> k2) {
		AvlNode<T> k1 = k2.right;
		k2.right = k1.left;
		k1.right = k2;
		k2.height= Math.max(height(k2.left) , height(k2.right)) + 1;
		k1.height = Math.max(height(k1.left) , k2.height)  +1;
		return k1;
	}

	private AvlBinarySearchTree<T>.AvlNode<T> rotateWithRight(AvlBinarySearchTree<T>.AvlNode<T> k3) {
		k3.
		return null;
	}
	
	
}
