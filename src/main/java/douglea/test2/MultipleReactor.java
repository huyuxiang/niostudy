package douglea.test2;

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
import java.util.logging.Handler;

public class MultipleReactor implements Runnable {
	
	final Selector selector;
	final ServerSocketChannel serverSocket;
	
	MultipleReactor (int port ) throws IOException {
		selector = Selector.open();
		serverSocket = ServerSocketChannel.open();
		serverSocket.socket().bind(
				new InetSocketAddress(port));
		serverSocket.configureBlocking(false);
		SelectionKey sk = serverSocket.register(
				selector, 
				SelectionKey.OP_ACCEPT);
		sk.attach(new Acceptor());
	}
	
	public void run() {
		try {
			while(!Thread.interrupted()) {
				selector.select();
				Set<SelectionKey> selected = selector.selectedKeys();
				Iterator<SelectionKey> it = selected.iterator();
				while(it.hasNext()) {
					dispatch((SelectionKey)(it.next()));
				}
				selected.clear();
			}
		} catch(IOException e ) {
			
		}
	}
	
	void dispatch(SelectionKey k) {
		Runnable r = (Runnable) (k.attachment());
		if(r!=null) 
			r.run();
	}
	
	Selector[] selectors;
	int next = 0;
	class Acceptor implements Runnable {
		public synchronized void run() {
			try {
				SocketChannel c = serverSocket.accept();
				if(c!=null) 
					new Handler(selectors[next], c);
				if(++next==selectors.length) next = 0;
			} catch(IOException e) {
				
			}
		}
	}
	
	static final int MAXIN = 1024;
	static final int MAXOUT = 1024;
	
	static final int DEFAULT_POOL_SIZE = 100;
	static final ExecutorService pool = Executors.newFixedThreadPool(
			DEFAULT_POOL_SIZE);
	
	class Handler implements Runnable {
		final SocketChannel socket ;
		final SelectionKey sk;
		ByteBuffer input = ByteBuffer.allocate(MAXIN);
		ByteBuffer output = ByteBuffer.allocate(MAXOUT);
		
		static final int READING = 0, SENDING = 1, PROCESSING = 3;
		int state = READING;
		
		Handler(Selector sel, SocketChannel c ) throws IOException {
			socket = c; 
			c.configureBlocking(false);
			sk = socket.register(sel, 0);
			sk.attach(this);
			sk.interestOps(SelectionKey.OP_READ);
			sel.wakeup();
		}
		
		public void run() {
			try {
				if(state == READING) read();
				else if (state == SENDING) send();
				
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		
		void send() throws IOException {
			socket.write(output);
			if(outputIsComplete()) sk.cancel();
		}
		
		synchronized void read() throws IOException {
			socket.read(input);
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
			state = SENDING;
			sk.interestOps(SelectionKey.OP_WRITE);
			
		}
		
		boolean inputIsComplete() {
			return false;
		}
		boolean outputIsComplete() {
			return false;
		}
		void process() {
			
		}
	}
}
