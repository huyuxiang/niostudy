package daily.template.datastructures.ch03;

import java.util.Random;

public class MyQueue<T> {

	
	
	public static final int DEFAULT_SIZE = 10;
	public static final int SIZE_65 = 65;
	public static final int SIZE_129 = 129;
	public static final int SIZE_257 = 257;
	
	public static final QueueNode NULL = new QueueNode();
	
	private QueueNode[] items;
	private int size;
	
	public MyQueue() {
		this(DEFAULT_SIZE);
	}
	
	public static class QueueNode<T> {
		public QueueNode() {
			this(null);
		}
		
		public QueueNode(T t) {
			this.element = t;
		}

		public T element;
	}
	
	public MyQueue(int size) {
		items = new QueueNode[size];
		size = 0;
	}
	
	/*     抛出异常 返回特殊值 
	 插入 add(e) offer(e) 
	 移除 remove() poll() 
	 检查 element() peek() */
	
	/*public boolean offer(T t) {
		if(t==null) {
			throw new NullPointerException();
		}
		if(size>=items.length) {
			return false;//添加失败 offer返回 false  addthrow IllegalStateException
		}
		
		QueueNode qnode = new QueueNode(t);
		items[size++] = qnode;
		
		return true;
	}*/
	public boolean offer(T t) {
		if(size>=items.length) {
			return false;//添加失败 offer返回 false  addthrow IllegalStateException
		}
		
		QueueNode qnode = new QueueNode(t);
		items[size++] = qnode;
		
		return true;
	}
	
	public boolean offerSeperator() {
		if(size>=items.length) {
			return false;//添加失败 offer返回 false  addthrow IllegalStateException
		}
		
		items[size++] = NULL;
		
		return true;
	}
	
	public QueueNode poll() {
		if(size<=0) {
			return null;
		}
		
		QueueNode head = items[0];
		
		for(int i=0;i<size-1;i++) {
			items[i] = items[i+1];
		}
		
		size--;
		return head;
	}
	
	public void print() {
		if(size==0) {
			System.out.println("queue is empty");
			return;
		}
		System.out.println("head->tail:");
		System.out.print("[ ");
		for(int i=0;i<size;i++) {
			if(i==0) {
				System.out.print(items[i].element);
			} else {
				System.out.print("," + items[i].element);
			}
		}
		System.out.print(" ]");
	}
	
	public static void main(String[] args) {
		MyQueue<Integer> myQueue = new MyQueue(SIZE_129);
		for(int i=0;i<100;i++) {
			myQueue.offer(new Random().nextInt(100));
		}
		myQueue.print();
		for(int i=0;i<10;i++) {
			myQueue.poll();
		}
		myQueue.print();
	}
	
}
