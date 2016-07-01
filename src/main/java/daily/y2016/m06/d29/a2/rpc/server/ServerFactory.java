package daily.y2016.m06.d29.a2.rpc.server;

import daily.y2016.m06.d29.a2.rpc.api.RpcFactory;

public class ServerFactory {

	private static Server server;
	
	static {
		server = new Server(RpcFactory.DEFAULT_PORT, 10);
		server.start();
	}
	
	public static Server getServer() {
		return server;
	}
}
