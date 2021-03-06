package daily.template.rpc.rpcf.api;

public interface RpcFactory {
	
	<T> void export(Class<T> type, T serviceObject);
	
	int getClientThreads();
	
	String getAuthorId();
}
