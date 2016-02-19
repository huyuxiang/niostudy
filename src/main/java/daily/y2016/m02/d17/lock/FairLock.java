package daily.y2016.m02.d17.lock;

public class FairLock {
	private boolean 		isLocked 		= false;
	private Thread 			lockingThread	= null;
	private List<QueueObject> waitingThreads = new ArrayList<QueueObject>();
	
	public void lock() throws InterruptedException {
		QueueObject queueObject = new QueueObject();
		boolean isLockedForThisThread = true;
		synchronized(this) {
			waitingThreads.add(queueObject);
		}
		
		while(isLockedForThisThread) {
			synchronized(this) {
				is
			}
		}
	}
}
