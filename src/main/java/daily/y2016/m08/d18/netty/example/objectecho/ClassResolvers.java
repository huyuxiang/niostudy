package daily.y2016.m08.d18.netty.example.objectecho;

import java.lang.ref.Reference;
import java.util.HashMap;

import org.jboss.netty.handler.codec.serialization.ClassResolver;
import org.jboss.netty.handler.codec.serialization.WeakReferenceMap;

public class ClassResolvers {

	public static ClassResolver cacheDisabled(ClassLoader classLoader) {
		return new ClassLoaderResolver(defaultClassLoader(classLoader));
	}
	
	public static ClassResolver weakCachingResolver(ClassLoader classLoader) {
		return new CachingClassResolver(new ClassLoaderClassResolver(defaultClassLoader(classLoader)),
				new WeakReferenceMap<String, Class<?>>(new HashMap<String, Reference<Class<?>>>()));
	}
	
	public static ClassResolver soft
}
