package daily.template.reactor.Basic;

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

//reactor 1: setup
public class Reactor implements Runnable {
	
	public static void main(String args[] ) throws IOException {
		Reactor reactor = new Reactor(8000);
		new Thread(reactor).start();
	}
	
	final Selector selector;
	final ServerSocketChannel serverSocketChannel;
	
	Reactor(int port) throws IOException {
		selector = Selector.open();
		serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.socket().bind(new InetSocketAddress(port), 1024);
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
//Reactor 4: Handler setup
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
		//Optionally try first read now
		selectionKey = socketChannel.register(sel, 0);
		selectionKey.attach(this);
		selectionKey.interestOps(SelectionKey.OP_READ);
		sel.wakeup();
	}
	
	//Reactor 5: Request handling
	public void run() {
		try {
			if(state == READING) 
				read();
			else if(state == SENDING) 
				send();
		} catch(IOException ex) {
			
		}
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

	
	void read() throws IOException {
		socketChannel.read(input);
		if(inputIsComplete()) {
			process();
			state = SENDING;
			//Normally alse do first write now
			selectionKey.interestOps(SelectionKey.OP_WRITE);
		}
	}
	
	void send() throws IOException {
		socketChannel.write(output);
		if(outputIsComplete()) 
			selectionKey.cancel();
	}
}

/*
 A simple use of GoF State-Object pattern
  Rebind appropriate handler as attachment
  
   class Handler {
   	public void run() {
   	 socket.read(input);
   	 if(inputIsComplete()) {
   	  process();
   	  sk.attach(new Sender());
   	  sk.interest(SelectionKey.OP_WRITE);
   	  sk.selector().wakeup();
   	 }
   	}
   	
   	class Sender impelements Runnable {
   	 public void run() {
   	  socket.write(output);
   	  if(outputIsComplete()) sk.cancel();
   	 }
   	}
   }
 */
