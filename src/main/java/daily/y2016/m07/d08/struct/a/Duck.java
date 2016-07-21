package daily.y2016.m07.d08.struct.a;

public abstract class Duck {

	FlyBehavior flyBehavior;
	QuackBehavior quackBehavior;
	
	public Duck() {
		
	}

	public FlyBehavior getFlyBehavior() {
		return flyBehavior;
	}

	public void setFlyBehavior(FlyBehavior flyBehavior) {
		this.flyBehavior = flyBehavior;
	}

	public QuackBehavior getQuackBehaivor() {
		return quackBehavior;
	}

	public void setQuackBehaivor(QuackBehavior quackBehaivor) {
		this.quackBehavior = quackBehaivor;
	}

	abstract void display();
	
	public void swim() {
		System.out.println("all ducks float, even decoys!");
	}
}
