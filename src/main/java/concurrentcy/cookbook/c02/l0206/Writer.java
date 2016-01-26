package concurrentcy.cookbook.c02.l0206;

import com.decartes.common.print.Print;

public class Writer implements Runnable {
	
	private PricesInfo pricesInfo;
	
	public Writer(PricesInfo pricesInfo) {
		this.pricesInfo = pricesInfo;
	}
	
	@Override
	public void run() {
		for(int i=0;i<3;i++){
			Print.printf("Writer: Attempt to modify the prices");
			pricesInfo.setPrices(Math.random()*10, Math.random()*8);
			Print.printf("Writer: Prices have been modified");
			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
