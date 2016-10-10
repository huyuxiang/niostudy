package daily.template.rpc.mayou.server;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicLong;

import daily.template.rpc.mayou.common.Entry;
import daily.template.rpc.mayou.common.Parameters;
import daily.template.rpc.mayou.common.Result;
import daily.template.rpc.mayou.method.MethodSupport;
import daily.template.rpc.mayou.network.handler.Decoder;
import daily.template.rpc.mayou.network.handler.Encoder;
import daily.template.rpc.mayou.network.handler.ParametersHandler;
import daily.template.rpc.mayou.serialize.Serializer;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

public class Server {

	private final int port;

	private static final byte[] failure;

	static {
		failure = Serializer.serialize(Result.fail(null, null));
	}

	private final List<BlockingDeque<Entry<Channel, byte[]>>> deques;

	private volatile boolean isStart;

	private MethodSupport methodSupport;

	private final int threadCount;

	private ChannelFactory channelFactory;

	private ServerBootstrap serverBootstrap;

	private ExecutorService executorService;

	private AtomicLong pos;

	@SuppressWarnings("unchecked")
	public Server(int port, int threadCount) {
		this.port = port;
		this.threadCount = threadCount;
		isStart = false;
		methodSupport = new MethodSupport();
		deques = new ArrayList<BlockingDeque<Entry<Channel, byte[]>>>(8);
		for (int index = 0; index < 8; ++index) {
			deques.add(new LinkedBlockingDeque<Entry<Channel, byte[]>>());
		}
		pos = new AtomicLong(0);
	}

	public synchronized void start() {
		if (!isStart) {
			isStart = true;

			channelFactory = new NioServerSocketChannelFactory(
					Executors.newFixedThreadPool(1),
					Executors.newFixedThreadPool(8), 8);
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
			for (int count = 0; count < threadCount; ++count) {
				final int seed = count % 8;
				executorService.execute(new Runnable() {

					@Override
					public void run() {
						Server.this.execute(seed);
					}

				});
			}
		} else {
			throw new RuntimeException("server started");
		}
	}

	public synchronized void stop() {
		if (isStart) {
			isStart = false;
		}
	}

	public void execute(int seed) {
		while (isStart) {
			Class<?>[] clazzArray = new Class<?>[1];

			Entry<Channel, byte[]> entry = null;
			try {
				entry = deques.get(seed % 8).takeFirst();
			} catch (InterruptedException e) {
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
			} catch (Exception e) {
				entry.getRow().write(failure);
			}
		}
	}

	public void putParameters(Channel channel, byte[] parameters) {
		if (isStart) {
			deques.get((int) (pos.getAndIncrement() % 8)).offerLast(Entry.<Channel, byte[]> 
			getEntry(channel, parameters));
		} else {
			throw new RuntimeException("server stoped");
		}
	}

	public void register(String className, Object impl) {
		methodSupport.register(className, impl);
	}
}
