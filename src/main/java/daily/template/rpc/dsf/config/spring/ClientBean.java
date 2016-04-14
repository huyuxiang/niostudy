package daily.template.rpc.dsf.config.spring;

import java.util.HashSet;
import java.util.Iterator;

import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import daily.template.rpc.dsf.client.AvroClientInvoker;
import daily.template.rpc.dsf.client.Invoker;
import daily.template.rpc.dsf.client.proxy.DynamicClientHandler;
import daily.template.rpc.dsf.common.Constants;
import daily.template.rpc.dsf.common.NetUtils;
import daily.template.rpc.dsf.common.ServerNode;
import daily.template.rpc.dsf.registry.ZKClientRegistry;

/**
 * Spring schema 配置Bean <dsf:client .../>
 * @author ZhongweiLee
 *
 */
@SuppressWarnings("rawtypes")
public class ClientBean implements FactoryBean, ApplicationContextAware{
	
	private final transient Logger log = LoggerFactory.getLogger(getClass());
	
	private String id;
	
	private String iface;
	
	private String ip = "0";
	
	private int port = Constants.RPC_DEFAULT_PORT;
	
	
	private transient ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		
		this.applicationContext = applicationContext;
	}


	private Object createProxy() throws Exception {
		
		RegistryBean registryBean = applicationContext.getBean(RegistryBean.class);
		
		CuratorFramework zkClient = registryBean.getZkClient();
		
		ServerNode clientNode = new ServerNode(NetUtils.getLocalHost(), 0);
		
		ZKClientRegistry registry = new ZKClientRegistry(Constants.ZK_SEPARATOR+getIface(), zkClient, clientNode);
		
		registry.register(genConfigJson());
		
		HashSet<ServerNode> hostSet = (HashSet<ServerNode>) registry.findAllService().getAllNodes();
		
		ServerNode node = null ;
		Iterator<ServerNode> it = hostSet.iterator();
		
		while (it.hasNext()) {
			node = it.next();
			break ;
		}
		
		//*******************************************//
		
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        // 加载Iface接口
        Class<?> ifaceClass = null;
			
        ifaceClass = classLoader.loadClass(getIface());
        
        Invoker invoker = new AvroClientInvoker(node.getIp(),iface,node.getPort());
        
        DynamicClientHandler dynamicClientHandler = new DynamicClientHandler(invoker);
        
        return dynamicClientHandler.bind(classLoader, ifaceClass);
        
	}

	private String genConfigJson() {
		return JSON.toJSONString(this);
	}


	@Override
	@JSONField(serialize = false)
	public Object getObject() throws Exception {
		
		return createProxy();
	}

	@Override
	@JSONField(serialize = false)
	public Class<?> getObjectType() {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		// 加载interface
		Class<?> objectClass = null;
		try {
			objectClass = classLoader.loadClass(getIface());
		} catch (ClassNotFoundException e) {
			
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return objectClass;
	}


	@Override
	public boolean isSingleton() {
		return true;
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
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
	
	
	

}
