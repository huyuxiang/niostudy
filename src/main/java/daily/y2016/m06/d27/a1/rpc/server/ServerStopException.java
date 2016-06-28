package daily.y2016.m06.d27.a1.rpc.server;


public class ServerStopException extends RuntimeException {

	public ServerStopException() {
		super("server is stop");
	}
}
