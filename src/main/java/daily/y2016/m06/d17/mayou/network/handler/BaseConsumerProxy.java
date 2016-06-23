package daily.y2016.m06.d17.mayou.network.handler;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.jboss.netty.channel.ChannelFuture;

import daily.y2016.m06.d17.mayou.api.RpcFactory;
import daily.y2016.m06.d17.mayou.client.ClientFactory;
import daily.y2016.m06.d17.mayou.common.Parameters;
import daily.y2016.m06.d17.mayou.common.Result;
import daily.y2016.m06.d17.mayou.serialize.SerializeException;
import daily.y2016.m06.d17.mayou.serialize.Serializer;

public abstract  class BaseConsumerProxy {

	private static final AtomicLong count = new AtomicLong(0);
	
	private ThreadLocal<ChannelFuture> channelFutureLocal;
	
	static {
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run () {
				while(true) {
					System.out.println(count.get());
					try {
						Thread.sleep(1000);
					} catch(InterruptedException e) {
						
					}
				}
			}
		});
	}
	
	public BaseConsumerProxy(final String remoteAddress) {
		channelFutureLocal = new ThreadLocal<ChannelFuture>() {
			
			@Override
			protected ChannelFuture initialValue() {
				return ClientFactory.getClient().getConnection(remoteAddress, 
						RpcFactory.DEFAULT_PORT);
			}
		};
	}
	
	public Object doInterval(String interfaceName, Object[] objs) {
		List<Class<?>> clazzs = new ArrayList<Class<?>>(objs.length);
		
		List<Object> params = new ArrayList<Object>();
		for(Object obj: objs) {
			clazzs.add(obj.getClass());
			params.add(obj);
		}
		
		Parameters parameters = new Parameters();
		parameters.setInterfaceName(interfaceName);
		parameters.setParameterTypes(clazzs);
		parameters.setParameters(params);
		
		while(!channelFutureLocal.get().getChannel().isConnected()) 
			;
		try {
			byte[] data = Serializer.serialize(parameters);
			
			channelFutureLocal.get().getChannel().write(data);
			synchronized (channelFutureLocal.get().getChannel()) {
				channelFutureLocal.get().getChannel().wait();
			}
			data = ClientFactory.getClient().getResult(
					channelFutureLocal.get().getChannel());
			Result result = Serializer.deserializer(data, Result.class);
			
			if(!result.isSuccess()) {
				System.out.println("出错啦");
			}
			return result.getResult();
		} catch(SerializeException e) {
			return null;
		} catch(InterruptedException e) {
			return null;
		}
	}
}
