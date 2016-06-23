package daily.y2016.m06.d17.mayou.factory;

import daily.y2016.m06.d17.mayou.api.RpcFactory;
import daily.y2016.m06.d17.mayou.proxy.ProxyFactory;
import daily.y2016.m06.d17.mayou.server.ServerFactory;

public class MayouRpcFactory implements RpcFactory{

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
	
	@Override
	public String getAuthorId() {
		return "mayou.lyt";
	}

}
