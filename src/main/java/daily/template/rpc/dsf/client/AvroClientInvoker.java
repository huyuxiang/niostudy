package daily.template.rpc.dsf.client;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;

import org.apache.avro.ipc.NettyTransceiver;
import org.apache.avro.ipc.reflect.ReflectRequestor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iqitoo.dsf.common.RpcException;

/**
 * 客户端Avro协议的Invoker
 * @author ZhongweiLee
 *
 * @param <T>
 */
public class AvroClientInvoker<T> implements Invoker{
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	private final String ip;
	
	private final String iface;
	
	private final int port;
	
	public AvroClientInvoker(final String ip,final String iface,final int port) {
		this.ip = ip ;
		this.iface = iface;
		this.port = port;
	}

	@Override
	public Object invoke(Method method, Object[] args) throws RpcException{
		
		Object client =  null;

		Object result = null;
		try {
			
			NettyTransceiver nettyTransceiver = new NettyTransceiver(new InetSocketAddress(ip,port));
			
			Class<?> ifaceClass = Class.forName(iface);

			client = ReflectRequestor.getClient(ifaceClass, nettyTransceiver);
			
			result = method.invoke(client, args);
			
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new RpcException(RpcException.CONFIG_EXCEPTION,"no find interface ！");
		} catch (IllegalAccessException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
			
		
		return result;
		
	}

}
