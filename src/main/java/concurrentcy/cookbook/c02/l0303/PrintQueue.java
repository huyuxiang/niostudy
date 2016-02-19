package concurrentcy.cookbook.c02.l0303;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PrintQueue {
	
	private boolean freePrinters[];
	
	private Lock lockPrinters;
	
	private final Semaphore semaphore;
	
	public PrintQueue() {
		semaphore = new Semaphore(3);
		freePrinters = new boolean[3];
		for(int i=0;i<3;i++) {
			freePrinters[i] = true;
		}
		lockPrinters = new ReentrantLock();
	}
	public void printJob(Object document) {
		try {
			semaphore.acquire();
			
			int assignedPrinter = getPrinter();
			
			long duration = (long)(Math.random() *10);
			System.out.printf("%s: PrintQueue :Printing a Job during %d"
					+ "secondes\n", Thread.currentThread().getName(), duration);
			Thread.sleep(duration);
			
			freePrinters[assignedPrinter] = true;
		} catch(InterruptedException e) {
			e.printStackTrace();
		} finally {
			semaphore.release();
		}
	}
	
	private int getPrinter() {
		int ret = -1;
		try {
			lockPrinters.lock();
			for(int i=0;i<freePrinters.length;i++) {
				if(freePrinters[i]) {
					ret = i;
					freePrinters[i] = false;
					break;
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			lockPrinters.unlock();
		}
		return ret;
	}
	
	public static void main(String args[]) {
		PrintQueue printQueue = new PrintQueue();
		Thread thread[] = new Thread[10];
		for(int i=0;i<10;i++) {
			thread[i] = new Thread(new Job(printQueue), "Thread" + i);
		}
		
		for(int i=0;i<10;i++) {
			thread[i].start();
		}
	}
	
}

class Job implements Runnable {
	
	private PrintQueue printQueue;
	
	public Job(PrintQueue printQueue) {
		this.printQueue = printQueue;
	}
	
	public void run() {
		System.out.printf("%s:Going to print a job \n", Thread.currentThread().getName());
		printQueue.printJob(new Object());
		System.out.printf("%s:The document has been printed\n", Thread.currentThread().getName());
	}
	
}