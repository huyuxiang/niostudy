package daily.y2016.m3.d13.a2;

//26.32 动态代理1
public class Tank3 implements Movable {
	
	public Tank3(Tank t) {
		super();
		this.t = t;
	}

	Tank t ;
	
	public void move() {
		long start = System.currentTimeMillis();
		t.move();
		long end = System.currentTimeMillis();
		System.out.println("time:" + (end-start));
	}
}
