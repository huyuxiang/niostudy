package daily.template.rpc.rpcf.factory;

import daily.template.rpc.rpcf.api.RpcFactory;
import daily.template.rpc.rpcf.server.ServerFactory;

public class GodzillaRpcFactory implements RpcFactory{
	
	public static final int CLIENT_THREADS = 80;
	
	public <T> void export(Class<T> type, T serviceObject) {
		ServerFactory.getServer().register(type.getName(), serviceObject);
	}
	
	public int getClientThreads() {
		return CLIENT_THREADS;
	}
	
	public String getAuthorId(){
		return "godzilla";
	}
}
