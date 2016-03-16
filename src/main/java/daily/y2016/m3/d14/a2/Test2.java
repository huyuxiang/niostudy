package daily.y2016.m3.d14.a2;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Test2 {
	
	static AtomicInteger index = new AtomicInteger(1);
	static Object obj = new Object();
	static ReentrantLock lock = new ReentrantLock();
	static Condition condition = lock.newCondition();
	
	public static void main(String args[]) throws InterruptedException {
		
		Runnable r = new TRunnable();
		Thread t1 = new Thread(r);
		Thread t2 = new Thread(r);
		Thread t3 = new Thread(r);
		t1.start();
		t2.start();
		t3.start();
		Thread.sleep(500);
			//obj.notifyAll();
		lock.lock();
		condition.signal();
		lock.unlock();
		
	}
	
	public static class TRunnable implements Runnable {
		
		public void run() {
			lock.lock();
			try {
				condition.await();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			lock.unlock();
			System.out.println(index.incrementAndGet());
		}
	}
}
