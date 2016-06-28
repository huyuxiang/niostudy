package daily.y2016.m06.d28.a1.rpc.example;

import daily.y2016.m06.d28.a1.rpc.server.Server;
import daily.y2016.m06.d28.a1.rpc.server.ServerFactory;

public class ServerBootstrapExample {

	public static void main(String args[]) {
		Server server = ServerFactory.getServer();
		server.register("daily.y2016.m06.d28.a1.rpc.example.TestRpc", 
				new TestRpcImpl());
	}
}
