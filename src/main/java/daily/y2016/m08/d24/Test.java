package daily.y2016.m08.d24;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;

public class Test {

	public static void main(String[] args) throws InterruptedException {
		final SynchronousQueue<String> synqueue = new SynchronousQueue<String>();
		//synqueue.put("aa");
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					synqueue.put("aa");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		
		new Thread(runnable).start();
		new Thread(runnable).start();
		System.out.println("aa");
		
		ThreadPoolExecutor exe1 = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
		ThreadPoolExecutor exe2 = (ThreadPoolExecutor) Executors.newSingleThreadExecutor();
		exe1.execute(runnable);
		exe1.execute(runnable);
		exe1.setCorePoolSize(2);
		exe1.setMaximumPoolSize(2);
		exe1.execute(runnable);
		//synqueue.take();
		//synqueue.take();
		exe2.execute(runnable);
		exe2.execute(runnable);
		exe2.setCorePoolSize(2);
		exe2.setMaximumPoolSize(2);
		exe2.execute(runnable);
		
	}
}
