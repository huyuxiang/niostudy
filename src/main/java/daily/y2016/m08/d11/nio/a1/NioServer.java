package daily.y2016.m08.d11.nio.a1;

public class NioServer {

	public static void main(String[] args) {
		int port = 8080;
		
		MultiplexerTimeServer timeServer = MultiplexerTimeServer(port);
		new Thread(timeServer, "NIO-MultiplexerTimeServer-001").start();
	}
}
