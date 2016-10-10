package daily.y2016.m08.d18.netty.example.factorial;

import java.math.BigInteger;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

public class NumberEncoder extends OneToOneEncoder {

	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {
		if(!(msg instanceof Number)) {
			//Ignore what this encoder can't encode.
			return msg;
		}
		
		// Convert to a BigInteger first for easier implementation.
		BigInteger v;
		if(msg instanceof BigInteger) {
			v = (BigInteger)msg;
		} else {
			v = new BigInteger(String.valueOf(msg));
		}
		
		//Convert the number into a byte array.
		byte[] data = v.toByteArray();
		int dataLength = data.length;
		
		//Construct a message
		ChannelBuffer buf = ChannelBuffers.dynamicBuffer();
		buf.writeByte((byte)'F'); //magic number
		buf.writeInt(dataLength);	//data length
		buf.writeBytes(data);		//data
		
		//return the constructed message.
		return buf;
	}
}
