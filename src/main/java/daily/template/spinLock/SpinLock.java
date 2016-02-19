package daily.template.spinLock;

import java.util.concurrent.atomic.AtomicReference;

//http://coderbee.net/index.php/concurrent/20131115/577/comment-page-1
// http://mauersu.iteye.com/blog/2277505
public class SpinLock {
	
	private AtomicReference<Thread> owner = new AtomicReference<Thread>();
	
	public void lock() {
		Thread currentThread = Thread.currentThread();
		while(owner.compareAndSet(null, currentThread)){
			
		}
	}
	
	public void unlock() {
		Thread currentThread = Thread.currentThread();
		
		owner.compareAndSet(currentThread, null);
	}
}
