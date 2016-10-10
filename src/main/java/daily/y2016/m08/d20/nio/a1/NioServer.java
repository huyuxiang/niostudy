package daily.y2016.m08.d20.nio.a1;

public class NioServer {

	public static void main(String[] args) {
		int port = 8080;
		
		MultiplexerTimeServer timeServer = new MultiplexerTimeServer(port);
		new Thread(timeServer, "NIO-").start();
	}
}
