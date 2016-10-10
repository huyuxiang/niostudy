package daily.y2016.m08.d22.rpc.a1.server;

import daily.y2016.m08.d22.rpc.a1.serialize.Serializer;

public class Server {

	private final int port;
	
	private static final byte[] failure;
	
	static {
		failure = Serializer.serialize(Result.fail(null, null));
	}
}
