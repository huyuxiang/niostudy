package douglea.test5;

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

public class MultipleReactor implements Runnable {
	
	
	public static void main(String args[]) throws IOException {
		MultipleReactor mainReactor = new MultipleReactor(8001);
		new Thread(mainReactor).start();
		
		mainReactor.subReactorOnListen();
		
		
	}
	static final int DEFAULT_SUB_POOL_SIZE = 10;
	static final ExecutorService subReactor_Pool = Executors.newFixedThreadPool(
			DEFAULT_SUB_POOL_SIZE);
	
	static Selector[] selectors;
	public void subReactorOnListen() throws IOException {
		selectors = new Selector[10];
		for(int i=0;i<selectors.length;i++) {
			Selector sel = Selector.open();
			selectors[i] = sel;
			Listen listen = new Listen(selectors[i]);
			subReactor_Pool.execute(listen);
		}
		
	}

	public class Listen implements Runnable {
		public Selector sel;
		public Listen(Selector sel) {
			this.sel = sel;
		} 
		public void run() {
			try {
				while(!Thread.interrupted()) {
					sel.select(1000);
					Set<SelectionKey> selected = sel.selectedKeys();
					Iterator<SelectionKey> it = selected.iterator();
					while(it.hasNext()) {
						dispatch((SelectionKey)(it.next()));
					}
					selected.clear();
				}
			} catch(IOException e ){
				
			}
		}
	}

	final ServerSocketChannel serverSocket;
	
	final Selector selector;
	
	MultipleReactor (int port) throws IOException {
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
		} catch(IOException e ){
			
		}
	}
	
	static void dispatch(SelectionKey k) {
		Runnable r = (Runnable) (k.attachment());
		if(r!=null) 
			r.run();
	}
	
	
	int next = 1;
	class Acceptor implements Runnable {
		public synchronized void run() {
			try {
				SocketChannel c = serverSocket.accept();
				if(c!=null) {
					print("accept one channel :	add to selectors for listen");
					c.configureBlocking(false);
					SelectionKey sk = c.register(selectors[next], SelectionKey.OP_READ);
					sk.attach(new Handler(c, sk));
					selectors[next].wakeup();
					if(++next==selectors.length) next = 0;
				}
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
		SocketChannel socket;
		SelectionKey sk;
		ByteBuffer input = ByteBuffer.allocate(MAXIN);
		ByteBuffer output = ByteBuffer.allocate(MAXOUT);
		
		static final int READING = 0, SENDING = 1, PROCESSING = 3;
		int state = READING;
		
		Handler(SocketChannel socket, SelectionKey sk) throws IOException {
			this.socket = socket;
			this.sk = sk;
		}
		
		public void run() {
			try {
				if(state==READING) read();
				else if(state ==SENDING) send();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		
		public void send() throws IOException {
			output.clear();
			byte[] outputarray = "hello client".getBytes();
			output.put(output);
			output.flip();
			socket.write(output);
			if(outputIsComplete()) sk.cancel();
		}
		
		synchronized void read() throws IOException {
			input.clear();
			socket.read(input);
			byte[] inputarray = new byte[input.remaining()];
			input.get(inputarray);
			System.out.println("Client array::" + new String(inputarray));
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
			print("interestOps:	" + "" + " OP_WRITE");
			sk.interestOps(SelectionKey.OP_WRITE);
		}
		
		boolean inputIsComplete() {
			print("input:complete");
			return true;
		}
		
		boolean outputIsComplete() {
			print("output:complete");
			return true;
		}
		
		void process() {
			print("process");
		}
		
	}
	
	public static void print(Object o) {
		System.out.println(o);
	}
}
