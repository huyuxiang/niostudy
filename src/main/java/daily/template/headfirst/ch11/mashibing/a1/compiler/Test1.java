package daily.template.headfirst.ch11.mashibing.a1.compiler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;

import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import daily.template.headfirst.ch11.mashibing.a1.Moveable;
import daily.template.headfirst.ch11.mashibing.a1.Tank;

public class Test1 {
	public static void main(String args[]) throws IOException, ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		String rt = "\r\n";
		String src = 
				"package daily.template.headfirst.ch11.mashibing.a1;"+ rt + 
				"public class TankTimeProxy implements Moveable { "+rt + 
				"    Moveable t;"+rt + 
				"    public TankTimeProxy(Moveable t) {"+rt + 
				"		super();"+rt + 
				"		this.t = t;"+rt + 
				"	 }"+rt + 
				"	 @Override"+rt + 
				"	 public void move() {"+rt + 
				"		 long start = System.currentTimeMillis();"+rt + 
				"		 System.out.println(\"starttime:\" + start);"+rt + 
				"		 t.move();"+rt + 
				"		 long end = System.currentTimeMillis();"+rt + 
				"		 System.out.println(\"time:\" + (end - start));"+rt + 
				"	 }"+rt + 
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
		URL[] urls = new URL[] {new URL("file:/" + System.getProperty("user.dir") + "/src")};
		URLClassLoader ul = new URLClassLoader(urls);
		System.out.println(ul.getParent());
		System.out.println(ul.getParent().getParent());
		System.out.println(ul.getParent().getParent().getParent());
		
		Class c = ul.loadClass("daily.template.headfirst.ch11.mashibing.a1.TankTimeProxy");
		System.out.println(c);
		
		Constructor ctr = c.getConstructor(Moveable.class);
		Moveable m = (Moveable)ctr.newInstance(new Tank());
		m.move();
		
	}
}
