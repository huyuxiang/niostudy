package daily.y2016.m08.d18.netty.example.factorial;

import java.math.BigInteger;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.CorruptedFrameException;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

public class BigIntegerDecoder extends FrameDecoder {

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
		//wait until the length prefix is available
		
		if(buffer.readableBytes()<5) {
			return null;
		}
		buffer.markReaderIndex();
		
		//check the magic number.
		int magicNumber = buffer.readUnsignedByte();
		if(magicNumber!='F') {
			buffer.resetReaderIndex();
			throw new CorruptedFrameException (
					"Invalid magic number:" + magicNumber);
		}
		
		//wait until the whole data is available.
		int dataLength = buffer.readInt();
		if(buffer.readableBytes()<dataLength) {
			buffer.resetReaderIndex();
			return null;
		}
		
		//Convert the received data into a new BigInteger.
		byte[] decoded = new byte[dataLength];
		buffer.readBytes(decoded);
		
		return new BigInteger(decoded);
	}
}
