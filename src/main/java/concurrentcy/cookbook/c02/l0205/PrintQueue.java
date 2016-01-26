package concurrentcy.cookbook.c02.l0205;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.decartes.common.print.Print;

public class PrintQueue {
	
	private final Lock queueLock = new ReentrantLock(true);
	
	private boolean  isStop = false;
	
	private int time = 2;
	
	public void printJob(Object document){
		queueLock.lock();
		if(time == 0) {
			Thread.currentThread().interrupt();
		}
		while(!isStop&&--time>0) {
			try{
				//Long duration = (long)(Math.random()*10000);
				Long duration = (long)(Math.random()*10000);
				Print.printf(Thread.currentThread().getName() + "PrintQueue:printing a job during " + (duration/1000) + " seconds");
				Thread.sleep(duration);
			} catch(InterruptedException e ) {
				e.printStackTrace();
			}
		}
		queueLock.unlock();
	}
	
	public void stop() {
		this.isStop = true;
	}
}
