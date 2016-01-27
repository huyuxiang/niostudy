package daily.template.reactor.ReactorPool;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
//Basic Reactor Design
//Single threaded version
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


//reactor 1: setup
public class MultipleReactor implements Runnable {
	final Selector selector;
	final ServerSocketChannel serverSocket;
	
	MultipleReactor(int port) throws IOException {
		selector = Selector.open();
		serverSocket = ServerSocketChannel.open();
		serverSocket.socket().bind(new InetSocketAddress(port));
		serverSocket.configureBlocking(false);
		SelectionKey sk = serverSocket.register(selector, SelectionKey.OP_ACCEPT);
		sk.attach(new Acceptor());
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
				Set selected = selector.selectedKeys();
				Iterator it = selected.iterator();
				while(it.hasNext()) 
					dispatch((SelectionKey)(it.next()));
				selected.clear();
			}
		} catch(IOException ex) {
			
		}
	}
	
	void dispatch(SelectionKey k) {
		Runnable r = (Runnable) (k.attachment());
		if(r!=null) 
			r.run();
	}
	
	Selector[] selectors;
	int next = 0;
//reactor 3: acceptor
	class Acceptor implements Runnable {//inner
		public synchronized void run() {
			SocketChannel connection=null;
			try {
				connection = serverSocket.accept();
			} catch (IOException e1) {
			}
			if(connection !=null)
				try {
					new Handler(selectors[next], connection);
				} catch (IOException e) {
				}
			if(++next==selectors.length) next= 0;
		}
	}
}
class Handler implements Runnable {
	
	int MAXIN = 1024;
	int MAXOUT = 1024;
	final SocketChannel socket;
	final SelectionKey sk;
	ByteBuffer input = ByteBuffer.allocate(MAXIN);
	ByteBuffer output = ByteBuffer.allocate(MAXOUT);
	static final int READING = 0, SENDING = 1;
	int state = READING;
	
	Handler(Selector sel, SocketChannel c) throws IOException {
		socket = c;
		c.configureBlocking(false);
		//Optionally try first read now
		sk = socket.register(sel, 0);
		sk.attach(this);
		sk.interestOps(SelectionKey.OP_READ);
		sel.wakeup();
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
		socket.write(output);
		if(outputIsComplete()) 
			sk.cancel();
	}
	
	//uses util.concurrent thread pool
	static ExecutorService pool = Executors.newFixedThreadPool(10);
	static final int PROCESSING = 3;
	
	synchronized void read() throws IOException {
		socket.read(input);
		if(inputIsComplete()) {
			state = PROCESSING;
			pool.execute(new Processer());
		}
	}
	
	synchronized void processAndHandOff() {
		process();
		state = SENDING;//or rebind attachment
		sk.interestOps(SelectionKey.OP_WRITE);
	}
	
	class Processer implements Runnable {
		public void run() {
			processAndHandOff();
		}
	}
}
