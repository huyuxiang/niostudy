package daily.template.rpc.dsf.server.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import daily.template.rpc.dsf.client.proxy.DynamicClientHandler;

/**
 * server端动态代理
 * @author ZhongweiLee
 *
 */
public class DynamicServiceHandler implements InvocationHandler {

	private final static transient Logger log = LoggerFactory.getLogger(DynamicClientHandler.class);
	
	private Object target;

	// 绑定委托对象，并返回代理类
	public Object bind(Object target) {

		this.target = target;

		return Proxy.newProxyInstance(target.getClass().getClassLoader(),
				target.getClass().getInterfaces(), this);
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		log.debug("begin invoke ...");
		// 在调用具体函数方法前执行
		Object result = method.invoke(target, args);
		// 在调用具体函数方法后执行
		return result;
	}

}
