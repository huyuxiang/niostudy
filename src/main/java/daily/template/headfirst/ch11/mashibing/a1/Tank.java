package daily.template.headfirst.ch11.mashibing.a1;

import java.util.Random;

public class Tank implements Moveable {
	
	@Override
	public void move() {
		System.out.println("Tank Moving...");
		try {
			Thread.sleep(new Random().nextInt(10000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
