package daily.y2016.m08.d18.netty.example.factorial;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

public class FactorialClient {

	public static void main(String[] args) {
		String host= "127.0.0.1";
		int port= 8080;
		int count = 1;
		
		ClientBootstrap bootstrap = new ClientBootstrap(
				new NioClientSocketChannelFactory(
						Executors.newCachedThreadPool(), 
						Executors.newCachedThreadPool()));
		
		bootstrap.setPipelineFactory(new FactorialClientPipelineFactory(count));
		
		ChannelFuture connectFuture = 
				bootstrap.connect(new InetSocketAddress(host, port));
		
		Channel channel = connectFuture.awaitUninterruptibly().getChannel();
		
		FactorialClientHandler handler = (FactorialClientHandler)channel.getPipeline().getLast();
		
		System.err.format("factorial of %,d is: %,d", count, handler.getFactorial());
		
		bootstrap.releaseExternalResources();
	}
}
