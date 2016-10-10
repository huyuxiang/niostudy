package daily.y2016.m08.d11.nio.a1;

import java.io.IOException;

public class MultiplexerTimeServer implements Runnable {

	private Selector selector;
	
	private ServerSocketChannel serverSocketChannel;
	
	private volatile boolean stop;
	
	public MultiplexerTimeServer(int port) {
		try {
			selector = Selector.open();
			serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.configureBlocking(false);
			serverSocketChannel.socket().bind(new InetSocketAddress(port), 1024);
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
			System.out.println("the time server is start in port:" + port);
		} catch(IOException e) {
			e.printStackTrace();
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
						handlerInput(selectionKey);
					} catch(Exception e) {
						if(selectionKey!=null) {
							selectionKey.cancel();
							if(selectionKey.channel()!=null)
								selectionKey.channel().close();
						}
					}
				}
			} catch(Throwable t) {
				t.printStackTrace();
			}
			                     
		}
		
		if(selector!=null) {
			try {
				selector.close();
			} catch(IOException e) {
				
			}
		}
	}
	
	private void handlerInput(SelectionKey selectionKey) throws IOException {
		if(selectionKey.isValid()) {
			if(selectionKey.isAcceptable()) {
				ServerSocketChannel serverSocketChannel = 
						(ServerSocketChannel)selectionKey.channel();
				SocketChannel socketChannel = serverSocketChannel.accept();
				socketChannel.configureBlocking(false);
				socketChannel.register(selector, SelectionKey.OP_READ);
			}
			if(selectionKey.isReadable()) {
				SocketChannel
			}
		}
	}
}
