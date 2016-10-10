package daily.y2016.m08.d18.netty.example.securechat;

import javax.net.ssl.SSLEngine;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.handler.ssl.SslHandler;



import static org.jboss.netty.channel.Channels.*;

public class SecureChatServerPipelineFactory implements ChannelPipelineFactory {

	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = pipeline();
		
		SSLEngine engine =
				SecureChatSslContextFactory.getServerContext().createSSLEngine();
	        engine.setUseClientMode(false);
	        
	   pipeline.addLast("ssl", new SslHandler(engine));
	   
	   //on top of the ssl handler , add the text line codec
	   pipeline.addLast("framer", new DelimiterBasedFrameDecoder(
			   8192, Delimiters.lineDelimiter()));
	   pipeline.addLast("decoder", new StringDecoder());
	   pipeline.addLast("encoder", new StringEncoder());
	   
	   pipeline.addLast("handler", new SecureChatServerHandler());
	   
	   return pipeline;
	   
	}
}
