package daily.template.rpc.dsf.common;

import java.util.ArrayList;
import java.util.List;


/**
 * 服务器节点
 * @author ZhongweiLee
 *
 */
public class ServerNode {
	
	/**IP*/
	private String ip;
	/**端口号*/
	private int port;
	
	public ServerNode(String ip, int port) {
		super();
		this.ip = ip;
		this.port = port;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	/**
	 * 生成服务地址 ip:port
	 * */
    public String genAddress() {
        return ip + ":" + port;
    }
	
	public static ServerNode transfer(String address) {
		String[] hostname = address.split(":");
		String ip = hostname[0];
		Integer port = Integer.valueOf(hostname[1]);

		return new ServerNode(ip, port);
	}

}
