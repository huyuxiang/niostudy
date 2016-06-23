package daily.y2016.m06.d17.mayou.client;

public class ClientStopException extends RuntimeException {

	public ClientStopException() {
		super("客户端已关闭");
	}
}
