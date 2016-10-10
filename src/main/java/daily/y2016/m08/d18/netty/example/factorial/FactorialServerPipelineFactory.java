package daily.y2016.m08.d18.netty.example.factorial;

import static org.jboss.netty.channel.Channels.*;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.handler.codec.compression.ZlibDecoder;
import org.jboss.netty.handler.codec.compression.ZlibEncoder;
import org.jboss.netty.handler.codec.compression.ZlibWrapper;

public class FactorialServerPipelineFactory implements ChannelPipelineFactory {

	public ChannelPipeline getPipeline()throws Exception {
		ChannelPipeline pipeline = pipeline();
		
		//Enable stream compression(you can remove these two if unnecessary)
		pipeline.addLast("deflater", new ZlibEncoder(ZlibWrapper.GZIP));
		pipeline.addLast("inflater", new ZlibDecoder(ZlibWrapper.GZIP));
		
		//Add the number codec first,
		pipeline.addLast("decoder", new BigIntegerDecoder());
		pipeline.addLast("encoder", new NumberEncoder());
		
		//and then business logic.
		//Please note we create a handler for every new channel
		//because it has stateful properties
		pipeline.addLast("handler", new FactorialServerHandler());
		
		return pipeline;
	}
}
