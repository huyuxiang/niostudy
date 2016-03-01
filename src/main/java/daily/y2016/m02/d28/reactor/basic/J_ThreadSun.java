package daily.y2016.m02.d28.reactor.basic;

import java.util.concurrent.atomic.AtomicInteger;

public class J_ThreadSun extends Thread{
	public static AtomicInteger m_data= new AtomicInteger();			
	public static int m_times=1000;
	public int m_ID;
	public volatile boolean m_done;
	
	J_ThreadSun(int id){
		m_ID=id;
	}
	
	public void run(){
		m_done=false;
		int d=((m_ID % 2==0) ? 1:-1);
		System.out.println("运行线程:"+m_ID+"(增量为:"+d+")");
		for(int i=0;i<m_times;i++){			
			for(int j=0;j<m_times;j++){
					//m_data+=1;
				m_data.getAndAdd(d);
			}
		}
		m_done=true;
		System.out.println("线程结束:"+m_ID);
	}
	
	public static void main(String[] args){
		J_ThreadSun t1=new J_ThreadSun(1);
		J_ThreadSun t2=new J_ThreadSun(2);
		t1.m_done=false;
		t2.m_done=false;
		t1.start();
		t2.start();
		while(true){
			if(t1.m_done && t2.m_done){//当t1,t2的m_done都为true时跳出循环
				break;
			}
			//如果此处加入代码，可以正常执行
		}
		System.out.println("结果: m_data="+m_data+"");
	}
}
