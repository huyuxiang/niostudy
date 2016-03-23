package daily.template.headfirst.ch11.mashibing.a1.compiler;

import java.lang.reflect.Method;

import daily.template.headfirst.ch11.mashibing.a1.Moveable;

public class Test2 {
	public static void main(String[] args) {
		Method[] methods = Moveable.class.getMethods();
		for(Method m : methods) {
			System.out.println(m.getName());
			System.out.println(m);
		}
	}
}
