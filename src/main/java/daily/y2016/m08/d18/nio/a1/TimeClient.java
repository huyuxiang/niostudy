package daily.y2016.m08.d18.nio.a1;

public class TimeClient {

	public static void main(String[] args) {
		new Thread(new TimeClientHandler("127.0.0.1", 8000), "timeClient-001").start();
	}
}
