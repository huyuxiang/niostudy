package daily.template.spinLock;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

//http://mauersu.iteye.com/blog/2277505
public class CLHLock {
	
	public static class CLHNode {
		private boolean isLocked = true;
	}
	
	private volatile CLHNode tail;
	
	private static final AtomicReferenceFieldUpdater<CLHLock, CLHNode> UPDATER = 
			AtomicReferenceFieldUpdater
            . newUpdater(CLHLock.class, CLHNode .class , "tail" );
	
	public void lock(CLHNode currentThread) {
		CLHNode preNode = UPDATER.getAndSet(this, currentThread);
		if(preNode != null) {
			while(preNode.isLocked) {
				
			}
		} 
	}
	
	public void unlock(CLHNode currentThread) {
		if(!UPDATER.compareAndSet(this, currentThread, null)) {
			currentThread.isLocked = false;
		}
	}
}
