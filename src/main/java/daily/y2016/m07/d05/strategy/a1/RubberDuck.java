package daily.y2016.m07.d05.strategy.a1;

public class RubberDuck extends Duck {

	public RubberDuck() {
		flyBehavior = new FlyNoWay();
		quackBehavior = new Squeak();
	}
	
	public void display() {
		System.out.println("I'm a rubber duckie");
	}
}
