package daily.template.datastructures.ch04;

import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

import daily.template.datastructures.ch03.MyQueue;
import daily.template.datastructures.ch03.MyQueue.QueueNode;

//checked 20160314
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
	public T findMin() {
		if(isEmpty())
			throw new UnderflowException();
		return findMin(root).element;
	}
	public T findMax() {
		if(isEmpty()) 
			throw new UnderflowException();
		return findMax(root).element;
	}
	public  void insert(T x) {
		root = insert(x, root);
	}
	public void remove(T x) {
		root = remove(x, root);
	}
	public void printTree() {
		if(isEmpty())
			System.out.println("Empty tree");
		else 
			printTree(root);
	}
	
	
	private boolean contains(T x, BinaryNode<T> t) {
		if(t == null)
			return false;
		
		int compareResult = x.compareTo(t.element);
		
		if(compareResult<0)
			return contains(x, t.left);
		else if(compareResult>0) 
			return contains(x, t.right);
		else 
			return true; //Match
	}
	private BinaryNode<T> findMin(BinaryNode<T> t) {
		if(t==null)
			return null;
		else if(t.left ==null)
			return t;
		
		return findMin(t.left);
	}
	private BinaryNode<T> findMax(BinaryNode<T> t) {
		if(t !=null)
			while(t.right!=null)
				t = t.right;
		return t;
	}
	
	
	private BinaryNode<T> insert(T x, BinaryNode<T> t) {
		if(t==null)
			return new BinaryNode<T>(x, null, null);
		
		int compareResult = x.compareTo(t.element);
		
		if(compareResult <0)
			t.left = insert(x, t.left);
		else if(compareResult>0)
			t.right = insert(x, t.right);
		else 
			; //Duplicate; do nothing
		return t;
	}
	private BinaryNode<T> remove(T x, BinaryNode<T> t) {
		if(t==null)
			return t; //Item not found; do nothing
		
		int compareResult = x.compareTo(t.element);
		
		if(compareResult<0)
			t.left = remove(x, t.left);
		else if(compareResult>0)
			t.right = remove(x, t.right);
		else if(t.left!=null&&t.right!=null) { //Two children
			t.element = findMin(t.right).element;
			t.right = remove(t.element, t.right);
		} else 
			t = (t.left!=null)?t.left:t.right;
		return t;
	}
	private void printTree(BinaryNode<T> t) {
		if(t!=null){
			printTree(t.left);
			System.out.println(t.element);
			printTree(t.right);
		}
	}
	public int height(BinaryNode<T> t) {
		if(t==null) {
			return -1;
		}
		int height = Math.max(height(t.left), height(t.right)) + 1;
		return height;
	}
	
	public int maxHeight = -1;
	//maxHeight 最大层元素数
	private volatile int maxFloorNum = -1;
	private int maxFloorHold = -1;
	static final int ELEMENT_HOLD = 4;
	
	private static MyQueue<BinaryNode> floorCacheQueue;
	
	private static ReentrantLock printLock = new ReentrantLock(true);
	
	public  void print() {
		try {
		if(!printLock.tryLock()) 
			throw new RuntimeException("print is call by others");
		
		maxHeight = height(root);
		floorCacheQueue = new MyQueue((int) Math.pow(2, maxHeight+1) + 1);
		maxFloorNum = (int) (Math.pow(2, maxHeight));//最底层满树下 多少个元素
		maxFloorHold = maxFloorNum * ELEMENT_HOLD;//最底层占用空间
		floorCacheQueue.offer(root);
		floorCacheQueue.offerSeperator();
		
		printFloor(maxHeight);
		} finally {
			if(printLock.isHeldByCurrentThread())
				printLock.unlock();
		}
		
	}
	
	private void printFloor(int height) {
		if(height==-1) {
			return;
		}
		int elementSpace = ((maxFloorHold-countFloorElement(height)*ELEMENT_HOLD)) / countFloorElement(height);
		println();
		
		int offsetSpace = elementSpace / 2;
		printString(getBlank(offsetSpace));
		
		while(true) {
			QueueNode<BinaryNode> queueNode = floorCacheQueue.poll();
			if(queueNode.equals(MyQueue.NULL)) {
				floorCacheQueue.offerSeperator();
				printFloor(--height);
				return ;
			} 
			BinaryNode bNode = queueNode.element;
			if(bNode == null) {
				floorCacheQueue.offer(null);
				floorCacheQueue.offer(null);
			} else {
				floorCacheQueue.offer(bNode.left);
				floorCacheQueue.offer(bNode.right);
			}
			printBinaryNode(bNode);
			printString(getBlank(elementSpace));
		}
	}
	
	public void printBinaryNode(BinaryNode<T> node) {
		if(node ==null) {
			printString(getBlank(ELEMENT_HOLD));
			return;
		}
		T element = node.element;
		int remaining = ELEMENT_HOLD - element.toString().length();
		StringBuffer buffer = new StringBuffer(element.toString());
		if(remaining > 0) {
			buffer.insert(0, getBlank(remaining));
		}
		printString(buffer.toString());
	}
	
	private static final String BLANK_STR = " ";
	private String getBlank(int blankNum) {
		StringBuffer buffer = new StringBuffer();
		for(int i=0;i<blankNum;i++) {
			buffer.append(BLANK_STR);
		}
		return buffer.toString();
	}
	
	//在高度为height的层上有 多少个元素
	private int countFloorElement(int height) {
		int n = maxHeight - height;
		int count = (int) (Math.pow(2, n));
		return count;
	}
	
	//---
	public static void printString(String s) {
		System.out.print(s);
	}
	
	public static void println() {
		System.out.println();
	}
	
	
	public static void main(String[] args) throws InterruptedException {
		BinarySearchTree<Integer> tree = new BinarySearchTree<Integer>();
		/*for(int i=0;i<100;i++) {
			tree.insert(new Random().nextInt(99));
		}*/
		/*tree.insert(94);
		tree.insert(70);
		tree.insert(39);
		tree.insert(75);
		tree.insert(100);
		tree.insert(98);
		tree.insert(26);
		tree.insert(115);
		tree.insert(162);
		tree.print();*/
		for(int i=1;i<=5;i++) {
			tree.insert(i);
			tree.print();
			println();
			System.out.println("---------------");
		}
	}

}
