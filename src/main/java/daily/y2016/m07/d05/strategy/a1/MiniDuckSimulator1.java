package daily.y2016.m07.d05.strategy.a1;

public class MiniDuckSimulator1 {

	public static void main(String[] args) {
		
		Duck mallard = new MallardDuck();
		mallard.performQuack();
		mallard.performFly();
		
		Duck model = new ModelDuck();
		model.performFly();
		model.setFlyBehavior(new FlyRocketPowered());
		model.performFly();
	}
}
