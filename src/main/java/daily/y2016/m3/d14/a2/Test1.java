package daily.y2016.m3.d14.a2;

import java.util.concurrent.atomic.AtomicInteger;

public class Test1 {
	
	static AtomicInteger index = new AtomicInteger(1);
	static Object obj = new Object();
	
	public static void main(String args[]) throws InterruptedException {
		
		Runnable r = new TRunnable();
		Thread t1 = new Thread(r);
		Thread t2 = new Thread(r);
		Thread t3 = new Thread(r);
		t1.start();
		t2.start();
		t3.start();
		Thread.sleep(500);
		synchronized(obj) {
			obj.notifyAll();
		}
		
		
	}
	
	public static class TRunnable implements Runnable {
		
		public void run() {
			synchronized(obj) {
				try {
					obj.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			System.out.println(index.incrementAndGet());
		}
	}
}
