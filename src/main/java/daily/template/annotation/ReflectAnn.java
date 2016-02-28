package daily.template.annotation;

import java.lang.reflect.Method;

public class ReflectAnn {
	
	public static void main(String args[]) {
		//1.使用加载器加载类
		try {
			Class c = Class.forName("daily.template.annotation.Child");
			
			//2.找到类上面的注解
			boolean isExist = c.isAnnotationPresent(Description.class);
			if(isExist) {
				//3.拿到注解实例
				Description d = (Description) c.getAnnotation(Description.class);
				System.out.println(d.desc());
				System.out.println(d);
			}
			
			//4.找到方法上的注解
			Method[] m = c.getMethods();
			for(Method method: m) {
				boolean isMExist = method.isAnnotationPresent(Description.class);
				if(isMExist) {
					Description d = method.getAnnotation(Description.class);
					System.out.println(d.desc());
				}
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
