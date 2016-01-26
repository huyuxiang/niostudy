package concurrentcy.cookbook.c02.l0206;

import com.decartes.common.print.Print;

public class Reader implements Runnable {
	
	private PricesInfo pricesInfo;
	
	public Reader(PricesInfo pricesInfo) {
		this.pricesInfo = pricesInfo;
	}
	
	@Override
	public void run() {
		for(int i=0;i<10;i++) {
			Print.printf(Thread.currentThread().getName() + " Price1 :" + pricesInfo.getPrice1());
			Print.printf(Thread.currentThread().getName() + " Price2 :" + pricesInfo.getPrice2());
		}
	}
}
