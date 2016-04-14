package daily.y2016.m4.d14.rpc.mayou.server;

public class ServerBootstrap {
	public static void main(String[] args) {
		Server server = ServerFactory.getServer();
		server.register("", new TestRpcImpl());
	}
}
