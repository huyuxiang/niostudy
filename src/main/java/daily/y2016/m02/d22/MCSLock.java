package daily.y2016.m02.d22;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public class MCSLock {
	
	public static class MCSNode {
		volatile MCSNode next;
		volatile boolean isBlock = true;
	}
	
	volatile MCSNode queue;
	private static final AtomicReferenceFieldUpdater<MCSLock, MCSNode> UPDATER = 
				AtomicReferenceFieldUpdater
				.newUpdater(MCSLock.class, MCSNode.class, "queue");
	
	public void lock(MCSNode currentThread) {
		MCSNode predecessor = UPDATER.getAndSet(this, currentThread);
		if(predecessor!=null) {
			predecessor.next = currentThread;
			
			while(currentThread.isBlock) {
				
			}
		} else {
			currentThread.isBlock = false;
		}
	}
	
	public void unlock(MCSNode currentThread) {
		if(currentThread.isBlock) {
			return ;
		}
		
		if(currentThread.next == null) {
			if(UPDATER.compareAndSet(this, currentThread, null)) {
				return ;
			} else {
				while(currentThread.next == null) {
					
				}
			}
		}
		
		currentThread.next.isBlock = false;
		currentThread.next = null;
	}
}
