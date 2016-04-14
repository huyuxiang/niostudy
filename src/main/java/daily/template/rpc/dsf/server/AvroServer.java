package daily.template.rpc.dsf.server;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.apache.avro.ipc.NettyServer;
import org.apache.avro.ipc.reflect.ReflectResponder;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

/**
 * 基于Avro协议的服务端
 * @author ZhongweiLee
 *
 */
public class AvroServer implements IDsfServer {

	private final NettyServer nettyServer;

	private boolean isStarted = false;
	
	
	public AvroServer(ReflectResponder reflectResponder,final int port) {
		
		ChannelFactory channelFactory = new NioServerSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool());

		this.nettyServer = new NettyServer(reflectResponder,
				new InetSocketAddress(port), channelFactory);
	}

	@Override
	public void start() {
		
		if(!isStarted){
			nettyServer.start();
			isStarted = true ;
			System.out.println("DsfAvroServer is start!");
		}

	}

	@Override
	public void stop() {
		
		if(isStarted){
			nettyServer.close();
			isStarted = false;
		}

		System.out.println("DsfAvroServer is closed!");
	}

	@Override
	public boolean isStarted() {
		
		return this.isStarted;
	}
	

	
}
