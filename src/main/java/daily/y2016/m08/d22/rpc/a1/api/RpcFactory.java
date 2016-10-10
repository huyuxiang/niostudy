package daily.y2016.m08.d22.rpc.a1.api;

public interface RpcFactory {

	int DEFAULT_PORT = 12121;
	
	<T> void export(Class<T> type, T serviceObject);
	
	<T> T getReference(Class<T> type, String ip);
	
	
}
