package daily.y2016.m06.d27.a1.rpc.server;

import daily.y2016.m06.d27.a1.rpc.client.RpcFactory;

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
