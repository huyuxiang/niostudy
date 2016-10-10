package daily.y2016.m08.d03.bio;

import java.net.ServerSocket;
import java.net.Socket;

public class TimeServer {

	public static void main(String[] args) {
		int port = 8080;
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(port);
			Socket socket = null;
			while(true) {
				socket = serverSocket.accept();
				new Thread(new TimeServerHandler(socket)).start();
			}
		} finally {
			if(serverSocket!=null) {
				serverSocket.close();
				serverSocket = null;
			}
		}
	}
}
