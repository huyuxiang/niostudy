package daily.template.headfirst.ch11;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


public class MyInvocationHandler implements InvocationHandler {

	Object target;
	
	public MyInvocationHandler(Object target) {
		super();
		this.target = target;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		return method.invoke(target, args);
	}

	public UserService getProxy() {
		return (UserService)Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[] {UserService.class},  this);
	}

}
