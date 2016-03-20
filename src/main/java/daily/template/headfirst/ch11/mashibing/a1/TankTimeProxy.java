package daily.template.headfirst.ch11.mashibing.a1;
import java.lang.reflect.*;
public class TankTimeProxy implements daily.template.headfirst.ch11.mashibing.a1.test.UserMgr { 
    daily.template.headfirst.ch11.mashibing.a1.InvocationHandler h;
    public TankTimeProxy(daily.template.headfirst.ch11.mashibing.a1.InvocationHandler h) {
		super();
		this.h = h;
	 }
@Override
public void addUser() {
 try {
 Method md = daily.template.headfirst.ch11.mashibing.a1.test.UserMgr.class.getMethod("addUser");
h.invoke(this, md);
 } catch(Exception e) {e.printStackTrace();}
}}