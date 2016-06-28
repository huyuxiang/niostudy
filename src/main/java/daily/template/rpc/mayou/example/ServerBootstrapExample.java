package daily.template.rpc.mayou.example;

import daily.template.rpc.mayou.server.Server;
import daily.template.rpc.mayou.server.ServerFactory;

public class ServerBootstrapExample {

	public static void main(String[] args) {
		Server server = ServerFactory.getServer();
		server.register("daily.template.rpc.mayou.example.TestRpc", 
				new TestRpcImpl());
	}

}
