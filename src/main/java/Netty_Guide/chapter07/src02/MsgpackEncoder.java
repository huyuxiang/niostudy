package Netty_Guide.chapter07.src02;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import org.msgpack.MessagePack;

public class MsgpackEncoder extends MessageToByteEncoder<Object> {
	
	protected void encode(ChannelHandlerContext arg0, Object arg1, ByteBuf arg2) throws Exception {
		MessagePack msgpack = new MessagePack();
		
		byte[] raw = msgpack.write(arg1);
		arg2.writeBytes(raw);
	}
}
