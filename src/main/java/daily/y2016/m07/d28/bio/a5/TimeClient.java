package daily.y2016.m07.d28.bio.a5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TimeClient {

	public static void main(String[] args) {
		int port = 8080;
		Socket socket = null;
		BufferedReader in = null;
		PrintWriter out = null;
		try {
			socket = new Socket("127.0.0.1", port);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			out.println("this is client");
			String resp = in.readLine();
			System.out.println("Now is :" + resp);
		} catch(Exception e) {
			
		} finally {
			if(out!=null) {
				out.close();
				out = null;
			}
			if(in!=null) {
				try {
					in.close();
				} catch(IOException e) {
					
				}
				in = null;
			}
			if(socket!=null) {
				try {
					socket.close();
				} catch(IOException e) {
					
				}
				socket = null;
			}
		}
	}
}
