package daily.y2016.m07.d6.rpc.network.handler;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import daily.y2016.m07.d6.rpc.server.Server;


public class ParameterHandler extends SimpleChannelUpstreamHandler{

	private final Server server;
	
	public ParameterHandler(Server server) {
		this.server = server;
	}
	
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		
		byte[] parameters = (byte[])e.getMessage();
		server.putParameters(e.getChannel(), parameters);
	}
}
