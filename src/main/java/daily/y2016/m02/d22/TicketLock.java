package daily.y2016.m02.d22;

import java.util.concurrent.atomic.AtomicInteger;

public class TicketLock {
	
	private AtomicInteger serviceNum = new AtomicInteger();
	private AtomicInteger ticketNum = new AtomicInteger();
	
	public int lock() {
		int myTicketNum = ticketNum.getAndIncrement();
		
		while(serviceNum.get()!=myTicketNum) {
			
		}
		return myTicketNum;
	}
	
	public void unlock(int myTicket) {
		int next = myTicket + 1;
		serviceNum.compareAndSet(myTicket, next);
	}
}
