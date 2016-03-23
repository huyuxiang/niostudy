package daily.template.concurrentapiExample;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.Phaser;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.TransferQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ConcurrentApiExample {
	
	public static void main(String args[]) {
		testExecutors();
		testFuture();
		testSynchronized();
		testReentrantLock();
		testCondition();
		testSemaphore();
		testReadWriteLock();
		testCountDownLatch();
		testCyclicBarrier();
		testPhaser();
		testBlinkingPhaser();
		testAtomicInteger();
		testBlockingQueue();
		testTransferQueue();
		testCompletionService();
		testConcurrentMap();
		testForkJoin_findMax();
		
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
	
	
	public static void testSemaphore() {
		//Constructor - pass in the number of permits
		final Semaphore semaphore = new Semaphore(4, true);
		//final Semaphore semaphore = new Semaphore(4);
		//Threads attempting to acquire will block until
		//the specified number of releases are counted
		
		try {
			semaphore.acquire();
		} catch(InterruptedException e) {
			
		}
		semaphore.release();
		
		//tryAcquire is like acquire, except that it
		//times out after an(optional) specified time.
		try {
			if(semaphore.tryAcquire(5, TimeUnit.SECONDS)) {
				//Do something
			}
		} catch(InterruptedException e) {
			
		}
		
		//If no time is specified, times out immediately
		// if not acquired
		if(semaphore.tryAcquire()) {
			//Do something
		}
	}
	
	private static void testReadWriteLock() {
		//Construct the ReadWriteLock
		final ReadWriteLock lock = new ReentrantReadWriteLock();
		// new ReentrantReadWriteLock(true)
		
		//Acquire the read lock
		try {
			lock.readLock().lock();
			// or 
			lock.readLock().tryLock(1L, TimeUnit.SECONDS);
		} catch(InterruptedException e) {}
		
		//Release the read lock
		lock.readLock().unlock();
		
		//Acquire the write lock
		try {
			lock.writeLock().lock();
			//or
			lock.writeLock().tryLock(1L, TimeUnit.SECONDS);
		} catch(InterruptedException e) {}
		
		//Release the lock
		lock.writeLock().unlock();
		//or
		lock.readLock().unlock();
		
		lock.readLock().lock();
		lock.writeLock().unlock();
	}
	
	public static void testCountDownLatch() {
		//Constructor - pass in the pass count
		final CountDownLatch countDownLatch = new CountDownLatch(4);
		// Threads attempting to acquire
		//will block until the specified
		//number of release is counted.
		Thread acquireThread = new Thread() {
			public void run() {
				try {
					countDownLatch.await();
				} catch(InterruptedException e) {}
			}
		};
		
		Thread releaseThread = new Thread() {
			public void run() {
				countDownLatch.countDown();
			}
		};
		
		//timed await is like await except that
		//it times out after the specified
		// timeout period
		try {
			countDownLatch.await(1L, TimeUnit.DAYS);
		} catch(InterruptedException e) {}
	}
	
	public static void testCyclicBarrier() {
		//Contructor specifies # of parties, and an
		//optional Runnable that gets called when the 
		//barrier is opened
		final CyclicBarrier cyclicBarrier = new CyclicBarrier(4, new Runnable() {
			public void run() {
				System.out.println("Runnable hit");
			}
		});
		
		//Each call to await blocks, until the number
		//specified in the constructor is reached.
		//Then the Runnable executes and all can pass.
		Thread thread = new Thread() {
			public void run() {
				try {
					cyclicBarrier.await();
				} catch(BrokenBarrierException | InterruptedException e) {}
			}
		};
		
		Thread thread1 = new Thread() {
			public void run() {
				try {
					cyclicBarrier.await(1, TimeUnit.SECONDS);
				} catch(BrokenBarrierException | InterruptedException | TimeoutException e) {}
			}
		};
		
		//reset() allows the barrier to be reused
		//Any waiting threads will throw 
		//a BrokenBarrierException
		cyclicBarrier.reset();
	}
	
	public static void testPhaser() {
		Phaser phaser = new Phaser(4) {
			@Override
			//Perform when all parties arrive
			protected boolean onAdvance(int phase, 
						int registeredParties) {
			//	return true if the Phaser should
			//	terminate no advance, else false
			return false;
			}
		};
		
		int phase = phaser.arriveAndAwaitAdvance();
		/*int phase = phaser.arrive();
		int phase = phaser.awaitAdvance(int phase);
		int phase = phaser.arriveAndDeregister();
		int phase = phaser.register();
		int phase = phaser.bulkRegister(int parties);*/
	}
	
	public static void testBlinkingPhaser() {
		/*private Phaser phaser = new Phaser(21) {
			protected boolean onAdvance(int phase, int registeredParties) {
				return phase >= BLINK_COUNT - 1 || registeredParties == 0;
			}
		};*/
		
		/*public void start() {
			Random rand = new Random();
			for(final JComponent comp: buttonArray ) {
				Thread thread = new Thread() {
					public void run() {
						try {
							do {
								Color defaultColor = comp.getBackgroud();
								Color newColor = new Color(rand.nextInt());
								changeColor(comp, newColor);
								Thread.sleep(500+ rand.nextInt(3000));
								changeColor(comp, defaultColor);
								Toolkit.getDefaultToolkit().beep();
								Thread.sleep(2000);
								phaser.arriveAndAwaitAdvance();
							} while(!phaser.isTerminated());
							
						} catch(InterruptedException e) {
							Thread.currentThread.interrupted();
						}
					}
				};
				thread.start();
			}
		}*/
	}
	
	public static void testAtomicInteger() {
		//Construct the AtomicVariable,
		//assigning an initial value
		
		final AtomicInteger atomicInteger = new AtomicInteger(1);
		
		//compareAndSet does an atomic
		//"check and set if".
		// Value is only set 
		// if original value == assumed value
		int assumedValue = 10, newValue = 5;
		
		boolean success = atomicInteger.compareAndSet(assumedValue, newValue);
		
		//Arithmetic functions on atomics
		//perform their computations in
		// an atomic fashion and return
		//the result.
		int result = atomicInteger.incrementAndGet();
		
	}
	
	public static void testBlockingQueue() {
		//Constructor -- pass in the upper bound
		final BlockingQueue queue = new ArrayBlockingQueue(4);
		
		//Threads attempting to put will block
		//until there is room in the buffer
		Thread putThread = new Thread() {
			public void run() {
				try {
					queue.put(1);
				} catch(InterruptedException e) {
					
				}
			}
		};
		
		//offer is like put except that it times out 
		//after the specified timeout period
		Thread offerThread = new Thread() {
			public void run() {
				try {
					queue.offer(2, 1L, TimeUnit.SECONDS);
				} catch(InterruptedException e) {}
				
			}
		};
		//Threads attempting to poll will return 
		// null if there is nothing on the queue
		Thread poolThread = new Thread() {
			public void run() {
				queue.poll();
			}
			
		};
		//Threads attempting to take will block
		//until the there is something to take
		Thread takeThread = new Thread() {
			public void run() {
				try {
					queue.take();
				} catch (InterruptedException e) {
				}
			}
		};
	}
	
	
	public static  void testTransferQueue() {
		TransferQueue<Integer> transferQueue = new LinkedTransferQueue<Integer>();
		
		try {
			transferQueue.put(1);
		} catch(InterruptedException e) {}
		try {
			Integer t = (Integer) transferQueue.take();
		} catch(InterruptedException e) {}
		
		Integer t = transferQueue.poll();
		
		//Integer t1 = transferQueue.transfer();
		
		boolean success = transferQueue.tryTransfer(t);
		
		try {
			boolean success1 = transferQueue.tryTransfer(t, 5, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void testCompletionService() {
		//Launch many Callables to the
		// completion service.
		//Results are queued as they arrive.
		// To retrieve the results in sequence,
		//call completionService.take().get();
		
		//Create a completionService, providing
		// an Executor in the constructor.
		final CompletionService completionService = new ExecutorCompletionService(Executors.newFixedThreadPool(4));
		
		//submit callables to the completion service
		completionService.submit(new Callable<Integer>() {
			@Override
			public Integer call()  {
				return 0;
			}
		});
		
		//now take results as they complete
		Future future = null;
		try {
			future = completionService.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
			Object result = future.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
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
