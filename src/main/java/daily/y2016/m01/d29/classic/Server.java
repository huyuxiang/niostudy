package daily.y2016.m01.d29.classic;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable{
	
	private final int PORT;
	
	public Server(int port) {
		this.PORT = port;
	}
	
	public void run() {
		try {
			ServerSocket ss = new ServerSocket(PORT);
			while(!Thread.interrupted()) {
				new Thread(new Handler(ss.accept())).start();
				//or , single-threaded , or thread pool
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public final static int MAX_INPUT = 1024;
	
	static class Handler implements Runnable {
		
		final Socket socket;
		
		Handler(Socket socket) {
			this.socket = socket;
		}
		
		public void run() {
			try {
				byte[] input = new byte[MAX_INPUT];
				socket.getInputStream().read(input);
				byte[] output = process(input);
				socket.getOutputStream().write(output);
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		
		private byte[] process(byte[] cmd) {
			return null;
		}
	}
}
//Note: most exception handling elided from code examples