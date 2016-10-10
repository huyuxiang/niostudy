package daily.y2016.m07.d29.rpc.example;

public class ServerBootstrapExample {

	public static void main(String[] args) {
		Server server = ServerFactory.getServer();
		server.register("", new TestRpcImpl());
	}
}
