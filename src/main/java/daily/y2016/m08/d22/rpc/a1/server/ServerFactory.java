package daily.y2016.m08.d22.rpc.a1.server;

import daily.y2016.m08.d22.rpc.a1.api.RpcFactory;

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
