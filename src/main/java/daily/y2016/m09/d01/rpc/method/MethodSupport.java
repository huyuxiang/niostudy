package daily.y2016.m09.d01.rpc.method;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import net.sf.cglib.reflect.FastClass;

public class MethodSupport {

	private static class FastClassWrapper {
		
		public final FastClass fastClass;
		
		public final Object obj;
		
		public FastClassWrapper(FastClass fastClass, Object obj) {
			this.fastClass = fastClass;
			this.obj = obj;
		}
	}
	
	private final ConcurrentMap<String, FastClassWrapper> fastClassMap = 
			new ConcurrentHashMap<String, FastClassWrapper>();
	
	public Object invoke(String methodName, Class<?>[] types, 
			Object[] parameters) throws InvocationTargetException {
		FastClassWrapper wrapper = fastClassMap.get(methodName.substring(0, methodName.lastIndexOf(".")));
		
		if(wrapper==null) {
			throw new RuntimeException(methodName + "not register");
		}
		return wrapper.fastClass.invoke(
				methodName.substring(methodName.lastIndexOf(".") + 1,
						methodName.length()), types, wrapper.obj, parameters);
	}
	
	public void register(String className, Object impl) {
		FastClass clazz = FastClass.create(impl.getClass());
		FastClassWrapper wrapper = new FastClassWrapper(clazz, impl);
		fastClassMap.putIfAbsent(className, wrapper);
	}
}
