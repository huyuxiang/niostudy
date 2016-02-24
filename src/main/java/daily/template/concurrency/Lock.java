package daily.template.concurrency;

public class Lock {
	private boolean 		isLocked 		= false;
	private Thread 			lockingThread 	= null;
	
	public synchronized void lock() throws InterruptedException {
		while(isLocked) {
			wait();
		}
		isLocked = true;
		lockingThread = Thread.currentThread();
	}
	
	public synchronized void unlock() {
		if(this.lockingThread != Thread.currentThread()) {
			throw new IllegalMonitorStateException(
					"Calling thread has not locked this lock");
		}
		isLocked = false;
		lockingThread = null;
		notify();
	}
	
	private int shareVar = 0;
	private static Lock lock = new Lock();
	
	public static void main(String args[]) {
		Thread thread1 = new Thread(lock.new AddOne());
		Thread thread2 = new Thread(lock.new AddOne());
		thread1.start();
		thread2.start();
	}
	
	private static int DEFAULT_WAIT_TIME = 10000;
	
	private class AddOne implements Runnable {
		
		@Override
		public void run() {
			try {
				lock.lock();
				Thread.sleep(DEFAULT_WAIT_TIME);
				shareVar ++;
				lock.unlock();
			} catch (InterruptedException e) {
				if(lock.lockingThread == Thread.currentThread()) {
					lock.unlock();
				}
			}
		}
		
	}
}
