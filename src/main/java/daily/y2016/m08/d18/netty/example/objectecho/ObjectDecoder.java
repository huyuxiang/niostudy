package daily.y2016.m08.d18.netty.example.objectecho;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferInputStream;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.LengthFieldBasedFrameDecoder;
import org.jboss.netty.handler.codec.serialization.ClassResolver;

public class ObjectDecoder extends LengthFieldBasedFrameDecoder {

	private final ClassResolver classResolver;
	
	public ObjectDecoder() {
		this(1048576);
	}
	
	public ObjectDecoder(ClassResolver classResolver) {
		this(1048576, classResolver);
	}
	
	public ObjectDecoder(int maxObjectSize) {
		this(maxObjectSize, ClassResolvers.weakCachingResolver(null));
	}
	
	public ObjectDecoder(int maxObjectSize, ClassResolver classResolver) {
		super(maxObjectSize, 0, 4, 0, 4);
		this.classResolver = classResolver;
	}
	
	public ObjectDecoder(int maxObjectSize, ClassLoader classLoader) {
		this(maxObjectSize, ClassResolvers.weakCachingResolver(classLoader));
	}
	
	@Override
	protected Object decode(
			ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
		
		ChannelBuffer frame = (ChannelBuffer) super.decode(ctx, channel, buffer);
		if(frame ==null) {
			return null;
		}
		
		return new CompactObjectInputStream(
				new ChannelBufferInputStream(frame), classResolver).readObject();
	}
	
	@Override
	protected ChannelBuffer extractFrame(ChannelBuffer buffer, int index, int length) {
		return buffer.slice(index, length);
	}
	
}
