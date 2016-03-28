package daily.template.headfirst.ch01;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import daily.template.headfirst.ch11.a2.CountService;
import daily.template.headfirst.ch11.a2.CountServiceImpl;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class DynamicProxyPerformanceTest {

	public static void main(String args[]) throws Exception {
		CountService delegate = new CountServiceImpl();
		
		long time = System.currentTimeMillis();
		CountService jdkProxy = createJkdDynamicProxy(delegate);
		time = System.currentTimeMillis() - time;
		System.out.println("Create JDK Proxy: " + time + " ms");
		
		time = System.currentTimeMillis();
		CountService cglibProxy = createCglibDynamicProxy(delegate);
		time = System.currentTimeMillis() - time;
		System.out.println("Create CGLIB Proxy:" + time + " ms");
		
		time = System.currentTimeMillis();
		CountService javassistProxy = createJavassistDynamicProxy(delegate);
		time = System.currentTimeMillis() - time;
		System.out.println("Create JAVASSIST Proxy: " + time + " ms");
		
		time = System.currentTimeMillis();
		CountService javassistBytecodeProxy = createJavassistBytecodeDynamicProxy(delegate);
		time = System.currentTimeMillis() - time;
		System.out.println("Create JAVASSIST Bytecode Proxy:" + time + " ms");
		
		time = System.currentTimeMillis();
		CountService asmBytecodeProxy = createAsmBytecodeDynamicProxy(delegate);
		time = System.currentTimeMillis() - time;
		System.out.println("Create ASM Proxy: " + time + " ms");
		System.out.println("=========");
		
		CountService reflectProxy = new ReflectProxy(delegate);
		for(int i=0;i<3;i++) {
			test(reflectProxy, "Run reflectProxy Proxy:");
			test(jdkProxy, "Run JDK Proxy: ");
			test(cglibProxy, "Run CGLIB Proxy: ");  
            test(javassistProxy, "Run JAVAASSIST Proxy: ");  
            test(javassistBytecodeProxy, "Run JAVAASSIST Bytecode Proxy: ");  
            test(asmBytecodeProxy, "Run ASM Bytecode Proxy: ");  
            System.out.println("----------------"); 
		}
	}
	
	private static CountService createJdkDynamicProxy(final CountService delegate) {
		CountService jdkProxy = (CountService) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(),
				new Class[]{CountService.class}, new JdkHandler(delegate));
		return jdkProxy;
	}
	
	private static class JdkHandler implements InvocationHandler {
		final Object delegate;
		
		JdkHandler(Object delegate) {
			this.delegate = delegate;
		}
		
		public Object invoke(Object object, Method method, Object[] args) throws Throwable {
			return method.invoke(delegate, args);
		}
	}
	
	private static CountService createCglibDynamicProxy(final CountService delegate) {
		Enhancer enhancer = new Enhancer();
		enhancer.setCallback(new CglibInterceptor(delegate));
		enhancer.setInterfaces(new Class[] {CountService.class});
		CountService cglibProxy = (CountService)enhancer.create();
		return cglibProxy;
	}
	
	private static class CglibInterceptor implements MethodInterceptor {
		final Object delegate;
		
		CglibInterceptor(Object delegate) {
			this.delegate = delegate;
		}
		
		public Object intercept(Object object, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
			return methodProxy.invoke(delegate, objects);
		}
	}
	
	private static CountService createJavassistDynamicProxy(final CountService delegate) throws Throwable {
		ProxyFactory proxyFactory = new ProxyFactory();
		proxyFactory.setInterfaces(new Class[]{CountService.class});
		Class<?> proxyClass = proxyFactory.createClass();
		CountService javassistProxy = (CountService)proxyClass.newInstance();
		((ProxyObject)javassistProxy).setHandler(new JavaAssistInterceptor(delegate));
		return javassistProxy;
	}
	
	private static class JavaAssistInterceptor implements MethodHandler {
		Object delegate;
		
		JavaAssistInterceptor(Object delegate) {
			this.delegate = delegate;
		}
		
		public Object invoke(Object self, Method m, Method proceed, Object[] args) throws Throwable {
			return m.invoke(delegate, args);
		}
	}
}
