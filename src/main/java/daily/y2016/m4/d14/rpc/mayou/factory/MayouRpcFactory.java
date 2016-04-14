package daily.y2016.m4.d14.rpc.mayou.factory;

import daily.template.rpc.mayou.api.RpcFactory;

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
	
	@Override
	public String getAuthorId() {
		return "mayou";
	}
}
