package daily.y2016.m4.d14.rpc.mayou.network.handler;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import daily.y2016.m4.d14.rpc.mayou.server.Server;

public class ParametersHandler extends SimpleChannelUpstreamHandler {

	private final Server server;
	
	public ParametersHandler(Server server) {
		this.server = server;
	}
	
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		byte[] paramters = (byte[]) e.getMessage();
		server.putParameters(e.getChannel(), paramters);
	}
}
