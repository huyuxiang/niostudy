package daily.y2016.m09.d23;

public class ClientFactory {

	private static Client client = new Client(RpcFactory.DEFAULT_PORT, "127.0.0.1");
	
	static {
		client = new Client(RpcFactory.DEFAULT_PORT, "127.0.0.1");
		client.start();
	}
	
	public static Client getClient() {
		return client;
	}
}
