package daily.y2016.m07.d05.strategy.a1;

public class ModeDuck extends Duck {

	public ModelDuck() {
		flyBehavior = new FlyNoWay();
		quackBehavior = new Quack();
	}
	
	public void display() {
		System.out.println("I'm a model duck");
	}
}
