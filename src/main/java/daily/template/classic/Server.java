package daily.template.classic;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {
	
	private final int PORT;
	public Server(int port) {
		this.PORT = port;
	}
	public void run() {
		try {
			ServerSocket ss = new ServerSocket(PORT);
			while(!Thread.interrupted()) {
				new Thread(new Handler(ss.accept())).start();
				//or , single-threaded, or a thread pool
			}
		} catch(IOException ex) {
			
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
			} catch(IOException ex) {
				
			}
		}
		
		private byte[] process(byte[] cmd) {
			
			return null;
		}
	}
}
//Note: most exception handling elided from code examples


