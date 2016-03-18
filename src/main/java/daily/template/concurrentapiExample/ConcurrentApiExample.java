package daily.template.concurrentapiExample;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentApiExample {
	
	public static void main(String args[]) {
		testExecutors();
		testFuture();
		testSynchronized();
		
		
		
		testForkJoin_findMax();
		testConcurrentMap();
	}
	
	
	public static void testExecutors() {
		// FixedThreadPool Construction 
		final Executor executor = Executors.newFixedThreadPool(4);
		
		//Use the Executor to execute a Runnable
		executor.execute(new Runnable() {
			@Override
			public void run() {
				//Do work
				System.out.println("run");
			}
		});
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//Prestarting Core Threads
		int count = ((ThreadPoolExecutor)executor).prestartAllCoreThreads();
		System.out.println(count);
	}
	
	public static void testFuture() {
		//Future objects are returned on submit 
		//to ExecutorService or can be created
		//by constructing a FutureTask.
		
		//The Future.get() method blocks
		//until some result is available.
		
		Callable someCallable = new Callable() {
			@Override
			public Object call() {
				return 1;
			}
		};
		
		final Future future = Executors.newCachedThreadPool().submit(someCallable);
		
		// OR
		
		FutureTask<Callable> futureTask = new FutureTask(someCallable);
		Thread thread = new Thread(futureTask);
		thread.start();
		
		//Finally, the Future task completes
		//and the block passes through.
		try {
			Object result = future.get();
		} catch(InterruptedException | ExecutionException e) {
			
		}
	}
	
	public static void testSynchronized() {
		Object object = new Object();
		synchronized(object) {
			//do some work
			
			//releasing lock
		}
		
		synchronized(object) {
			try {
				object.wait();
			} catch(InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
		
		synchronized (object) {
			object.notify();
		}
		
		synchronized(object) {
			object.notifyAll();
		}
		
		Thread thread = new Thread();
		thread.start();
		thread.interrupt();
	}
	
	public static void testReentrantLock() {
		//Constructor
		final Lock lock = new ReentrantLock();
		lock.lock();
		
		try {
			lock.lockInterruptibly();
		} catch(InterruptedException e) {
			
		}
		lock.unlock();
		
		boolean acquired = false;
		try {
			acquired = lock.tryLock(1L, TimeUnit.SECONDS);
			if(acquired) {
				//doSomething();
			}
		} catch(InterruptedException e) {
			
		} finally {
			if(acquired) {
				lock.unlock();
			}
		}
		
		//<lockedThread>.interrupt();
		//<blockedThread>.interrupt();
	}
	
	
	public static void testCondition() {
		Lock lock = new ReentrantLock();
		Condition condition1 = lock.newCondition();
		Condition condition2 = lock.newCondition();
		Condition condition3 = lock.newCondition();
		
		lock.lock();
		try {
			condition1.await();
		} catch(InterruptedException e) {
			
		} finally {
			lock.unlock();
		}
		
		lock.lock();
		condition1.signal();
		lock.unlock();
		
		lock.lock();
		condition1.signalAll();
		lock.unlock();
	}
	
	
	public static void testConcurrentMap() {
		//Construct empty concurrent map
		ConcurrentMap<Integer, String> map = new ConcurrentHashMap<Integer, String>();
		
		//putIfAbsent only puts value
		//if key isn't contained.
		//Returns the previous value, or null
		//if key wasn't previously contained.
		int key = 1;
		String value = "v(1)";
		String previousValue = map.putIfAbsent(key, value);
		boolean wasAbsent = previousValue == null;
		System.out.println("previousValue:" + previousValue + ";wasAbsent:" + wasAbsent);
		//If key is contained, replaces value
		//with supplied value.
		//Returns the previous value,
		//or null if key was absent.
		String previousValue1 = map.replace(key, value);
		boolean wasAbsent1 = previousValue1 == null;
		System.out.println("previousValue1:" + previousValue1 + ";wasAbsent1:" + wasAbsent1);
	}
	
	/** Challenge: Calculate the maximum 
	 * element of array[14] using Fork/Join
	 */
	public static void testForkJoin_findMax() {
		int[] array = new int[14];
		
		for(int i=0;i<array.length;i++) {
			array[i] = new Random().nextInt();
		}
		int nthreads = 4;
		Solver solver = new Solver(array, 0, array.length);
		ForkJoinPool pool = new ForkJoinPool(nthreads);
		pool.invoke(solver);
		int result = solver.result;
		
		System.out.println(result);
	}
	
	static class Solver extends RecursiveAction {
		private int start, end, result;
		private int array[];
		
		private Solver(int [] array, int start, int end) {
			this.array = array;
			this.start = start;
			this.end = end;
		}
		@Override
		protected void compute() {
			if(end -start==1) {
				result = array[start];
			} else {
				int mid = (start + end)/2;
				Solver solver1 = new Solver(array, start, mid);
				Solver solver2 = new Solver(array, mid, end);
				invokeAll(solver1, solver2);
				result = Math.max(solver1.result, solver2.result);
			}
		}
	}
	
}
