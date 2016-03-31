package daily.template.rpc.network.handler;

import daily.template.rpc.server.Server;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

public class ParametersHandler extends SimpleChannelUpstreamHandler {

	private final Server server;

	public ParametersHandler(Server server) {
		this.server = server;
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		byte[] parameters = (byte[])e.getMessage();
		server.putParameters(e.getChannel(), parameters);
	}

}
