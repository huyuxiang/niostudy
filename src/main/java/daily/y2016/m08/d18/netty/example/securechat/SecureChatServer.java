package daily.y2016.m08.d18.netty.example.securechat;

public class SecureChatServer {

	public static void main(String[] args) {
		ServerBootstrap bootstrap = new ServerBootstrap(
				new NioServerSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()));
		
		bootstrap.setPipelineFactory(new SecureChatServerPipelineFactory());
		
		bootstrap.bind(new InetSocketAddress(8080));
	}
}
