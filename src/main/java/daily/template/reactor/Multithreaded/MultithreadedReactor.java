package daily.template.reactor.Multithreaded;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


//reactor 1: setup
public class MultithreadedReactor implements Runnable {
	
	public static void main(String args[] ) throws IOException {
		MultithreadedReactor server = new MultithreadedReactor(8000);
		new Thread(server).start();
	}
	
	final Selector selector;
	final ServerSocketChannel serverSocketChannel;
	
	MultithreadedReactor(int port) throws IOException {
		selector = Selector.open();
		serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.socket().bind(new InetSocketAddress(port));
		serverSocketChannel.configureBlocking(false);
		SelectionKey selectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		selectionKey.attach(new Acceptor());
	}
	
	/*
	 Alternatively, use explicit SPI provider:
	 	SelectorProvider p = SelectorProvider.provider();
	 	selector = p.openSelector();
	 	serverSocket = p.openServerSocketChannel();
	 */
	
//reactor 2: dispatch loop
	public void run() { //normally in a new Thread
		try {
			while(!Thread.interrupted()) {
				selector.select();
				Set set = selector.selectedKeys();
				Iterator it = set.iterator();
				while(it.hasNext()) 
					dispatch((SelectionKey)(it.next()));
				set.clear();
			}
		} catch(IOException ex) {
			
		}
	}
	
	void dispatch(SelectionKey selectionKey) {
		Runnable r = (Runnable) (selectionKey.attachment());
		if(r!=null) 
			r.run();
	}
//reactor 3: acceptor
	class Acceptor implements Runnable {//inner
		public void run() {
			try {
				SocketChannel socketChannel = serverSocketChannel.accept();
				if(socketChannel !=null) 
					new Handler(selector, socketChannel);
			} catch(IOException ex) {
				
			}
		}
	}
}

class Handler implements Runnable {
	
	int MAXIN = 1024;
	int MAXOUT = 1024;
	final SocketChannel socketChannel;
	final SelectionKey selectionKey;
	ByteBuffer input = ByteBuffer.allocate(MAXIN);
	ByteBuffer output = ByteBuffer.allocate(MAXOUT);
	static final int READING = 0, SENDING = 1;
	int state = READING;
	Selector selector;
	
	Handler(Selector selector, SocketChannel sc) throws IOException {
		socketChannel = sc;
		this.selector = selector;
		socketChannel.configureBlocking(false);
		//Optionally try first read now
		selectionKey = socketChannel.register(selector, 0);
		selectionKey.attach(this);
		selectionKey.interestOps(SelectionKey.OP_READ);
		selector.wakeup();
	}
	
	public void run() {
		try {
			if(state == READING) 
				read();
			else if(state == SENDING) 
				send();
		} catch(IOException ex) {
			
		}
	}
	
	void send() throws IOException {
		socketChannel.write(output);
		if(outputIsComplete()) 
			selectionKey.cancel();
	}
	
	//uses util.concurrent thread pool
	static ExecutorService pool = Executors.newFixedThreadPool(10);
	static final int PROCESSING = 3;
	
	synchronized void read() throws IOException {
		socketChannel.read(input);
		if(inputIsComplete()) {
			state = PROCESSING;
			pool.execute(new Processer());
		}
	}
	
	class Processer implements Runnable {
		public void run() {
			processAndHandOff();
		}
	}
	
	synchronized void processAndHandOff() {
		process();
		state = SENDING;//or rebind attachment
		selectionKey.interestOps(SelectionKey.OP_WRITE);
		selector.wakeup();
	}
	
	boolean inputIsComplete() {
		System.out.println("inputIsComplete()");
		return true;
	}
	
	boolean outputIsComplete() {
		System.out.println("outputIsComplete()");
		return true;
	}
	
	void process() {
		System.out.println("process()");
	}
}
