package douglea.test5;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TimeClient {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		int port = 8001;
		if(args!=null && args.length>0) {
			try {
				port = Integer.valueOf(args[0]);
			} catch(NumberFormatException e) {
				
			}
		}
		
		new Thread (new TimeClientHandler("127.0.0.1", port), "TimeClient-001").start();
	}

}
