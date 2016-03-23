package daily.template.headfirst.ch11.mashibing.a1.test;

import daily.template.headfirst.ch11.mashibing.a1.Moveable;
import daily.template.headfirst.ch11.mashibing.a1.Proxy;

public class Client {
public static void main(String[] args) throws Exception {
	UserMgr mgr = new UserMgrImpl();
	TransactionHandler h = new TransactionHandler(mgr);
	
	UserMgr m = (UserMgr)Proxy.newProxyInstance(UserMgr.class, h);
	m.addUser();
	
}
}
