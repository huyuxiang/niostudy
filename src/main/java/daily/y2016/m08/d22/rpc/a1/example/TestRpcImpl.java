package daily.y2016.m08.d22.rpc.a1.example;

public class TestRpcImpl implements TestRpc {

	@Override
	public int test(byte[] data) {
		System.out.println("invoke tes");
		return 1;
	}
}
