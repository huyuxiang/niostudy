package daily.y2016.m07.d28.nio.a2;

public class NioServer {

	public static void main(String[] args) {
		int port = 8080;
		
		MultiplexerTimeServer timeServer = new MultiplexerTimeServer(port);
		new Thread(timeServer, "NIO-MultiplexerTimeServer-001").start();
	}
}
