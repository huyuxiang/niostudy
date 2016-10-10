package daily.y2016.m08.d22.rpc.a1.example;

public class ServerBootstrapExample {

	public static void main(String[] args) {
		Server server = ServerFactory.getServer();
		server.register("", new TestRpcImpl());
	}
}
