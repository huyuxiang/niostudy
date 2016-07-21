package daily.y2016.m07.d08.struct.a;

public class RedHeadDuck extends Duck {

	public RedHeadDuck() {
		flyBehavior = new FlyWithWings();
		quackBehavior = new Quack();
	}
	
	public void display() { 
		System.out.println("I'm a read red headed duck");
	}
}
