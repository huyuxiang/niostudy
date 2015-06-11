package douglea.test1;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {
	static final int PORT = 8080;
	public void run() {
		try{
			ServerSocket ss = new ServerSocket(PORT);
			while(!Thread.interrupted())
				new Thread(new Handler(ss.accept())).start();
		} catch(IOException e) {
			
		}
	}
	
	static final int MAX_INPUT = 1024;
	static class Handler implements Runnable {
		final Socket socket;
		Handler(Socket s) { socket = s;}
		public void run() {
			try {
				byte[] input = new byte[MAX_INPUT];
				socket.getInputStream().read(input);
				byte[] output = process(input);
				socket.getOutputStream().write(output);
			} catch(IOException e) {
				
			}
		}
		
		private byte[] process(byte[] cmd) {
			return cmd;
		}
	}
}
