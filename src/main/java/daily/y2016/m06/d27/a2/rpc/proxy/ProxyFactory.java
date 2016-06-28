package daily.y2016.m06.d27.a2.rpc.proxy;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import net.sf.cglib.proxy.NoOp;

public class ProxyFactory {

	private static final ConcurrentHashMap<Class<?>, Object> clazzMap = 
			new ConcurrentHashMap<Class<?>, Object>();
	
	public static Object getConsumerProxy(Class<?> clazz, String ip) {
		Object obj = clazzMap.get(clazz);
		if(obj==null) {
			synchronized(clazz) {
				if(clazzMap.contains(clazz)) {
					return clazzMap.get(clazz);
				} else {
					Enhancer enhancer = new Enhancer();
					enhancer.setSuperclass(BaseConsumerProxy.class);
					enhancer.setInterfaces(new Class[]{clazz});
					enhancer.setCallbacks(new Callback[] {
							
							new MethodInterceptor() {
								
								@Override
								public Object intercept(Object object,
										Method method, Object[] objects, 
										MethodProxy methodProxy) throws Throwable {
									
									return ((BaseConsumerProxy)object).doInterval(
											method.getDeclaringClass().getName() + "." + method.getName(), 
											objects);
								}
							}, NoOp.INSTANCE});
					enhancer.setCallbackFilter(new CallbackFilter() {
						
						@Override
						public int accept(Method method) {
							if(method.getName().equals("doInterval")) {
								return 1;
							} else {
								return 0;
							}
						}
					});
					obj = enhancer.create(new Class[] {String.class}, 
							new Object[] {ip});
					clazzMap.put(clazz, obj);
					return obj;
				
				}
			}
		} else {
			return obj;
		}
	}
	
}
