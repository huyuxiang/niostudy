package daily.template.rpc.dsf.registry;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import daily.template.rpc.dsf.common.Constants;
import daily.template.rpc.dsf.common.RpcException;
import daily.template.rpc.dsf.common.ServerNode;

public class ZKClientRegistry implements IRegistry{

	private final Logger log = LoggerFactory.getLogger(getClass());
	
	private final String path ;
	
	private final CuratorFramework zkClient;
	
	private final ServerNode clientNode;
	
	private final DynamicHostSet hostSet = new DynamicHostSet();
	
	private PathChildrenCache cachedPath;
	
	private final Object lock = new Object();
	
	public ZKClientRegistry(String path,CuratorFramework zkClient,ServerNode clientNode) {
		
		this.path = path ;
		this.zkClient = zkClient ;
		this.clientNode = clientNode ;
	}
	

	@Override
	public void unregister() {
		try {
			cachedPath.close();
			zkClient.close();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	@Override
	public DynamicHostSet findAllService() {
		
		return hostSet;
	}

	@Override
	public void register(String value) throws Exception {
		
		// 如果zk尚未启动,则启动
        if (zkClient.getState() == CuratorFrameworkState.LATENT) {
        	zkClient.start();
        }

        // 启动监听，失败重试
        addListener(path);
        //consumer端zk注册
        createZookeeperNode(value);
        //provider端信息缓存到consumer端
        cacheProvidersNode();
        //节点信息缓存
        cacheHostNode();

        try {
            cachedPath.start(StartMode.POST_INITIALIZED_EVENT);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
		
	}
	
	/**
	 * consumer端创建zk节点
	 * @param config
	 * @return
	 * @throws RpcException
	 */
	private boolean createZookeeperNode(String config) throws RpcException {
		
            String address = clientNode.genAddress();
            StringBuilder pathBuilder = new StringBuilder(path);
            pathBuilder.append(Constants.ZK_SEPARATOR).append(Constants.ZK_NAMESPACE_CONSUMERS).append(Constants.ZK_SEPARATOR).append(address);
            // 创建节点
            try {
                zkClient.create().creatingParentsIfNeeded()
                		.withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                		.forPath(pathBuilder.toString(),config.getBytes(Constants.UTF8));
                return true;
                
//                if (zkClient.checkExists().forPath(pathBuilder.toString()) == null) {
//                	zkClient.create().creatingParentsIfNeeded()
//                    .withMode(CreateMode.EPHEMERAL).forPath(pathBuilder.toString(), config.getBytes(Constants.UTF8));
//                    return true;
//                }
            } catch (Exception e) {
                String message = MessageFormat.format("Create node error in the path : {0}", pathBuilder.toString());
                log.error(message, e);
                throw new RpcException(message, e);
            }
       
    }
	
	/**
	 * zk节点创建监听器
	 * @param config
	 */
    private void addListener(final String config) {
        zkClient.getConnectionStateListenable().addListener(new ConnectionStateListener() {
            @Override
            public void stateChanged(CuratorFramework curatorFramework, ConnectionState connectionState) {
                if (connectionState == ConnectionState.LOST) {
                    while (true) {
                        try {
                            if (curatorFramework.getZookeeperClient().blockUntilConnectedOrTimedOut()) {
                                if (createZookeeperNode(config)) {
                                    break;
                                }
                            }
                        } catch (Exception e) {
                            log.error(e.getMessage(), e);
                            break;
                        }
                    }
                }
            }
        });
    }
	/**
     * 缓存provider主机列表 
     * 注意：构建时直接操作ZK，不能使用PathChildrenCache
     * 
     * @throws RpcException
     */
    private void cacheHostNode() throws RpcException {
        List<String> childrenList = null;
        String servicePath = path + Constants.ZK_SEPARATOR + Constants.ZK_NAMESPACE_PROVIDERS;
        try {
            childrenList = zkClient.getChildren().forPath(servicePath);
        } catch (Exception e) {
            String message = MessageFormat.format("Get children node error,the path : {0}", servicePath);
            log.error(message, e);
            throw new RpcException(message, e);
        }

        if (CollectionUtils.isEmpty(childrenList)) {
            log.error("Not find a service in zookeeper!");
            throw new RpcException("Not find a service in zookeeper!");
        }

        List<ServerNode> current = new ArrayList<ServerNode>();
        for (String children : childrenList) {
            current.add(ServerNode.transfer(children));
        }
        /**
         * 缓存provider主机列表
         */
        synchronized (lock) {
			hostSet.replaceWithList(current);
		}
    }
    
	/**
	 * provider相关信息缓存
	 * 
	 * zk里provider相关信息缓存到consumer端
	 * 并watch节点，节点数据变化rebuild
	 */
	private void cacheProvidersNode(){
		
		cachedPath = new PathChildrenCache(zkClient, path + Constants.ZK_SEPARATOR + Constants.ZK_NAMESPACE_PROVIDERS, true);
		cachedPath.getListenable().addListener(new PathChildrenCacheListener() {
	            @Override
	            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) {
	                PathChildrenCacheEvent.Type eventType = event.getType();
	                switch (eventType) {
	                    //case CONNECTION_SUSPENDED:
	                    //	log.info("Connection suspended....");
	                	case INITIALIZED:
	                		System.out.println("initialized");
	                		log.info("listening child node INITIALIZED");
	                		//break;
	                		return ;
		                case CHILD_ADDED:
		                	System.out.println("add");
		                	log.info("listening child node added: "+event.getData().getPath());
		                	break;
		                case CHILD_REMOVED:
		                	System.out.println("remove");
		                	log.info("listening child node removed:"+event.getData().getPath());
		                	break;
		                case CHILD_UPDATED:
		                	System.out.println("update");
		                	log.info("listening child node updated:"+event.getData().getPath());
		                	break;
		                case CONNECTION_RECONNECTED:
		                	System.out.println("reconnected");
		                	log.info("listening child node CONNECTION_RECONNECTED");
		                	break;
		                case CONNECTION_SUSPENDED:
		                	System.out.println("suspended");
		                	break;
	                    case CONNECTION_LOST:
	                        log.error("Connection error,waiting...");
	                        return;
	                    default:
	                }
	                
	                // 任何节点的时机数据变动,都会rebuild
					try {
						//cachedPath.rebuild();
						log.info("rebuild node on" + event.getData().getPath());
						System.out.println("rebuild node on" + event.getData().getPath());
						cachedPath.rebuildNode(event.getData().getPath());
						
						log.info("rebuild the local cache ....");
						freshHostNode();
					} catch (Exception e) {
						log.error("CachedPath rebuild error!", e);
						e.printStackTrace();
					}
	            }
	        });
	}
	
	/**
	 * 刷新provider主机列表
	 */
	private void freshHostNode() {
        List<ChildData> children = cachedPath.getCurrentData();
        if (children == null || children.isEmpty()) {
            // 有可能所有的 provider都与ZK断开了连接;可能provider与consumer通信良好
            log.error("[ERROR]: All of zookeeper's node is empty !");
            return;
        }
        List<ServerNode> current = new ArrayList<ServerNode>();
        for (ChildData data : children) {
            String path = data.getPath();
            String address = path.substring(path.lastIndexOf(Constants.ZK_SEPARATOR) + 1);
            log.debug("Server address {}.", address);
            current.add(ServerNode.transfer(address));
        }
        synchronized (lock) {
			hostSet.replaceWithList(current);
		}
    }

}
