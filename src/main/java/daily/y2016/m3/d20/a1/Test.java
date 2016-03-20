package daily.y2016.m3.d20.a1;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Properties;

public class Test implements T {
	public static void main(String[] args) throws Exception {

		/*Field field = System.class.getDeclaredField("props");
		field.setAccessible(true);
		Properties props = (Properties) field.get(null);
		props.put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
*/
		System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles","true"); // 通过此语句得到动态生成 的class 文件，然后通过 反编译工具得到代码   
		Test test = new Test();

		test1(test);
		test2(test);
		test3(test);

	}

	public static void test1(T t) throws Exception {
		Method method = Test.class.getMethod("hello");
		method.invoke(t);
	}

	public static void test2(T t) {
		t.hello();
	}

	public static void test3(T t) throws Exception {
		createJdkDynamicProxy(t).hello(); // warm up
	}

	public static T createJdkDynamicProxy(final T delegate) {
		T jdkProxy = (T) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[] { T.class },
				new JdkHandler(delegate));
		return jdkProxy;
	}

	public static class JdkHandler implements InvocationHandler {

		final Object delegate;

		JdkHandler(Object delegate) {
			this.delegate = delegate;
		}

		public Object invoke(Object object, Method method, Object[] objects) throws Throwable {
			return method.invoke(delegate, objects);
		}
	}

	public void hello() {
		System.out.println("aa");
	}
}
