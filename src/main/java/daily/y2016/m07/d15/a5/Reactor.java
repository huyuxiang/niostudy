package daily.y2016.m07.d15.a5;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Reactor implements Runnable{

	public static void main(String[] args) throws IOException {
		Reactor reactor = new Reactor(8000);
		new Thread(reactor).start();
	}
	
	final Selector selector;
	final ServerSocketChannel serverSocketChannel;
	
	Reactor(int port) throws IOException {
		selector = Selector.open();
		serverSocketChannel  = ServerSocketChannel.open();
		serverSocketChannel.socket().bind(new InetSocketAddress(port));
		serverSocketChannel.configureBlocking(false);
		SelectionKey selectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		selectionKey.attach(new Acceptor());
	}
	
	public void run() {
		try {
			while(!Thread.interrupted()) {
				selector.select();
				Set set = selector.selectedKeys();
				Iterator it = set.iterator();
				while(it.hasNext())
					dispatch((SelectionKey)(it.next()));
				set.clear();
			}
		} catch(IOException e) {
			
		}
	}
	
	void dispatch(SelectionKey selectionKey) {
		Runnable r = (Runnable) (selectionKey.attachment());
		if(r!=null)
			r.run();
	}
	
	class Acceptor implements Runnable {
		public void run() {
			try {
				SocketChannel socketChannel = serverSocketChannel.accept();
				if(socketChannel!=null)
					new Handler(selector, socketChannel);
			} catch(IOException e) {
				
			}
		}
	}
}

final class Handler implements Runnable {
	int MAXIN = 1024;
	int MAXOUT = 1024;
	final SocketChannel socketChannel;
	final SelectionKey selectionKey;
	ByteBuffer input = ByteBuffer.allocate(MAXIN);
	ByteBuffer output = ByteBuffer.allocate(MAXOUT);
	static final int READING = 0, SENDING = 1;
	int state = READING;
	
	Handler(Selector sel, SocketChannel sc) throws IOException {
		socketChannel = sc;
		socketChannel.configureBlocking(false);
		selectionKey = socketChannel.register(sel, 0);
		selectionKey.attach(this);
		selectionKey.interestOps(SelectionKey.OP_READ);
		sel.wakeup();
	}
	
	public void run() {
		try {
			if(state==READING)
				read();
			else if(state==SENDING)
				send();
		} catch(IOException e) {
			
		}
	}
	
	boolean inputIsComplete() {
		return true;
	}
	
	boolean outputIsComplete() {
		return true;
	}
	
	void process() {
		
	}
	
	void read() throws IOException {
		socketChannel.read(input);
		if(inputIsComplete()) {
			process();
			state = SENDING;
			selectionKey.interestOps(SelectionKey.OP_WRITE);
		}
	}
	
	void send() throws IOException {
		socketChannel.write(output);
		if(outputIsComplete())
			selectionKey.cancel();
	}
}
