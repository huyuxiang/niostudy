package daily.template.rpc.rpcf.benchmark.service;

public interface HelloService {

	String helloWorld(String projectCode, String profile, String attr) throws NumberFormatException, InterruptedException;
}
