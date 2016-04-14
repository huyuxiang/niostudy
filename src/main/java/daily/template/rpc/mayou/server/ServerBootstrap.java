package daily.template.rpc.mayou.server;

import daily.template.rpc.mayou.example.TestRpcImpl;

public class ServerBootstrap {

	public static void main(String[] args) {
		Server server = ServerFactory.getServer();
		server.register("daily.template.rpc.mayou.example.TestRpc", new TestRpcImpl());
	}

}
