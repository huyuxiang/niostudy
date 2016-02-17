package daily.y2016.m02.d17.lock;

public class Lock {

	private boolean isLocked = false;
	private Thread lockingThread = null;
	
	public synchronized void lock() throws InterruptedException {
		while(isLocked) {
			wait();
		}
		isLocked = true;
		lockingThread = Thread.currentThread();
	}
	
	public synchronized void unlock() {
		if(this.lockingThread != Thread.currentThread()) {
			throw new IllegalMonitorStateException("Calling Thread has not locked this lock");
		}
		
		isLocked = false;
		lockingThread = null;
		notify();
	}
}
