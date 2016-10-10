package daily.y2016.m08.d31.rpc.api;

public interface RpcFactory {

	int DEFAULT_PORT = 121211;
	
	<T> void export(Class<T> type, T serviceObject);
	
	<T> T getReference(Class<T> type, String ip);
	
}
