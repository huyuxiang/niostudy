package daily.template.rpc.server;

import daily.template.rpc.example.TestRpcImpl;

public class ServerBootstrap {

	public static void main(String[] args) {
		Server server = ServerFactory.getServer();
		server.register("daily.template.rpc.example.TestRpc", new TestRpcImpl());
	}

}
