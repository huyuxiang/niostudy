package daily.y2016.m07.d21.rpc.proxy;

import org.jboss.netty.channel.ChannelFuture;

import daily.y2016.m07.d21.rpc.client.ClientFactory;

public class BaseConsumerProxy {

	private ThreadLocal<ChannelFuture> channelFutureThreadLocal;
	
	public BaseConsumerProxy(final String remoteAddress) {
		channelFutureThreadLocal = new ThreadLocal<ChannelFuture>() {
			
			@Override
			protected ChannelFuture initialValue() {
				return ClientFactory.getClient().getConnection(remoteAddress, 
						RpcFactory.DEFAULT_PORT);
			}
		};
	}
	
	protected Object doInterval(String interfaceName, Object[] objs) {
		
	}
}
