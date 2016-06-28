package daily.template.rpc.mayou.proxy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import daily.template.rpc.mayou.client.ClientFactory;
import daily.template.rpc.mayou.common.Parameters;
import daily.template.rpc.mayou.common.Result;
import daily.template.rpc.mayou.serialize.SerializeException;
import daily.template.rpc.mayou.serialize.Serializer;

import org.jboss.netty.channel.ChannelFuture;

import daily.template.rpc.mayou.api.RpcFactory;

public abstract class BaseConsumerProxy {

	private ThreadLocal<ChannelFuture> channelFutureLocal;

	public BaseConsumerProxy(final String remoteAddress) {
		channelFutureLocal = new ThreadLocal<ChannelFuture>() {

			@Override
			protected ChannelFuture initialValue() {
				return ClientFactory.getClient().getConnection(remoteAddress,
						RpcFactory.DEFAULT_PORT);
			}
		};
	}

	protected Object doInterval(String interfactName, Object[] objs) {
		List<Class<?>> clazzs = new ArrayList<Class<?>>(objs.length);
		List<Object> params = new ArrayList<Object>();
		for (Object obj : objs) {
			clazzs.add(obj.getClass());
			params.add(obj);
		}

		Parameters parameters = new Parameters();
		parameters.setInterfaceName(interfactName);
		parameters.setParameterTypes(clazzs);
		parameters.setParameters(params);

		while (!channelFutureLocal.get().getChannel().isConnected())
			;
		try {
			byte[] data = Serializer.serialize(parameters);

			synchronized (channelFutureLocal.get().getChannel()) {
				channelFutureLocal.get().getChannel().write(data);
				channelFutureLocal.get().getChannel().wait();
			}
			data = ClientFactory.getClient().getResult(
					channelFutureLocal.get().getChannel());
			Result result = Serializer.deserialize(data, Result.class);

			if (!result.isSuccess()) {
				System.out.println("出错啦");
			}

			return result.getResult();
		} catch (SerializeException e) {
			return null;
		} catch (InterruptedException e) {
			return null;
		}
	}

}
