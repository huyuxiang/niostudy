package daily.y2016.m08.d18.netty.example.factorial;

import java.math.BigInteger;
import java.util.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

public class FactorialServerHandler extends SimpleChannelUpstreamHandler {

	private static final Logger logger = Logger.getLogger(
			FactorialServerHandler.class.getName());
	
	//stateful properties.
	private int lastMultiplier = 1;
	private BigInteger factorial = new BigInteger(new byte[] {1});
	
	@Override
	public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
		if(e instanceof ChannelStateEvent) {
			logger.info(e.toString());
		}
		super.handleUpstream(ctx, e);
	}
	
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		//calculate the cumulative factorial and send it to the client
		BigInteger number;
		if(e.getMessage() instanceof BigInteger) {
			number = (BigInteger)e.getMessage();
		} else {
			number = new BigInteger(e.getMessage().toString());
		}
		lastMultiplier = number.intValue();
		factorial = factorial.multiply(number);
		e.getChannel().write(factorial);
	}
	
	@Override
	public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		logger.info(new Formatter().format(
				"Factorial of %,d is: %,d", lastMultiplier, factorial).toString());
	}
	
	@Override
	public void exceptionCaught(
			ChannelHandlerContext ctx, ExceptionEvent e) {
		logger.log(Level.WARNING,
				"Unexpected exception from downStream",
				e.getCause());
		e.getChannel().close();
	}
}
