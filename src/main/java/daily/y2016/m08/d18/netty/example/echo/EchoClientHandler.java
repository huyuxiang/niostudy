package daily.y2016.m08.d18.netty.example.echo;

import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

public class EchoClientHandler extends SimpleChannelUpstreamHandler {

	private static final Logger logger = Logger.getLogger(
			EchoClientHandler.class.getName());
	
	private final ChannelBuffer firstMessage;
	private final AtomicLong transferredBytes = new AtomicLong();
	
	public EchoClientHandler(int firstMessageSize) {
		if(firstMessageSize<=0) {
			throw new IllegalArgumentException (
					"firstMessageSize:" + firstMessageSize);
		}
		firstMessage = ChannelBuffers.buffer(firstMessageSize);
		for(int i=0;i<firstMessage.capacity();i++) {
			firstMessage.writeByte((byte)i);
		}
		
	}
	
	public long getTransferredBytes() {
		return transferredBytes.get();
	}
	
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		e.getChannel().write(firstMessage);
	}
	
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		transferredBytes.addAndGet(((ChannelBuffer)e.getMessage()).readableBytes());
		e.getChannel().write(e.getMessage());
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		logger.log(
				Level.WARNING, "Unexpected exception from downstream.", e.getCause());
		e.getChannel().close();
	}
	
	
}
