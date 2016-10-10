package daily.y2016.m09.d23;

public class TestRpcImpl implements TestRpc {

	@Override
	public int test(byte[] data) {
		System.out.println("invoke test");
		return 1;
	}
}
