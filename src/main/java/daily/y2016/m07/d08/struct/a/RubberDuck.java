package daily.y2016.m07.d08.struct.a;

public class RubberDuck extends Duck{

	public RubberDuck() {
		flyBehavior = new FlyNoWay();
		quackBehavior = new Squeak();
	}
	
	public void display() {
		System.out.println("I'm a rubber duckie");
	}
}
