package daily.y2016.m06.d17.mayou.client;

public class ClientStartException extends RuntimeException {

	public ClientStartException() {
		super("客户端已经启动");
	}
}
