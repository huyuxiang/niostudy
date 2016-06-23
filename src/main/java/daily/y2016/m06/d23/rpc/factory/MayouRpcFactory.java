package daily.y2016.m06.d23.rpc.factory;

import daily.y2016.m06.d23.rpc.api.RpcFactory;
import daily.y2016.m06.d23.rpc.proxy.ProxyFactory;
import daily.y2016.m06.d23.rpc.server.ServerFactory;

public class MayouRpcFactory implements RpcFactory {

	@Override
	public <T> void export(Class<T> type, T serviceObject) {
		ServerFactory.getServer().register(type.getName(), serviceObject);
	}
	
	@Override
	public <T> T getReference(Class<T> type, String ip) {
		return (T)ProxyFactory.getConsumerProxy(type, ip);
	}
	
	@Override
	public int getClientThreads() {
		return 80;
	}
	
	public String getAuthorId() {
		return "";
	}
}
