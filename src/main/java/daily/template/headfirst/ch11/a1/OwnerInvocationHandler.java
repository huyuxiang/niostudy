package daily.template.headfirst.ch11.a1;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class OwnerInvocationHandler implements InvocationHandler {
	
	PersonBean person;
	
	public OwnerInvocationHandler(PersonBean person) {
		this.person = person;
	}
	
	public Object invoke(Object proxy, Method method, Object[] args) throws IllegalAccessException {
		
		try {
			if(method.getName().startsWith("get")) {
				return method.invoke(person, args);
			} else if(method.getName().equals("setHotOrNotRating")) {
				System.out.println("你没有权限！");   
				throw new IllegalAccessException();
			} else if(method.getName().startsWith("set")) {
				return method.invoke(person, args);
			}
		} catch(InvocationTargetException e) {
			//如果真正主题抛出异常的话，就会执行这里
			e.printStackTrace();
		}
		return null;
	}
	
	static PersonBean getOwnerProxy(PersonBean person) {
		return (PersonBean)Proxy.newProxyInstance(person.getClass().getClassLoader(), person.getClass().getInterfaces(), new OwnerInvocationHandler(person));
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
		System.out.println("person.getName() cost :" + (t2 -t1));
		
		PersonBean personProxy = getOwnerProxy(person);
		
		long t3 = System.currentTimeMillis();
		for(int i=0;i<=TOTAL_CALL;i++) {
			personProxy.getName();
		}
		long t4 = System.currentTimeMillis();
		System.out.println("person.getName() cost :" + (t4 -t3));
	}
}
