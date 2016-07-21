package daily.y2016.m07.d19.rpc.proxy;

import java.util.ArrayList;
import java.util.List;

import org.jboss.netty.channel.ChannelFuture;

import daily.y2016.m07.d19.rpc.api.RpcFactory;
import daily.y2016.m07.d19.rpc.client.ClientFactory;
import daily.y2016.m07.d19.rpc.common.Parameters;
import daily.y2016.m07.d19.rpc.common.Result;
import daily.y2016.m07.d19.rpc.serialize.SerializeException;
import daily.y2016.m07.d19.rpc.serialize.Serializer;

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
		
		while(!channelFutureThreadLocal.get().getChannel().isConnected())
			;
		try {
			byte[] data = Serializer.serialize(parameters);
			
			synchronized(channelFutureThreadLocal.get().getChannel()) {
				channelFutureThreadLocal.get().getChannel().write(data);
				channelFutureThreadLocal.get().getChannel().wait();
			}
			data = ClientFactory.getClient().getResult(
					channelFutureThreadLocal.get().getChannel());
			Result result = Serializer.deserialize(data, Result.class);
			
			if(!result.isSuccess()) {
				System.out.println("error");
			}
			
			return result.getResult();
		} catch(SerializeException e) {
			return null;
		} catch(InterruptedException e) {
			return null;
		}
		
	}
}
