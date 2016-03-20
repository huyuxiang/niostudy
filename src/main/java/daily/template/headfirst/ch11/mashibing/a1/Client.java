package daily.template.headfirst.ch11.mashibing.a1;

public class Client {
	
	public static void main(String args[]) throws Exception {
		Tank t = new Tank();
		TimeHandler h = new TimeHandler();
		h.setO(t);
		Moveable m = (Moveable)Proxy.newProxyInstance(Moveable.class, h);
		m.move();
	}
}
