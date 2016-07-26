package daily.y2016.m07.d26.c3;

public class MultipleReactor implements Runnable {

	public static void main(String[] args) {
		MultipleReactor server = new MultipleReactor(8000);
		
	}
	
	final Selector selector;
	final ServerSocketChannel serverSocketChannel;
	
	final int DEFAULT_WORKER_SIZE = 10;
	Selector[] selectors;
	int next = 0;
	ExecutorService workerpool = 
}
