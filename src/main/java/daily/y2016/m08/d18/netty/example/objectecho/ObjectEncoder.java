package daily.y2016.m08.d18.netty.example.objectecho;

import java.io.ObjectOutputStream;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferOutputStream;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

import static org.jboss.netty.buffer.ChannelBuffers.*;

public class ObjectEncoder extends OneToOneEncoder {

	private static final byte[] LENGTH_PLACEHOLDER = new byte[4];
	
	private final int estimatedLength;
	
	public ObjectEncoder() {
		this(512);
	}
	
	public ObjectEncoder(int estimatedLength) {
		if(estimatedLength<0) {
			throw new IllegalArgumentException (
					"estimatedLength: "+ estimatedLength);
		}
		this.estimatedLength = estimatedLength;
	}
	
	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {
		ChannelBufferOutputStream bout = 
				new ChannelBufferOutputStream(dynamicBuffer(
						estimatedLength, ctx.getChannel().getConfig().getBufferFactory()));
		
		bout.write(LENGTH_PLACEHOLDER);
		ObjectOutputStream oout = new CompactObjectOutputStream(bout);
		oout.writeObject(msg);
		oout.flush();
		oout.close();
		
		ChannelBuffer encoded = bout.buffer();
		encoded.setInt(0, encoded.writerIndex() -4);
		return encoded;
	}
}
