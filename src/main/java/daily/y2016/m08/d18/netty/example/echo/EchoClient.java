package daily.y2016.m08.d18.netty.example.echo;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

public class EchoClient {

	public static void main(String[] args) {
		final String host = "127.0.0.1";
		final int port = 8080;
		final int firstMessageSize = 256;
		
		ClientBootstrap bootstrap = new ClientBootstrap(
				new NioClientSocketChannelFactory(
						Executors.newCachedThreadPool(), 
						Executors.newCachedThreadPool()));
		
		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() throws Exception {
				return Channels.pipeline(
						new EchoClientHandler(firstMessageSize));
			}
		});
		
		ChannelFuture future = bootstrap.connect(new InetSocketAddress(host, port));
		future.getChannel().getCloseFuture().awaitUninterruptibly();
		
		bootstrap.releaseExternalResources();
	}
}
