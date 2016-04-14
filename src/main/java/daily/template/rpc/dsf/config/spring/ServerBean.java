package daily.template.rpc.dsf.config.spring;

import java.util.HashSet;
import java.util.Set;

import org.apache.avro.ipc.reflect.ReflectResponder;
import org.apache.commons.lang.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.alibaba.fastjson.JSON;
import daily.template.rpc.dsf.common.Constants;
import daily.template.rpc.dsf.common.NetUtils;
import daily.template.rpc.dsf.common.RpcException;
import daily.template.rpc.dsf.common.ServerNode;
import daily.template.rpc.dsf.registry.IRegistry;
import daily.template.rpc.dsf.registry.ZKServerRegistry;
import daily.template.rpc.dsf.server.AvroServer;
import daily.template.rpc.dsf.server.IDsfServer;
import daily.template.rpc.dsf.server.proxy.DynamicServiceHandler;

/**
 * Spring schema 配置 <dsf:server .../>
 * @author ZhongweiLee
 */
public class ServerBean implements ApplicationContextAware ,InitializingBean{
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	private String id;
	
	private String iface;
	
	private transient Object ref;
	
	private transient ApplicationContext applicationContext;
	
	private String ip ;
	
	private int port = Constants.RPC_DEFAULT_PORT;
	

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		
		this.applicationContext = applicationContext;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		
		//***********反射provider*********
		
		Class<?> serverClass = getRef().getClass();
		
		Class<?> ifaceClass = Class.forName(getIface());
		
		Set<String> hashSet = new HashSet<String>();
		for (Class<?> clazz : serverClass.getInterfaces()) {
		 	hashSet.add(clazz.getName());
		}
		
		if(!hashSet.contains( getIface() ) ){
			throw new RpcException(serverClass.getName()+ " should implements "+ getIface());
		}
		
		DynamicServiceHandler dynamicServiceHandler = new DynamicServiceHandler();
		Object target = dynamicServiceHandler.bind(ref);
		
		AvroServer server = new AvroServer(new ReflectResponder(ifaceClass, target),this.port);
		
		//启动服务
		server.start();
		
		//******************************
        RegistryBean registryBean = applicationContext.getBean(RegistryBean.class);
        //获取zkClient
        CuratorFramework zkClient  = registryBean.getZkClient();
        
        //服务注册
        IRegistry registry = new ZKServerRegistry(zkClient, Constants.ZK_SEPARATOR+getIface(), 
        											getServerNode().genAddress(), 
        											registryBean.getUsername(), registryBean.getPassword());
        
        if (server.isStarted()) {
            try {
                // 暴露服务
                registry.register(genConfigJson());
                // 添加关闭钩子
                addShutdownHook(registry, server);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                e.printStackTrace();
                server.stop();
            }
        } else {
            server.stop();
        }
	}
	/**
	 * 添加关闭钩子，JVM关闭时会执行此方法
	 * @param registry
	 * @param server
	 */
    protected void addShutdownHook(final IRegistry registry, final IDsfServer server) {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                if (registry != null) {
                    registry.unregister();
                }
                if (server != null) {
                    server.stop();
                }
            }
        }));
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Object getRef() {
		return ref;
	}

	public void setRef(Object ref) {
		this.ref = ref;
	}
	
	public String getIface() {
		return iface;
	}

	public void setIface(String iface) {
		this.iface = iface;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * 生成服务节点，如果IP为空，获取当前操作系统IP
	 * @return
	 */
	protected ServerNode getServerNode() {
		
		if (StringUtils.isEmpty(ip)) {
			ip = NetUtils.getLocalHost();
		}
		if (StringUtils.isEmpty(ip)) {
			throw new RpcException("Can't find server ip!");
		}
		return new ServerNode(ip, port);
	}
	
    /**
     * 生成配置文件的json格式
     * 
     * @return 配置的json格式
     */
    protected String genConfigJson() {
        return JSON.toJSONString(this);
    }
}
