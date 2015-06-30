package Netty_Guide.chapter05.src01;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class EchoClientHandler extends ChannelHandlerAdapter {
	
	private int counter;
	static final String ECHO_REQ = "Hi, Lilingfeng.Welcome to Netty.$_";
	
	public EchoClientHandler() {
		
	}
	
	public void channelActive(ChannelHandlerContext ctx) {
		for(int i=0;i<10;i++) {
			ctx.writeAndFlush(Unpooled.copiedBuffer(ECHO_REQ.getBytes()));
		}
	}
	
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("This is " + ++counter + " times receive server : ["  + msg + "]" );
	}
	
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}
	
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
}
