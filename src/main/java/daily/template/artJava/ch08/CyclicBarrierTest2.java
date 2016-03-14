package daily.template.artJava.ch08;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierTest2 {
	static CyclicBarrier c = new CyclicBarrier(2, new A());
	public static void main(String args[]) {
		
		Thread t = new Thread(new Runnable() {
			
			public void run() {
				try {
					Thread.sleep(1500);
					c.await();
				} catch(InterruptedException | BrokenBarrierException e) {
					e.printStackTrace();
				}
				System.out.println(System.currentTimeMillis() + ":" + 1);
			}
		});
		t.start();
		
		try {
			
			c.await();
		} catch(InterruptedException | BrokenBarrierException e) {
			e.printStackTrace();
		} 
		System.out.println(System.currentTimeMillis() + ":" + 2);
	}
	
	static class A implements Runnable {
		public void run() {
			System.out.println(3);
		}
	}
}
