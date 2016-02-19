package daily.template.spinLock;

import java.util.concurrent.atomic.AtomicInteger;
//http://mauersu.iteye.com/blog/2277505
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
