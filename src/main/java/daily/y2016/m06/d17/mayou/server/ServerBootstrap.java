package daily.y2016.m06.d17.mayou.server;

import daily.y2016.m06.d17.mayou.example.TestRpcImpl;

public class ServerBootstrap {

	public static void main(String[] args) {
		Server server = ServerFactory.getServer();
		server.register("daily.template.rpc.mayou.example.TestRpc", new TestRpcImpl());
	}
}
