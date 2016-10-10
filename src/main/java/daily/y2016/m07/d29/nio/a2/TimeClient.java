package daily.y2016.m07.d29.nio.a2;

public class TimeClient {

	public static void main(String[] args) {
		new Thread(new TimeClientHandler("127.0.0.1", 8000), "TimeClient-001").start();
	}
}
