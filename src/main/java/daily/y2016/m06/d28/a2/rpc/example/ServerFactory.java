package daily.y2016.m06.d28.a2.rpc.example;

import daily.y2016.m06.d28.a2.rpc.api.RpcFactory;
import daily.y2016.m06.d28.a2.rpc.server.Server;

public class ServerFactory {

	private static Server server;
	
	static {
		server = new Server (RpcFactory.DEFAULT_PORT, 10);
		server.start();
	}
	
	public static Server getServer() {
		return server;
	}
}
