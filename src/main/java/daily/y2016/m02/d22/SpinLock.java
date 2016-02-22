package daily.y2016.m02.d22;

import java.util.concurrent.atomic.AtomicReference;

public class SpinLock {
	
	private AtomicReference<Thread> owner = new AtomicReference<Thread>();
	
	public void lock() {
		Thread currentThread = Thread.currentThread();
		while(owner.compareAndSet(null, currentThread)) {
			
		}
	}
	
	public void unlock() {
		Thread currentThread = Thread.currentThread();
		
		owner.compareAndSet(currentThread, null);
	}
}
