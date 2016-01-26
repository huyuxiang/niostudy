package concurrentcy.cookbook.c02.l0205;

import com.decartes.common.print.Print;

public class Job implements Runnable {
	
	private PrintQueue printQueue;
	
	public Job(PrintQueue printQueue) {
		this.printQueue = printQueue;
	}
	
	public void run() {
		Print.printf(Thread.currentThread().getName() + " Going to print a document");
		printQueue.printJob(new Object());
		Print.printf(Thread.currentThread().getName() + " The document has been printed");
	}
}
