package rpcdemo.rpc.api;

public interface RpcFactory {

	static final int DEFAULT_PORT = 121211;
	<T> void export (Class<T> type, T serviceObject);
	<T> T getReference(Class<T> type, String ip);
	
	int getClientThreads();
	
	String getAuthorId();
}
