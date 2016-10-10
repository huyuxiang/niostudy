package daily.y2016.m08.d31.rpc.example;

public class ServerBootstrapExample {

	public static void main(String[] args) {
		Server server = ServerFactory.getServer();
		server.register("", new TestRpcImpl());
	}
}
