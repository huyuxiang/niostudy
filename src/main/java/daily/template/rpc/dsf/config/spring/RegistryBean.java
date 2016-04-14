package daily.template.rpc.dsf.config.spring;

import org.apache.commons.lang.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import daily.template.rpc.dsf.common.Constants;
import daily.template.rpc.dsf.common.RpcException;


public class RegistryBean {
	
	
	private String id ;
	
	//zk地址
	private String url;
	//超时时间
	private int timeout = Constants.ZK_DEFAULT_TIMEOUT;
	//重试次数
	private int retry = Constants.ZK_DEFAULT_RETRY;
	//共享zk连接
	private boolean singleton = true ;
	//应用命名空间
    private String namespace = Constants.ZK_NAMESPACE_ROOT;
    //zk用户名
    private String username;
    //zk密码
    private String password;

    private CuratorFramework zkClient;
    
    
    public CuratorFramework getZkClient() throws RpcException {
        check(); // 配置检查
        if (singleton) {
            if (zkClient == null) {
                zkClient = createConnection();
                zkClient.start();
            }
            return zkClient;
        }
        zkClient = createConnection();
        return zkClient;
    }
    
    public void check() throws RpcException {
        if (StringUtils.isEmpty(url)) {
            throw new RpcException(RpcException.CONFIG_EXCEPTION, "please check <dsf:registry /> url properties is null!");
        }
    }
    
    private CuratorFramework createConnection() throws RpcException {
        return createConnection(url, timeout, namespace, retry);
    }
    
    private CuratorFramework createConnection(String url, Integer sessionTimeout, String namespace, int retry) {
        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder();
        	
        String auth = StringUtils.defaultIfEmpty(username,"") + ":" +StringUtils.defaultIfEmpty(password, "");
        builder.authorization("digest", auth.getBytes());
        return builder.connectString(url)
        			.sessionTimeoutMs(sessionTimeout)
        			.connectionTimeoutMs(sessionTimeout)
        			.namespace(namespace)
        			.retryPolicy(new ExponentialBackoffRetry(Constants.ZK_RETRY_INTERVAL,retry)).defaultData(null).build();
    }
    public void close() {
        if (zkClient != null) {
            zkClient.close();
        }
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public int getRetry() {
		return retry;
	}

	public void setRetry(int retry) {
		this.retry = retry;
	}

	public boolean isSingleton() {
		return singleton;
	}

	public void setSingleton(boolean singleton) {
		this.singleton = singleton;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setZkClient(CuratorFramework zkClient) {
		this.zkClient = zkClient;
	}

    
    

}
