package daily.y2016.m08.d18.netty.example.factorial;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.handler.codec.compression.ZlibDecoder;
import org.jboss.netty.handler.codec.compression.ZlibEncoder;
import org.jboss.netty.handler.codec.compression.ZlibWrapper;

import static org.jboss.netty.channel.Channels.*;

public class FactorialClientPipelineFactory implements ChannelPipelineFactory {

	private final int count;
	
	public FactorialClientPipelineFactory(int count) {
		this.count = count;
	}
	
	public ChannelPipeline getPipeline() throws Exception {
		
		ChannelPipeline pipeline = pipeline();
		
		pipeline.addLast("deflater", new ZlibEncoder(ZlibWrapper.GZIP));
		pipeline.addLast("inflater", new ZlibDecoder(ZlibWrapper.GZIP));
		
		pipeline.addLast("decoder", new BigIntegerDecoder());
		pipeline.addLast("encoder", new NumberEncoder());
		
		pipeline.addLast("handler", new FactorialClientHandler(count));
		return pipeline;
	}
}
