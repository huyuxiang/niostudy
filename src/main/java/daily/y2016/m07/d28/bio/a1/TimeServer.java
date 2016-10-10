package daily.y2016.m07.d28.bio.a1;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TimeServer {
	public static void main(String[] args) throws IOException {
		int port = 8080;
		ServerSocket server = null;
		try {
			server = new ServerSocket(port);
			Socket socket = null;
			while(true) {
				socket = server.accept();
				new Thread(new TimeServerHandler(socket)).start();
			}
		} finally {
			if(server!=null) {
				System.out.println("the time server close.");
				server.close();
				server = null;
			}
		}
	}
}
