package daily.y2016.m06.d17.mayou.network.handler;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.replay.ReplayingDecoder;

public class Decoder extends ReplayingDecoder<DecodeState> {

	private int length;
	
	public Decoder() {
		super(DecodeState.READ_LENGTH);
		length = 0;
	}
	
	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, 
			ChannelBuffer buffer, DecodeState state) throws Exception {
		switch(state) {
		case READ_LENGTH: 
			length = buffer.readInt();
			this.checkpoint(DecodeState.READ_CONTENT);
		case READ_CONTENT:
			byte[] data = buffer.readBytes(length).array();
			length = 0;
			this.checkpoint(DecodeState.READ_LENGTH);
			return data;
			default:
				throw new Exception("读取错误");
		}
	}
}
