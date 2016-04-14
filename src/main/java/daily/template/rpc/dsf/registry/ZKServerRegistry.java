package daily.template.rpc.dsf.registry;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.ConnectionLossException;
import org.apache.zookeeper.ZooDefs.Perms;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import daily.template.rpc.dsf.common.Constants;
import daily.template.rpc.dsf.common.RpcException;

public class ZKServerRegistry implements IRegistry {

	private final transient Logger log = LoggerFactory.getLogger(getClass());

	private final CuratorFramework zkClient;

	/** ZooKeeper中service目录 eg: /dsf/com.iqitoo.demo.TestService */
	private final String servicePath;
	/**
	 * provider 目录 如果是 /XXX 则在ZooKeeper中会create "/dsf/com.iqitoo.demo.TestService/providers/xxxxx"
	 */
	private final String providerAddress;
	/**ZooKeeper用户名*/
	private final String username;
	/** ZooKeeper密码 */
	private final String password;

	/**
	 * 
	 * @param zkClient
	 * @param servicePath
	 * 		  eg:/dsf/com.iqitoo.demo.TestService
	 * @param providerAddress
	 * 		      如果是/XXX 会在zookeeper中创建  /dsf/com.iqitoo.demo.TestService/providers/XXX
	 * @param username 用户名
	 * @param password 密码
	 */
	public ZKServerRegistry(CuratorFramework zkClient, final String servicePath,
			final String providerAddress,final String username,final String password) {
		this.zkClient = zkClient;
		this.servicePath = servicePath;
		this.providerAddress = providerAddress;
		this.username = username;
		this.password = password;
	}

	@Override
	public void register(final String value) throws Exception {

		if (zkClient.getState() == CuratorFrameworkState.LATENT) {

			zkClient.start();

			zkClient.createContainers(servicePath);
		}
		addListener(value);

		createNode(value);
	}

	/**
	 * 创建zk节点
	 * @param value
	 * @return
	 */
	private boolean createNode(final String value) {

		// 创建父节点
		createParentsNode();

		// 创建子节点
		return createChildrensNode(value);

	}

	/**
	 * 创建子节点
	 * @param value
	 * @return
	 */
	private boolean createChildrensNode(final String value) {

		// 创建子节点
		StringBuilder pathBuilder = new StringBuilder(servicePath);
		pathBuilder.append(Constants.ZK_SEPARATOR)
				.append(Constants.ZK_NAMESPACE_PROVIDERS)
				.append(Constants.ZK_SEPARATOR).append(providerAddress);
		try {
			if (zkClient.checkExists().forPath(pathBuilder.toString()) == null) {
				zkClient.create()
						.withMode(CreateMode.EPHEMERAL)
						.forPath(pathBuilder.toString(),value.getBytes(Constants.UTF8));
				
				log.info("create childrens node success ！"+value.getBytes(Constants.UTF8));
				return true;
			}
		}catch(KeeperException e){
			String message = ("Can not connection zookeeper,Please check config <dsf:registry .../>");
			log.error(message);
			throw new RpcException(message, e);
		}catch (Exception e) {
			String message = MessageFormat.format("createChildrensNode error in the path : {0} on zookeeper",pathBuilder.toString());
			log.error(message, e);
			throw new RpcException(message, e);
		}
		return false;
	}

	/**
	 * 创建父节点
	 */
	private void createParentsNode() {

		//  /dsf/com.iqitoo.demo.TestService/providers
		String parentPath = servicePath + Constants.ZK_SEPARATOR + Constants.ZK_NAMESPACE_PROVIDERS;
		try {
			if (zkClient.checkExists().forPath(parentPath) == null) {
				
				String auth = null ;
				
				auth = StringUtils.defaultIfEmpty(username,"")+":"+StringUtils.defaultIfEmpty(password, "");
				
				Id id = new Id("digest",DigestAuthenticationProvider.generateDigest(auth));
				List<ACL> acls = new ArrayList<ACL>(2);
				ACL acl = new ACL(Perms.CREATE, id);// 创建子节点权限，供其他server创建
				Id id2 = new Id("world", "anyone");
				ACL acl2 = new ACL(Perms.READ, id2);// read权限，供管理系统使用
				acls.add(acl);
				acls.add(acl2);
				zkClient.create().creatingParentsIfNeeded()
						.withMode(CreateMode.PERSISTENT).withACL(acls)
						.forPath(parentPath);
			}
		}catch(KeeperException e){
			String message = ("Can not connection zookeeper,Please check config <dsf:registry .../>");
			log.error(message);
			throw new RpcException(message, e);
		} 
		catch (Exception e) {
			String message = MessageFormat.format("registry error in the path : {0} on zookeeper", parentPath);
			log.error(message);
			throw new RpcException(message, e);
		}
	}

	/**
	 * 添加监听器，防止网络异常或者zk挂掉
	 * 
	 * @param value
	 */
	private void addListener(final String value) {
		zkClient.getConnectionStateListenable().addListener(
				new ConnectionStateListener() {
					@Override
					public void stateChanged(CuratorFramework curatorFramework,ConnectionState connectionState) {
						if (connectionState == ConnectionState.LOST) { // session过期的情况
							while (true) {
								try {
									if (curatorFramework.getZookeeperClient().blockUntilConnectedOrTimedOut()) {
										if (createNode(value)) {
											break;
										}
									}
								} catch (Exception e) {
									e.printStackTrace();
									break;
								}
							}
						}
					}
				});
	}

	@Override
	public void unregister() {
		
		if (zkClient.getState() != CuratorFrameworkState.STOPPED) {
			
			zkClient.close();
		}
	}

	@Override
	public DynamicHostSet findAllService() {
		// 仅consumer端需要
		return null;
	}

	public CuratorFramework getZkClient() {
		return zkClient;
	}

	public String getServicePath() {
		return servicePath;
	}

	public String getProviderAddress() {
		return providerAddress;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

}
