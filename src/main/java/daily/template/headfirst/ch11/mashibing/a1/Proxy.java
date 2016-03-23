package daily.template.headfirst.ch11.mashibing.a1;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

public class Proxy {
	public static Object newProxyInstance(Class infce, InvocationHandler h) throws Exception{ //JDK6 Complier API, CGLib, ASM
		String methodStr = "";
		String rt = "\r\n";
		
		Method[] methods = infce.getMethods();
		/*for(Method m: methods) {
			methodStr +="@Override" + rt + 
						"public void " + m.getName() + "() {" + rt + 
						"		 long start = System.currentTimeMillis();"+rt + 
						"		 System.out.println(\"starttime:\" + start);"+rt + 
						"		 t."+ m.getName()+"();"+rt + 
						"		 long end = System.currentTimeMillis();"+rt + 
						"		 System.out.println(\"time:\" + (end - start));"+rt + 
						"}";
		}*/
		for(Method m: methods) {
			methodStr +="@Override" + rt + 
						"public void " + m.getName() + "() {" + rt +
						" try {"+ rt + 
						" Method md = " + infce.getName() + ".class.getMethod(\"" + m.getName() + "\");"+ rt +
						"h.invoke(this, md);" + rt + 
						" } catch(Exception e) {e.printStackTrace();}"+ rt + 
						"}";
		}
		
		String src = 
				"package daily.template.headfirst.ch11.mashibing.a1;"+ rt + 
				"import java.lang.reflect.*;" + rt + 
				"public class TankTimeProxy implements "+ infce.getName() +" { "+rt + 
				"    daily.template.headfirst.ch11.mashibing.a1.InvocationHandler h;"+rt + 
				"    public TankTimeProxy(daily.template.headfirst.ch11.mashibing.a1.InvocationHandler h) {"+rt + 
				"		super();"+rt + 
				"		this.h = h;"+rt + 
				"	 }"+rt + 
				methodStr + 
				"}";
		String fileName = System.getProperty("user.dir") 
				+ "/src/main/java/daily/template/headfirst/ch11/mashibing/a1/TankTimeProxy.java";
		File f = new File(fileName);
		FileWriter fw = new FileWriter(f);
		fw.write(src);
		fw.flush();
		fw.close();
		
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		StandardJavaFileManager fileMgr = compiler.getStandardFileManager(null, null, null);
		Iterable units = fileMgr.getJavaFileObjects(fileName);
		CompilationTask t = compiler.getTask(null, fileMgr, null, null, null, units);
		t.call();
		fileMgr.close();
		
		//load into memory and create an instance
		URL[] urls = new URL[] {new URL("file:/" + System.getProperty("user.dir") + "/src/")};
		URLClassLoader ul = new URLClassLoader(urls);
		System.out.println(ul.getParent());
		System.out.println(ul.getParent().getParent());
		System.out.println(ul.getParent().getParent().getParent());
		
		Class c = ul.loadClass("daily.template.headfirst.ch11.mashibing.a1.TankTimeProxy");
		System.out.println(c);
		
		Constructor ctr = c.getConstructor(daily.template.headfirst.ch11.mashibing.a1.InvocationHandler.class);
		Object m = ctr.newInstance(h);
		
		return m;
	}
}
