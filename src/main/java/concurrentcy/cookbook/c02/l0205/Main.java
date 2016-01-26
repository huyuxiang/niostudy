package concurrentcy.cookbook.c02.l0205;

import com.decartes.common.print.Print;

public class Main {
	
	static PrintQueue printQueue = new PrintQueue();
	
	public static void main(String args[]) {
		Thread thread[] = new Thread[10];
		for(int i=0;i<10;i++) {
			thread[i] = new Thread(new Job(printQueue), "Thread " + i);
		}
		
		for(int i=0;i<10;i++) {
			thread[i].start();
		}
	}
	
	public static void stop() {
		printQueue.stop();
	}
}

