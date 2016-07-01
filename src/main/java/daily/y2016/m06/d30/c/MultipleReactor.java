package daily.y2016.m06.d30.c;

public class MultipleReactor implements Runnable {

	public static void main(String[] args) {
		MultipleReactor server = new MultipleReactor(8000);
		new Thread(server).start();
	}
	
	final Selector selector;
	final
}
