package daily.template.rpc.dsf.common;

public class Constants {
	
	/**编码格式*/
	public static final String UTF8 = "utf-8";
	
	/**节点分隔符*/
	public static final String ZK_SEPARATOR = "/";
	
	/**应用zk的命名空间*/
	public static final String ZK_NAMESPACE_ROOT = "dsf";
	
	/**生产者配置节点*/
	public static final String ZK_NAMESPACE_PROVIDERS = "providers";
	
	/**消费者zk配置节点*/
    public static final String ZK_NAMESPACE_CONSUMERS = "consumers";

    /**存放配置的zk节点*/
    public static final String ZK_NAMESPACE_CONFIGS = "configurators";

    /**zk超时时间*/
	public static final int ZK_DEFAULT_TIMEOUT = 3000;

	/**zk默认重试次数*/
	public static final int ZK_DEFAULT_RETRY = 2;
	
	/**zk重试时间间隔1s */
	public static final int ZK_RETRY_INTERVAL = 1000;

	/**RPC 默认端口 */
	public static final int RPC_DEFAULT_PORT = 19090;

}
