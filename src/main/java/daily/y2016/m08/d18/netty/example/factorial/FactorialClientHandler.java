package daily.y2016.m08.d18.netty.example.factorial;

import java.math.BigInteger;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

public class FactorialClientHandler extends SimpleChannelUpstreamHandler {

	private static final Logger logger = Logger.getLogger(
			FactorialClientHandler.class.getName());
	
	//stateful properties
	private int i=1;
	private int receivedMessages = 0;
	private final int count;
	final BlockingQueue<BigInteger> answer = new LinkedBlockingQueue<BigInteger>();
	
	public FactorialClientHandler(int count) {
		this.count = count;
	}
	
	public BigInteger getFactorial() {
		boolean interrupted = false;
		for(;;) {
			try {
				BigInteger factorial = answer.take();
				if(interrupted) {
					Thread.currentThread().interrupted();
				}
				return factorial;
			} catch(InterruptedException e) {
				interrupted = true;
			}
		}
	}
	
	@Override
	public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
		if(e instanceof ChannelStateEvent) {
			logger.info(e.toString());
		}
		super.handleUpstream(ctx, e);
	}
	
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		sendNumbers(e);
	}
	
	@Override
	public void channelInterestChanged(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		sendNumbers(e);
	}
	
	@Override
	public void messageReceived(
			ChannelHandlerContext ctx, final MessageEvent e) {
		receivedMessages++;
		if(receivedMessages==count) {
			e.getChannel().close().addListener(new ChannelFutureListener() {
				public void operationComplete(ChannelFuture future) {
					boolean offered = answer.offer((BigInteger)e.getMessage());
					assert offered;
				}
			});
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		logger.log(Level.WARNING, "Unexpected exception from downstream", e.getCause());
		e.getChannel().close();
	}
	
	private void sendNumbers(ChannelStateEvent e) {
		Channel channel = e.getChannel();
		while(channel.isWritable()) {
			if(i<=count) {
				channel.write(Integer.valueOf(i));
				i++;
			} else {
				break;
			}
		}
	}
}
