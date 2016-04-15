package daily.y2016.m4.d15.reactor.multithreaded;

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

public class MultithreadedReactor implements Runnable {

	public static void main(String[] args) throws IOException {
		MultithreadedReactor server = new MultithreadedReactor(8000);
		new Thread(server).start();
	}
	
	final Selector selector;
	final ServerSocketChannel serverSocket;
	
	MultithreadedReactor(int port) throws IOException {
		selector = Selector.open();
		serverSocket = ServerSocketChannel.open();
		serverSocket.socket().bind(new InetSocketAddress(port));
		serverSocket.configureBlocking(false);
		SelectionKey sk = serverSocket.register(selector, SelectionKey.OP_ACCEPT);
		sk.attach(new Acceptor());
	}
	
	public void run() {
		try {
			while(!Thread.interrupted()) {
				selector.select();
				Set selected = selector.selectedKeys();
				Iterator it = selected.iterator();
				while(it.hasNext())
					dispatch((SelectionKey)(it.next()));
				selected.clear();
			}
		} catch(IOException e) {
			
		}
	}
	
	void dispatch(SelectionKey k) {
		Runnable r = (Runnable) (k.attachment());
		if(r!=null)
			r.run();
	}
	
	class Acceptor implements Runnable {
		public void run() {
			try {
				SocketChannel c = serverSocket.accept();
				if(c!=null)
					new Handler(selector, c);
			} catch(IOException e){
				
			}
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
	Selector selector;
	
	Handler(Selector sel, SocketChannel c) throws IOException {
		socket = c;
		selector = sel;
		c.configureBlocking(false);
		
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
			if(state==READING)
				read();
			else if(state==SENDING)
				send();
		} catch(IOException e) {
			
		}
	}
	
	void send() throws IOException {
		socket.write(output);
		if(outputIsComplete()) 
			sk.cancel();
	}
	
	static ExecutorService pool = Executors.newFixedThreadPool(10);
	static final int PROCESSING = 3;
	
	synchronized void read() throws IOException {
		socket.read(input);
		if(inputIsComplete()) {
			state = PROCESSING;
			pool.execute(new Processor());
		}
	}
	
	synchronized void processAndHandOff() {
		process();
		state = SENDING;
		sk.interestOps(SelectionKey.OP_WRITE);
		selector.wakeup();
	}
	
	class Processor implements Runnable {
		public void run() {
			processAndHandOff();
		}
	}
}
