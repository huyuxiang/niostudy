package daily.y2016.m06.d28.a2.rpc.example;

import daily.y2016.m06.d28.a2.rpc.server.Server;

public class ServerBootstrapExample {

	public static void main(String[] args) {
		Server server = ServerFactory.getServer();
		server.register("TestRpc", new TestRpcImpl());
	}
}
