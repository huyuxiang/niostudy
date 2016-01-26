package concurrentcy.cookbook.c01.l0102;

import com.decartes.common.print.*;

public class Calculator implements Runnable {

	private int number;
	
	public Calculator(int number) {
		this.number = number;
	}
	
	@Override
	public void run() {
		for(int i=1;i<=10;i++) {
			Print.printf(Thread.currentThread().getName() + ":" + number + "*" + i + "=" +  i * number);
		}
	}

}
