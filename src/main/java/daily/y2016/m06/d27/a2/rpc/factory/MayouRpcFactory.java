package daily.y2016.m06.d27.a2.rpc.factory;

import daily.y2016.m06.d27.a2.rpc.api.RpcFactory;
import daily.y2016.m06.d27.a2.rpc.proxy.ProxyFactory;
import daily.y2016.m06.d27.a2.rpc.server.ServerFactory;

public class MayouRpcFactory implements RpcFactory {

	@Override
	public <T> void export(Class<T> type, T serviceObject) {
		ServerFactory.getServer().register(type.getName(), serviceObject);
	}
	
	@Override
	public <T> T getReference(Class<T> type, String ip) {
		return (T)ProxyFactory.getConsumerProxy(type, ip);
	}
	
}
