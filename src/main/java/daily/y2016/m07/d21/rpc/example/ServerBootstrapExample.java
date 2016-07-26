package daily.y2016.m07.d21.rpc.example;

import daily.y2016.m07.d21.rpc.server.Server;
import daily.y2016.m07.d21.rpc.server.ServerFactory;

public class ServerBootstrapExample {

	public static void main(String[] args) {
		Server server = ServerFactory.getServer();
		server.register("TestRpc", new TestRpcImpl());
	}
}
