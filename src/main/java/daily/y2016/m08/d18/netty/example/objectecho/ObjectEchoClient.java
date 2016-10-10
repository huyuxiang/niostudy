package daily.y2016.m08.d18.netty.example.objectecho;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

public class ObjectEchoClient {

	public static void main(String[] args) {
		if(args.length<2||args.length>3) {
			return ;
		}
		
		final String host = args[0];
		final int port = Integer.parseInt(args[1]);
		final int firstMessageSize;
		
		if(args.length==3) {
			firstMessageSize = Integer.parseInt(args[2]);
		} else {
			firstMessageSize = 256;
		}
		
		ClientBootstrap bootstrap = new ClientBootstrap(
				new NioClientSocketChannelFactory(
						Executors.newCachedThreadPool(), 
						Executors.newCachedThreadPool()));
		
		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() throws Exception {
				return Channels.pipeline(
						new ObjectEncoder(), 
						new ObjectDecoder(), 
						new ObjectEchoClientHandler(firstMessageSize));
			}
		});
		
		bootstrap.connect(new InetSocketAddress(host, port));
	}
}
