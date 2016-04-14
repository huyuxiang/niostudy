package daily.template.rpc.dsf.client.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import daily.template.rpc.dsf.client.Invoker;

/**
 * 客户端动态代理
 * @author ZhongweiLee
 * 
 */
public class DynamicClientHandler implements InvocationHandler {

	private final Invoker invoker;

	public DynamicClientHandler(final Invoker invoker) {
		this.invoker = invoker;
	}

	/**
	 * 动态代理绑定实例
	 * 
	 * @param classLoader
	 * @param serviceClass
	 * @return
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public <T> T bind(ClassLoader classLoader, Class<?> serviceClass) throws ClassNotFoundException {
		
		return (T) Proxy.newProxyInstance(classLoader,new Class[] { serviceClass }, this);
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		return invoker.invoke(method, args);
	}

}
