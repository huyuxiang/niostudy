package daily.y2016.m06.d29.a2.rpc.example;

public class TestRpcImpl implements TestRpc {

	@Override
	public int test(byte[] data) {
		System.out.println("invoke test");
		return 1;
	}
}
