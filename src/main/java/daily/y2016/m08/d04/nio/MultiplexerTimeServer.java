package daily.y2016.m08.d04.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

public class MultiplexerTimeServer implements Runnable {

	private Selector selector;
	
	private ServerSocketChannel serverSocketChannel;
	
	private volatile boolean stop;
	
	public MultiplexerTimeServer(int port) {
		try{
			selector = Selector.open();
			serverSocketChannel = ServerSocketChannel.open();
			serverSocketCahnnel.configureBlocking(false);
			serverSocketChannel.socket().bind(new InetSocketAddress(port), 1024);
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
			
		} catch(IOException e) {
			System.exit(1);
		}
	}
	
	public void stop() {
		this.stop = true;
	}
	
	public void run() {
		while(!stop) {
			try {
				selector.select(1000);
				Set<SelectionKey> selectedKeys = selector.selectedKeys();
				Iterator<SelectionKey> it = selectedKeys.iterator();
				SelectionKey selectionKey = null;
				while(it.hasNext()) {
					selectionKey = it.next();
					it.remove();
					try {
						handlerInput(selectionkey);
					} catch(Exception e) {
						if(selectionKey!=null) {
							selectionKey.cancel();
							if(selectionKey.cancel()!=null)
								selectionKey.channel().close();
						}
					}
				}
			} catch(Throwable t) {
				t.printStackTrace();
				
			}
		}
	}
}
