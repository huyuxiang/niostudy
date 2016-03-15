package daily.y2016.m3.d15.a2;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import daily.template.headfirst.ch11.a1.PersonBean;
import daily.template.headfirst.ch11.a1.PersonBeanImpl;

public class OwnerInterceptor implements MethodInterceptor, CallbackFilter {
	
	private PersonBean person;
	
	public OwnerInterceptor(PersonBean person) {
		this.person = person;
	}
	
	public Object intercept(Object object, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
		if(method.getName().startsWith("get")) {
			return methodProxy.invokeSuper(object, args);
		} else if(method.getName().equals("setHotOrNotRating")) {
			System.out.println("你没有权限");
			throw new IllegalAccessException();
		} else if(method.getName().startsWith("set")) {
			return methodProxy.invokeSuper(object, args);
		}
		return null;
	}
	
	public static PersonBean getProxyInstance1(PersonBean person) {
		Enhancer en = new Enhancer();
		en.setSuperclass(PersonBeanImpl.class);
		en.setCallback(new OwnerInterceptor(person));
		
		return (PersonBean)en.create();
	}
	
	public static PersonBean getProxyInstance2(PersonBean person) {
		Enhancer en = new Enhancer();
		
		en.setSuperclass(PersonBeanImpl.class);
		Object proxy = new OwnerInterceptor(person);
		en.setCallbacks(new Callback[] {(MethodInterceptor)proxy});
		en.setCallbackFilter((CallbackFilter)proxy);
		
		return (PersonBean)en.create();
	}
	
	public int accept(Method method) {
		//if(!"query".equalsIgnoreCase(method.getName()))    
		return 0 ;
	}
	
	final static int TOTAL_CALL = 10000000;
	
	public static void main(String args[]) {
		PersonBean person = new PersonBeanImpl();
		person.setName("joe");
		
		long t1 = System.currentTimeMillis();
		for(int i=0;i<=TOTAL_CALL;i++) {
			person.getName();
		}
		long t2 = System.currentTimeMillis();
		System.out.println("person.getName() cost: 	" + (t2 -t1));
		
		PersonBean personProxy = getProxyInstance1(person);
		
		long t3 = System.currentTimeMillis();
		for(int i=0;i<=TOTAL_CALL;i++) {
			personProxy.getName();
		}
		long t4 = System.currentTimeMillis();
		System.out.println("person.getName() cost:" + (t4 - t3));
	}
}
