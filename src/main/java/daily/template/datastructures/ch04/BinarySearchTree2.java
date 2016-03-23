package daily.template.datastructures.ch04;

//checked 20160314
public class BinarySearchTree2<T extends Comparable<? super T>> {
	
	private static class AvlNode<T> {
		T element;			//the data in the node
		AvlNode<T> left;	//left child
		AvlNode<T> right;	//child
		int height;			//height
		
		//Constructors
		AvlNode(T theElement) {
			this(theElement , null, null);
		}
		
		AvlNode(T theElement, AvlNode<T> lt, AvlNode<T> rt) {
			element = theElement;
			left = lt;
			right = rt;
			height = 0;
		}
	}
	
	// return the height of node t, or -1, if null.
	private int height(AvlNode<T> t) {
		return t==null?-1:t.height;
	}
	
	/*
	 * internal method to insert into a subtree.
	 */
	
	private AvlNode<T> insert(T x, AvlNode<T> t) {
		if(t==null)
			return new AvlNode<T>(x, null, null);
		
		int compareResult = x.compareTo( t.element);
		
		if(compareResult<0) {
			t.left = insert(x, t.left);
			if(height(t.left) - height(t.right) == 2) 
				if(x.compareTo( t.left.element) < 0)
					t = rotateWithLeftChild(t);
				else 
					t = doubleWithLeftChild(t);
		} else if(compareResult > 0) {
			t.right = insert(x, t.right);
			if(height(t.right) - height(t.left)==2) 
				if(x.compareTo( t.right.element) > 0)
					t = rotateWithRightChild(t);
				else 
					t = doubleWithRightChild(t);
		} else 
			; //duplicate; do nothing
		
		t.height = Math.max(height(t.left), height(t.right)) + 1;
		return t;
	}
	/*
	 * rotate binary tree node with left child.
	 * for avl trees, this is a single rotation for case 1.
	 * update height, then return new root.
	 */
	private AvlNode<T> rotateWithLeftChild(AvlNode<T> k2) {
		AvlNode<T> k1 = k2.left;
		k2.left = k1.right;
		k1.right = k2;
		k2.height = Math.max(height(k2.left), height(k2.right)) + 1;
		k1.height = Math.max(height(k1.left), k2.height) + 1;
		return k1;
	}
	
	/*
	 * double rotate binary tree node: first left child
	 * with its right child; then node k3 with new left child.
	 * for avl trees, this is a double rotation for case 2.
	 * update height, then return new root.
	 */
	private AvlNode<T> doubleWithLeftChild(AvlNode<T> k3) {
		k3.left = rotateWithRightChild(k3.left);
		return rotateWithLeftChild(k3);
	}
	
}
