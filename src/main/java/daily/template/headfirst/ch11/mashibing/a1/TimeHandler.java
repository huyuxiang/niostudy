package daily.template.headfirst.ch11.mashibing.a1;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TimeHandler implements InvocationHandler {

	private Object target;
	
	public Object getO() {
		return target;
	}

	public void setO(Object o) {
		this.target = o;
	}

	@Override
	public void invoke(Object o, Method m)  {
		long start = System.currentTimeMillis();
		System.out.println("starttime:" + start);
		try {
			m.invoke(target, new Object[]{});
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		System.out.println("time:" + (end - start));
	}
}
