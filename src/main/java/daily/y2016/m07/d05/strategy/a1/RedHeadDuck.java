package daily.y2016.m07.d05.strategy.a1;

public class RedHeadDuck extends Duck {

	public RedHeadDuck() {
		flyBehavior = new FlyWithWings();
		quackBehavior = new Quack();
	}
	
	public void display() {
		System.out.println("I'm a real Red Headed duck");
	}
}
