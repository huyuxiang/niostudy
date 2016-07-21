package daily.y2016.m07.d15.a6;

public class Reactor implements Runnable {

	public static void main(String[] args) {
		Reactor reactor = new Reactor(8000);
		new Thread(reactor).start();
	}
	
	final Selector selector;
	final ServerSocketChannel serverSocketChannel;
	
	Reactor(int port)throws IOException {
		selector = Selector.open();
		serverSocketChannel = ServerSocketChannel.open();
		
	}
}
