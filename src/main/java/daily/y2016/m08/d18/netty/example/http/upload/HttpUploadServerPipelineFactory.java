package daily.y2016.m08.d18.netty.example.http.upload;

import static org.jboss.netty.channel.Channels.*;

public class HttpUploadServerPipelineFactory implements ChannelPipelineFactory {

	public ChannelPipeline getPipeline() throws Exception {
		
		//create a default pipeline implementation.
		ChannelPipeline pipeline = pipeline();
		
		if(HttpUploadServer.isSSL) {
			SSLEngine engine = SecureChatSslContextFactory.getServerContext().createSSLEngine();
			engine.setUseClientMode(false);
			SslHandler handler = new SslHandler(engine);
			handler.setIssueHandsshake(true);
			pipeline.addLast("ssl", handler);
		}
		
		pipeline.addLast("decoder", new HttpRequestDecoder());
		
		pipeline.addLast("encoder", new HttpResponseEncoder());
		
		pipeline.addLast("deflater", new HttpContentCompressor());
		
		pipeline.addLast("handler", new HttpUploadServerHandler());
		return pipeline;
	}
}
