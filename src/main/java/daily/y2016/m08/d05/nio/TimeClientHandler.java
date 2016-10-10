package daily.y2016.m08.d05.nio;

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
	private int port;
	private Selector selector;
	private SocketChannel socketChannel;
	private volatile boolean stop;
	
	public TimeClientHandler(String host, int port) {
		this.host = host ==null?"127.0.0.1":host;
		this.port = port;
		try {
			selector = Selector.open();
			socketChannel = SocketChannel.open();
			socketChannel.configureBlocking(false);
			
		} catch(IOException e) {
			System.exit(1);
		}
	}
	
	private void doConnect() throws IOException {
		if(socketChannel.connect(new InetSocketAddress(host, port))) {
			socketChannel.register(selector, SelectionKey.OP_READ);
			doWrite(socketChannel);
		} else {
			socketChannel.register(selector, SelectionKey.OP_CONNECT);
		}
	}
	
	public void run() {
		try {
			doConnect();
		} catch(IOException e) {
			System.exit(1);
		}
		
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
						handleInput(selectionKey);
					} catch(Exception e) {
						if(selectionKey!=null) {
							selectionKey.cancel();
							if(selectionKey.channel()!=null)
								selectionKey.channel().close();
						}
					}
				}
			} catch(Exception e) {
				System.exit(1);
			}
		}
		if(selector!=null) {
			try {
				selector.close();
			} catch(IOException e) {
				
			}
		}
	}
	
	private void handleInput(SelectionKey selectionKey) throws IOException {
		if(selectionKey.isValid()) {
			SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
			if(selectionKey.isConnectable()) {
				if(socketChannel.finishConnect()) {
					socketChannel.register(selector, SelectionKey.OP_READ);
					doWrite(socketChannel);
				} else {
					System.exit(1);
				}
			}
			if(selectionKey.isReadable()) {
				ByteBuffer readBuffer = ByteBuffer.allocate(1024);
				int reaBytes = socketChannel.read(readBuffer);
				if(readBytes>0) {
					readBuffer.flip();
					byte[] bytes = new byte[readBuffer.remaining()];
					readBuffer.get(bytes);
					String body = new String(bytes, "UTF-8");
					this.stop = true;
				} else if(readBytes<0) {
					selectionKey.cancel();
					socketChannel.close();
				} else {
					;
				}
			}
		}
	}
	
	private void doWrite(SocketChannel socketChannel) throws IOException {
		byte[] req = "QUERY TIME ORDER".getBytes();
		ByteBuffer writeBuffer = ByteBuffer.allocate(req.length);
		writeBuffer.put(req);
		writeBuffer.flip();
		socketChannel.write(writeBuffer);
		if(!writeBuffer.hasRemaining()) 
			System.out.println("Send order 2 server success");
	}
}
