package douglea.test6;

public class TimeClient {
	
	public static void main(String[] args) {
		int port = 8001;
		if(args!=null && args.length>0) {
			try {
				port = Integer.valueOf(args[0]);
				
			} catch(NumberFormatException e) {
				
			}
		}
		
		new Thread (new TimeClientHandler("127.0.0.1", port), "TimeClient-001").start();
	}
}
