package daily.y2016.m08.d18.netty.example.discard;

import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

public class DiscardServerHandler extends SimpleChannelUpstreamHandler {

	private static final Logger logger = Logger.getLogger(DiscardServerHandler.class.getName());
	
	private final AtomicLong transferredBytes = new AtomicLong();
	
	public long getTransferredBytes() {
		return transferredBytes.get();
	}
	
	@Override
	public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
		if(e instanceof ChannelStateEvent) {
			logger.info(e.toString());
		}
		
		super.handleUpstream(ctx, e);
	}
	
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		transferredBytes.addAndGet(((ChannelBuffer)e.getMessage()).readableBytes());
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		logger.log(Level.WARNING, "Unexpected exception from dowstream.", e.getCause());
		e.getChannel().close();
	}
}
