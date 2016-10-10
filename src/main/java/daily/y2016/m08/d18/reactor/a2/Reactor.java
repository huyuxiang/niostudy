package daily.y2016.m08.d18.reactor.a2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Reactor implements Runnable {

	public static void main(String[] args) throws IOException {
		Reactor reactor = new Reactor(8080);
		new Thread(reactor).start();
	}
	
	final Selector selector;
	final ServerSocketChannel serverSocketChannel;
	
	public Reactor(int port) throws IOException  {
		selector = Selector.open();
		serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.socket().bind(new InetSocketAddress(port));
		serverSocketChannel.configureBlocking(false);
		SelectionKey selectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		selectionKey.attach(new Acceptor());
	}
	public void run() {
		while(true) {
			try {
				int a = selector.select(1000);
			} catch (IOException e) {
				e.printStackTrace();
			}
			Set set = selector.selectedKeys();
			Iterator it = set.iterator();
			while(it.hasNext()) 
				dispatch((SelectionKey)it.next());
			set.clear();
		}
	}
	
	void dispatch(SelectionKey selectionKey ) {
		Runnable r  =(Runnable)selectionKey.attachment();
		if(r!=null) 
			r.run();
	}
	
	class Acceptor implements Runnable {
		
		public void run() {
			SocketChannel socketChannel;
			try {
				socketChannel = serverSocketChannel.accept();
				new Handler(selector, socketChannel);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

class Handler implements Runnable {
	
	int MAXIN = 1024;
	int MAXOUT = 1024;
	ByteBuffer input = ByteBuffer.allocate(MAXIN);
	ByteBuffer output = ByteBuffer.allocate(MAXOUT);
	int READING = 0, SENDING = 1;
	int state = READING;
	Selector selector;
	SocketChannel socketChannel;
	SelectionKey selectionKey;
	
	Handler(Selector selector, SocketChannel sc) throws IOException {
		this.selector = selector;
		this.socketChannel = sc;
		SelectionKey selectionKey = socketChannel.register(selector, 0);
		selectionKey.attach(this);
		this.selectionKey = selectionKey;
		selectionKey.interestOps(SelectionKey.OP_READ);
	}
	
	public void run() {
		if(state ==READING)
			read();
		else if(state==SENDING)
			send();
	}
	
	void read() {
		try {
			socketChannel.read(input);
			process();
			this.selectionKey = socketChannel.register(selector, SelectionKey.OP_WRITE);
			state = SENDING;
		} catch (ClosedChannelException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	void send() {
		
		try {
			socketChannel.write(output);
			selectionKey.cancel();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void process() {
		
	}
}
