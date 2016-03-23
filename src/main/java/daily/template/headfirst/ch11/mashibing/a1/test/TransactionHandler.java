package daily.template.headfirst.ch11.mashibing.a1.test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import daily.template.headfirst.ch11.mashibing.a1.InvocationHandler;

public class TransactionHandler implements InvocationHandler {

	private Object target;
	
	public TransactionHandler(Object target) {
		super();
		this.target = target;
	}

	@Override
	public void invoke(Object o, Method m) {
		System.out.println("Transaction Start:");
		try {
			m.invoke(target, new Object[]{});
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		System.out.println("Transaction Commit");		
	}

}
