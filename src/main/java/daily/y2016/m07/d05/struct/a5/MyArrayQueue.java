package daily.y2016.m07.d05.struct.a5;

public class MyArrayQueue<T> {

	public static final int DEFAULT_SIZE = 10;
	
	public static final QueueNode NULL = new QueueNode();
	
	private QueueNode[] items;
	private int size;
	
	public MyArrayQueue() {
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
	
	public MyArrayQueue(int size) {
		items = new QueueNode[size];
		size= 0;
	}
	public boolean offer(T t) {
		if(size>=items.length)
			return false;
		
		QueueNode qnode  = new QueueNode(t);
		items[size++] = qnode;
		return true;
	}
	
	public boolean offerSeperator() {
		if(size>=items.length)
			return false;
		
		items[size++] = NULL;
		return true;
	}
	
	public QueueNode poll(){
		if(size<=0)
			return null;
		
		QueueNode head = items[0];
		
		for(int i=0;i<size-1;i++)
			items[i] = items[i+1];
		size--;
		return head;
	}
	
}
