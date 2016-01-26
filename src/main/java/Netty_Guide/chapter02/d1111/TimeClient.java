package Netty_Guide.chapter02.d1111;

public class TimeClient {
	public static void main(String args[]) {
		new Thread(new TimeClientHandler("127.0.0.1", 8000), "TimeClient-001").start();
	}
}
