package daily.y2016.m09.d01.rpc.example;

import daily.template.rpc.mayou.example.TestRpc;
import daily.y2016.m09.d01.rpc.proxy.ProxyFactory;

public class ClientBootstrapExample {

	public static void main(String[] args) {
		TestRpc testRpc = (TestRpc) ProxyFactory.getConsumerProxy(TestRpc.class, "127.0.0.1");
	}
}
