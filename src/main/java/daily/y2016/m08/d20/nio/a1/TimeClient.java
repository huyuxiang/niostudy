package daily.y2016.m08.d20.nio.a1;

public class TimeClient {

	public static void main(String[] args) {
		new Thread(new TimeClientHandler("127.0.0.1", 8000), "timeClient").start();
	}
}
