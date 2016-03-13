package daily.y2016.m3.d13.a2;

import java.util.Random;

public class Tank implements Movable {

	public void move() {
		long start = System.currentTimeMillis();
		System.out.println("Tank Moving...");
		try {
			Thread.sleep(new Random().nextInt(10000));
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		System.out.println("time:" + (end - start) );
	}
}
