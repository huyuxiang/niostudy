package douglea.test6;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class TimeClientHandler implements Runnable {
	
	private String host;
	private int port ;
	private Selector selector;
	private SocketChannel socketChannel;
	private volatile boolean stop;
	
	public TimeClientHandler(String host, int port) {
		this.host = host==null?"127.0.0.1":host;
		this.port = port;
		try {
			selector = Selector.open();
			socketChannel = SocketChannel.open();
			socketChannel.configureBlocking(false);
		} catch(IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public void run() {
		while(!stop) {
			try {
				selector.select(1000);
				Set<SelectionKey> selectedKeys = selector.selectedKeys();
				Iterator<SelectionKey> it = selectedKeys.iterator();
				SelectionKey key = null;
				while(it.hasNext()) {
					key = it.next();
					it.remove();
					try {
						handlerInput(key);
					} catch(Exception e) {
						if(key!=null) {
							key.cancel();
							if(key.channel()!=null) 
								key.channel().close();
						}
					}
				}
			} catch(Exception e ) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		if(selector!=null){
			try{
				selector.close();
			} catch(IOException e ) {
				e.printStackTrace();
			}
		}
	}
	
	private void handlerInput(SelectionKey key) throws IOException {
		if(key.isValid()) {
			SocketChannel sc = (SocketChannel) key.channel();
			if(key.isConnectable()) {
				if(sc.finishConnect()) {
					sc.register(selector, SelectionKey.OP_READ);
					//doWrite(sc);
				} else {
					System.exit(1);
				}
			}
			if(key.isReadable()) {
				ByteBuffer readBuffer = ByteBuffer.allocate(1024);
				int readBytes = sc.read(readBuffer);
				if(readBytes>0) {
					readBuffer.flip();
					
				}
			}
		}
	}
}
