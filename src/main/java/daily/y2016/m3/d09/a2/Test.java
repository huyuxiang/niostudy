package daily.y2016.m3.d09.a2;

import java.util.concurrent.locks.ReentrantLock;

public class Test {
	
	private int i = 0;
	private volatile int j = 0;
	
	public void test1() {
		ReentrantLock lock = new ReentrantLock();
		try {
		lock.lock();
		i = i + 1;
		} finally {
			if(lock.isHeldByCurrentThread()) {
				lock.unlock();
			}
		}
	}
	
	public synchronized void test2() {
		i = i + 1;
	}
	
	public void test3() {
		synchronized(this) {
			i = i + 1;
		}
	}
	
	public void test4() {
		j = j + 1;
	}
	
}
