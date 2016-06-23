package daily.y2016.m06.d17.mayou.example;

public class TestRpcImpl implements TestRpc{

	@Override
	public int test(byte[] data) {
		System.out.println("invoke tes");
		return 1;
	}
}
