package daily.y2016.m09.d05.rpc.api;

public interface RpcFactory {

	int DEFAULT_PORT = 12121;
	
	<T> void export(Class<T> type, T serviceObject);
	
	<T> T getReference(Class<T> type, String ip);
	
	
}
