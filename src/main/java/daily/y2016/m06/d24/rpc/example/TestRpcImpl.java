package daily.y2016.m06.d24.rpc.example;

public class TestRpcImpl implements TestRpc {

	@Override
	public int test(byte[] data) {
		System.out.println("invoke test");
		return 1;
	}

}
