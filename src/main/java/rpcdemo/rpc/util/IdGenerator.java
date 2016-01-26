<<<<<<< HEAD
package rpcdemo.rpc.util;

import java.util.concurrent.atomic.AtomicLong;

public class IdGenerator {
	private static final AtomicLong prefixGenerator = 
			new AtomicLong();
	
	public static String getId() {
		return TimeUtil.currentTime() + "_" + 
				prefixGenerator.incrementAndGet();
	}
}
=======
package rpcdemo.rpc.util;

import java.util.concurrent.atomic.AtomicLong;

public class IdGenerator {
	private static final AtomicLong prefixGenerator = 
			new AtomicLong();
	
	public static String getId() {
		return TimeUtil.currentTime() + "_" + 
				prefixGenerator.incrementAndGet();
	}
}
>>>>>>> origin/master
