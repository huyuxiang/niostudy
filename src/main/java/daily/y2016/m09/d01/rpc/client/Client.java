package daily.y2016.m09.d01.rpc.client;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

import daily.y2016.m09.d01.rpc.network.handler.Decoder;
import daily.y2016.m09.d01.rpc.network.handler.Encoder;
import daily.y2016.m09.d01.rpc.network.handler.ResultHandler;

public class Client {

	private final int port;
	
	private final String remoteAddress;
	
	private ClientBootstrap clientBootstrap;
	
	private ChannelFactory channelFactory;
	
	private volatile boolean isStart;
	
	final private ConcurrentMap<Channel, byte[]> resultMap;
	
	public Client(int port, String remoteAddress) {
		this.port = port;
		this.remoteAddress = remoteAddress;
		resultMap = new ConcurrentHashMap<Channel, byte[]>();
		isStart = false;
	}
	
	public synchronized void start() {
		if(isStart) {
			throw new RuntimeException("client not strated");
		} else {
			isStart = true;
			channelFactory = new NioClientSocketChannelFactory(
					Executors.newFixedThreadPool(1),
					Executors.newFixedThreadPool(8), 1,8);
			
			clientBootstrap = new ClientBootstrap(channelFactory);
			clientBootstrap.setPipelineFactory(new ChannelPipelineFactory() {
				
				@Override
				public ChannelPipeline getPipeline() throws Exception {
					return Channels.pipeline(new Decoder(), new ResultHandler(
							Client.this), new Encoder());
				}
			});
			clientBootstrap.setOption("tcpKeepAlive", true);
		}
	}
	
	public synchronized void stop() {
		if(!isStart) {
			throw new RuntimeException("client not started");
		} else {
			isStart = false;
		}
	}
	
	public void putResult(Channel channel, byte[] result) {
		if(isStart) {
			resultMap.put(channel, result);
			synchronized(channel) {
				channel.notify();
			}
		} else {
			throw new RuntimeException("client not started");
		}
	}
	
	public byte[] getResult(Channel channel) {
		if(isStart) {
			byte[] result = resultMap.get(channel);
			resultMap.remove(channel);
			return result;
		} else {
			throw new RuntimeException("client not started");
		}
	}
	
	public ChannelFuture getConnection(String remoteAddress, int port) {
		if(isStart) {
			return clientBootstrap.connect(new InetSocketAddress(remoteAddress, port));
		} else {
			throw new RuntimeException("client not started");
		}
	}
}
