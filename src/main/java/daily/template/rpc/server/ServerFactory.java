package daily.template.rpc.server;

import daily.template.rpc.api.RpcFactory;

public class ServerFactory {
	
	private static Server server;
	
	static{
		server = new Server(RpcFactory.DEFAULT_PORT, 10);
		server.start();
	}
	
	public static Server getServer(){
		return server;
	}

}
