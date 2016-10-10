package daily.y2016.m09.d23;

public class ServerBootstrapExample {

	public static void main(String[] args) {
		Server server = ServerFactory.getServer();
		server.register("", new TestRpcImpl());
	}
}
