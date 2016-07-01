package daily.y2016.m06.d29.a2.rpc.example;

import daily.y2016.m06.d29.a2.rpc.server.Server;
import daily.y2016.m06.d29.a2.rpc.server.ServerFactory;

public class ServerBootstrapExample {

	public static void main(String[] args) {
		Server server = ServerFactory.getServer();
		server.register("daily.y2016.m06.d29.a2.rpc.example.TestRpc", 
				new TestRpcImpl());
	}
}
