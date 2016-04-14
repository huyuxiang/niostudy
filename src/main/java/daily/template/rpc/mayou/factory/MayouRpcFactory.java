package daily.template.rpc.mayou.factory;

import daily.template.rpc.mayou.proxy.ProxyFactory;
import daily.template.rpc.mayou.server.ServerFactory;

import daily.template.rpc.mayou.api.RpcFactory;

public class MayouRpcFactory implements RpcFactory {
	
	@Override
	public <T> void export(Class<T> type, T serviceObject) {
		ServerFactory.getServer().register(type.getName(), serviceObject);
	}

	@SuppressWarnings("unchecked")
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
