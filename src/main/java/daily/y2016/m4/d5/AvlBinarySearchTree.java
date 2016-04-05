package daily.y2016.m4.d5;

import java.util.NoSuchElementException;

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
			left =lt;
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
		root = insert(root, x);
	}
	private AvlNode<T> insert(AvlNode<T> node, T x) {
		if(node==null) 
			return new AvlNode<T>(x, null, null);
		
		int compareResult = x.compareTo(node.element);
		
		if(compareResult>0) {
			node.right = insert(node.right, x);
			node.height = Math.max(height(node.right), height(node.left)) + 1;
			if(height(node.right) - height(node.left) == 2) {
				if(x.compareTo(node.right.element) > 0) {
					node = rotateWithRightChild(node);
				} else {
					node = doubleWithRightChild(node);
				}
			}
		} else if(compareResult<0) {
			node.left = insert(node.left, x) ;
			node.height = Math.max(height(node.right), height(node.left)) + 1;
			if(height(node.left) - height(node.right) == 2) {
				if(x.compareTo(node.left.element)<0) {
					node = rotateWithLeftChild(node);
				} else {
					node = doubleWithLeftChild(node);
				}
			}
		} else {
			;
		}
		return node;
	}
	
	private AvlNode<T> rotateWithRightChild(AvlNode<T> k2) {
		AvlNode<T> k1 = k2.right;
		k2.right = k1.left;
		k1.left = k2;
		k2.height = Math.max(height(k2.left), height(k2.right)) + 1;
		k1.height = Math.max(height(k1.right), k2.height) + 1;
		return k1;
	}
	
	private AvlNode<T> doubleWithRightChild(AvlNode<T> k3) {
		k3.right = rotateWithLeftChild(k3.right);
		return rotateWithRightChild(k3);
	}
	
	private AvlNode<T> rotateWithLeftChild(AvlNode<T> k2) {
		AvlNode<T> k1 = k2.left;
		k2.left = k1.right;
		k1.right = k2;
		k2.height = Math.max(height(k2.left), height(k2.right)) + 1;
		k1.height = Math.max(height(k1.left), k2.height) + 1;
		return k1;
	}
	
	private AvlNode<T> doubleWithLeftChild(AvlNode<T> k3) {
		k3.left = rotateWithRightChild(k3.left);
		return rotateWithLeftChild(k3);
	}
	                                                    
	public void remove(T x) {
		root = remove(root, x);
	}
	
	private AvlNode<T> remove(AvlNode<T> node, T x) {
		if(node==null) 
			throw new NoSuchElementException();
		
		int compareResult = x.compareTo(node.element);
		
		if(compareResult > 0 ) {
			node.right = remove(node.right, x);
			node.height = Math.max(height(node.left), height(node.right)) + 1;
			if(height(node.left) - height(node.right) == 2) {
				if(height(node.left.left) > height(node.left.right)) {
					node = rotateWithLeftChild(node);
				} else {
					doubleWithRightChild(node);
				}
			}
		} else if(compareResult <0) {
			node.left = remove(node.left, x);
			node.height = Math.max(height(node.left), height(node.right)) + 1;
			if(height(node.right) - height(node.left) == 2) {
				// 判断 node.right.left 与 node.right.right 谁大
				if(height(node.right.left) > height(node.right.right)) {
					node = doubleWithRightChild(node);
				} else {
					node = rotateWithRightChild(node);
				}
			}
		} else if(node.left!=null&& node.right!=null) {
			node.element = findMin(node.right);
			node.right = remove(node.right, node.element);
			node.height = Math.max(height(node.left), height(node.right)) + 1;
			if(height(node.left) - height(node.right) == 2) {
				// 判断 node.right.left 与 node.right.right 谁大
				if(height(node.left.right) > height(node.left.left)) {
					node = doubleWithLeftChild(node);
				} else {
					node = rotateWithLeftChild(node);
				}
			}
		} else {
			node = node.left!=null?node.left: node.right;
			// never happen 
			//left.height - right height > 2
		}
		return node;
	}
	
	private T findMin(AvlNode<T> node) {
		if(node ==null) {
			throw new NullPointerException();
		}
		while(node.left!=null) {
			node = node.left;
		}
		return node.element;
	}
	
	
}
