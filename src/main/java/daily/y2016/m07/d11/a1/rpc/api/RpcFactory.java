package daily.y2016.m07.d11.a1.rpc.api;

public interface RpcFactory {

	int DEFAULT_PORT = 12121;
	
	<T> void export(Class<T> type, String ip);
	
	<T> T getReference(Class<T> type, String ip);
}
