package com.decartes.common.print;

import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;

public class Stack {
	LinkedList list = new LinkedList();
	
	public synchronized void push (Object x) {
		System.out.println("11");
		synchronized (list) {
			list.addLast(x);
			notify();
		}
		System.out.println("aa");
	}
	
	public synchronized Object pop(CountDownLatch countDownLatch) throws Exception {
		
		synchronized(list) {
			if(list.size()<=0){
				countDownLatch.countDown();
				wait();
			}
			return list.removeLast();
		}
	}
	
	
	public static void main(String[] args) throws InterruptedException {
		final Stack stack = new Stack();
		final CountDownLatch countDownLatch = new CountDownLatch(1);
		Thread thread1 = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					countDownLatch.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				stack.push(1);
			}
		});
		Thread thread2 = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					stack.pop(countDownLatch);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		thread1.start();
		thread2.start();
		thread1.join();
		thread2.join();
	}
}
