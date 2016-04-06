package daily.template.datastructures.ch04;

import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

import daily.template.datastructures.ch03.MyQueue;
import daily.template.datastructures.ch03.MyQueue.QueueNode;
import daily.template.datastructures.ch04.AvlBinarySearchTree.AvlNode;

public class AvlBinarySearchTreePrintMain {

	public static int maxHeight = -1;
	//maxHeight 最大层元素数
	private static volatile int maxFloorNum = -1;
	private static int maxFloorHold = -1;
	static int ELEMENT_HOLD = 4;
	
	private static MyQueue<AvlNode> floorCacheQueue;
	
	private static ReentrantLock printLock = new ReentrantLock(true);
	
	private static void printTree(AvlNode t) {
		if(t!=null){
			printTree(t.left);
			System.out.println(t.element);
			printTree(t.right);
		}
	}
	public static int height(AvlNode t) {
		if(t==null) {
			return -1;
		}
		int height = Math.max(height(t.left), height(t.right)) + 1;
		return height;
	}
	
	public static void print(AvlBinarySearchTree tree) {
		try {
		if(!printLock.tryLock()) 
			throw new RuntimeException("print is call by others");
		
		maxHeight = height(tree.root);
		floorCacheQueue = new MyQueue((int) Math.pow(2, maxHeight+1) + 1);
		maxFloorNum = (int) (Math.pow(2, maxHeight));//最底层满树下 多少个元素
		maxFloorHold = maxFloorNum * ELEMENT_HOLD;//最底层占用空间
		floorCacheQueue.offer(tree.root);
		floorCacheQueue.offerSeperator();
		
		printFloor(maxHeight);
		} finally {
			if(printLock.isHeldByCurrentThread())
				printLock.unlock();
		}
		
	}
	
	private static void printFloor(int height) {
		if(height==-1) {
			return;
		}
		int elementSpace = ((maxFloorHold-countFloorElement(height)*ELEMENT_HOLD)) / countFloorElement(height);
		println();
		
		int offsetSpace = elementSpace / 2;
		printString(getBlank(offsetSpace));
		
		while(true) {
			QueueNode<AvlNode> queueNode = floorCacheQueue.poll();
			if(queueNode.equals(MyQueue.NULL)) {
				floorCacheQueue.offerSeperator();
				printFloor(--height);
				return ;
			} 
			AvlNode bNode = queueNode.element;
			if(bNode == null) {
				floorCacheQueue.offer(null);
				floorCacheQueue.offer(null);
			} else {
				floorCacheQueue.offer(bNode.left);
				floorCacheQueue.offer(bNode.right);
			}
			printAvlNode(bNode);
			printString(getBlank(elementSpace));
		}
	}
	
	public static void printAvlNode(AvlNode<Integer> node) {
		if(node ==null) {
			printString(getBlank(ELEMENT_HOLD));
			return;
		}
		Integer element = node.element;
		int remaining = ELEMENT_HOLD - element.toString().length();
		StringBuffer buffer = new StringBuffer(element.toString());
		if(remaining > 0) {
			buffer.insert(0, getBlank(remaining));
		}
		printString(buffer.toString());
	}
	
	private static final String BLANK_STR = " ";
	private static String getBlank(int blankNum) {
		StringBuffer buffer = new StringBuffer();
		for(int i=0;i<blankNum;i++) {
			buffer.append(BLANK_STR);
		}
		return buffer.toString();
	}
	
	//在高度为height的层上有 多少个元素
	private static int countFloorElement(int height) {
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
	
	
	public static void main(String[] args) {
		AvlBinarySearchTree<Integer> tree = new AvlBinarySearchTree<Integer>();
		/*for(int i=0;i<100;i++) {
			tree.insert(new Random().nextInt(1000));
		}*/
		/*tree.insert(94);
		tree.insert(70);
		tree.insert(39);
		tree.insert(75);
		tree.insert(100);
		tree.insert(98);
		tree.insert(26);
		tree.insert(115);
		tree.insert(162);*/
		for(int i=10;i<17;i++) {
			tree.insert(i);
			print(tree);
			println();
			System.out.println("---------------");
		}
		for(int i=5;i<10;i++) {
			tree.insert(i);
			print(tree);
			println();
			System.out.println("---------------");
		}
		for(int i=1;i<5;i++) {
			tree.insert(i);
			print(tree);
			println();
			System.out.println("---------------");
		}
		
		//----- remove
		
		for(int i=1;i<10;i++) {
			tree.remove(i);
			print(tree);
			println();
			System.out.println("---------------");
		}/**/
		/*for(int i=10;i<17;i++) {
			tree.remove(i);
			print(tree);
			println();
			System.out.println("---------------");
		}*/
	}
}
