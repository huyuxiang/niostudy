package daily.y2016.m07.d29.rpc.server;

import daily.y2016.m07.d29.rpc.common.Result;
import daily.y2016.m07.d29.rpc.serialize.SerializeException;

public class Server {

	private final int port;
	
	private static final byte[] failure;
	
	static {
		failure = SerializeException.serialize(Result.fail(null, null));
	}
	
	private final List<BlockingDeque<Entry<Channel, byte[]>>> deques;
	
	private volatile boolean isStart;
	
	private MethodSupport methodSupport;
	
	private final int threadCount;
	
	private ChannelFactory channelFactory;
	
	private ServerBootstrap serverBootstrap;
	
	private ExecutorService executorService;
	
	private AtomicLong pos;
	
	public Server(int port, int threadCount) {
		this.port = port;
		this.threadCount = threadCount;
		
		isStart = false;
		methodSupport = new MethodSupport();
		deques = new ArrayList<BlockingDeque<Entry<Channel, byte[]>>>(8);
		for(int index=0;index<8;++index) {
			deques.add(new LinkedBlockingDeque<Entry<Channel, byte[]>>());
		}
		pos = new AtomicLong(0);
	}
	
	public synchronized void start() {
		if(!isStart) {
			isStart = true;
			
			channelFactory = new NioServerSocketChannelFactory(
					Executors.newFixedThreadPool(2), 
					Executors.newFidxedThreadPool(9), 8);
			serverBootstrap = new ServerBootstrap(channelFactory);
			serverBootstrap.setPipelineFactory(new ChannelPipelineFactory() {
				
				@Override
				public ChannelPipeline getPipeline() throws Exception {
					return Channels.pipeline(new Decoder(), 
							new ParametersHandler(Server.this), new Encoder());
				}
			});
			serverBootstrap.setOption("child.keepAlive", true);
			serverBootstrap.bind(new InetSocketAddress(port));
			
			executorService = Executors.newFixedThreadPool(threadCount);
			for(int count=0;count<threadCount;++count) {
				final int seed = count%8;
				executorService.execute(new Runnable() {
					@Override
					public void run() {
						Server.this.execute(seed);
					}
				});
			}
		} else {
			throw new ServerStartException();
		}
	}
	
	public synchronized void stop() {
		if(isStart) {
			isStart = false;
		}
	}
	
	public void execute(int seed) {
		while(isStart) {
			Class<?>[] clazzArray = new Class<?>[1];
			
			Entry<Channel, byte[]> entry = null;
			try {
				entry = deques.get(seed%8).takeFirst();
			} catch(InterruptedException e) {
				continue;
			}
			
			try {
				Parameters parameters = Serializer.deserialize(entry.getColumn(), 
						Parameters.class);
				Object result = methodSupport.invoke(
						parameters.getInterfaceName(),
						parameters.getParameterTypes().toArray(clazzArray), 
						parameters.getParameters().toArray());
				entry.getRow().write(Serializer.serialize(Result.success(result, null)));
			} catch(Exception e) {
				entry.getRow().write(failure);
			}
		}
	}
	
	public void putParameters(Channel channel, byte[] parameters) {
		if(isStart) {
			deques.get((int)(pos.getAndIncrement()%8)).offerLast(Entry.<Channel, byte[]>
			getEntry(channel, parameters));
		} else {
			throw new ServerStopException();
		}
	}
	
	public void register(String className, Object impl) {
		methodSupport.register(className, impl);
	}
}
