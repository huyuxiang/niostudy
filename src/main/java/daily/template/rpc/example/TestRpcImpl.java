package daily.template.rpc.example;

public class TestRpcImpl implements TestRpc {

	@Override
	public int test(byte[] data) {
		System.out.println("invoke test");
		return 1;
	}

}
