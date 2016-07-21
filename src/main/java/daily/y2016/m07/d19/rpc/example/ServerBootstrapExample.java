package daily.y2016.m07.d19.rpc.example;

public class ServerBootstrapExample {

	public static void main(String[] args) {
		Server server = ServerFactory.getServer();
		server.register("TestRpc", new TestRpcImpl());
	}
}
