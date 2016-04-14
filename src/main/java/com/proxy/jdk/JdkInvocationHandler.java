package com.proxy.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.proxy.CountService;
import com.proxy.CountServiceImpl;

public class JdkInvocationHandler implements InvocationHandler {
	
	Object target;
	
	public JdkInvocationHandler(Object target) {
		this.target = target;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		System.out.println("call proxy");
		return method.invoke(target, args);
	}
	
	public static CountService getProxy(InvocationHandler handler) {
		return (CountService) Proxy.newProxyInstance(JdkInvocationHandler.class.getClassLoader(), new Class[]{CountService.class}, handler);
	}
	
	public static void main(String[] args) {
		CountService realObject = new CountServiceImpl();
		InvocationHandler handler = new JdkInvocationHandler(realObject);
		CountService jdkProxy = getProxy(handler);
		
		jdkProxy.count();
	}

}
