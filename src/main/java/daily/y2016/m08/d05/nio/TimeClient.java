package daily.y2016.m08.d05.nio;

public class TimeClient {

	public static void main(String[] args) {
		new Thread(new TimeClientHandler("127.0.0.1", 8080), "timeClient-001").start();
	}
}
