package daily.y2016.m07.d21.rpc.common;

import java.util.List;

import org.jboss.netty.channel.Channel;

public class Parameters {

	private String id;
	
	private String interfaceName;
	
	private List<Class<?>> parameterTypes;
	
	private List<?> parameters;
	
	private transient Channel channel;
	
	public String getInterfaceName() {
		return interfaceName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Class<?>> getParameterTypes() {
		return parameterTypes;
	}

	public void setParameterTypes(List<Class<?>> parameterTypes) {
		this.parameterTypes = parameterTypes;
	}

	public List<?> getParameters() {
		return parameters;
	}

	public void setParameters(List<?> parameters) {
		this.parameters = parameters;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}
	
	
}
