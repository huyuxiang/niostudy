package douglea.test1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class GoFReactor implements Runnable {
	final Selector selector;
	final ServerSocketChannel serverSocket;
	
	GoFReactor(int port) throws IOException {
		selector = Selector.open();
		serverSocket = ServerSocketChannel.open();
		serverSocket.socket().bind(
				new InetSocketAddress(port));
		serverSocket.configureBlocking(false);
		SelectionKey sk = serverSocket.register(selector, 
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
		} catch(IOException e) {
		}
	}
	
	void dispatch(SelectionKey k)  {
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
			} catch(IOException e) {
				
			}
		}
	}
	static final int MAXIN = 1024;
	static final int MAXOUT = 1024;
	
	final class Handler implements Runnable {
		final SocketChannel socket;
		final SelectionKey sk ;
		ByteBuffer input = ByteBuffer.allocate(MAXIN);
		ByteBuffer output = ByteBuffer.allocate(MAXOUT);
		static final int READING = 0, SENDING = 1;
		int state = READING;
		
		Handler(Selector sel, SocketChannel c) throws IOException {
			socket = c;
			c.configureBlocking(false);
			sk = socket.register(sel, 0);
			sk.attach(this);
			sk.interestOps(SelectionKey.OP_READ);
			sel.wakeup();
		}
		
		boolean inputIsComplete() {
			return false;
			
		}
		boolean outputIsComplete() {
			return false;
			
		}
		void process() {
			
		}
		
		public void run() {
			try {
				socket.read(input);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(inputIsComplete()) {
				process();
				sk.attach(new Sender());
				sk.interestOps(SelectionKey.OP_WRITE);
				sk.selector().wakeup();
			}
		}
		
		class Sender implements Runnable {
			public void run() {
				try {
					socket.write(output);
				} catch (IOException e) {
					e.printStackTrace();
				}
				if(outputIsComplete())
					sk.cancel();
			}
		}
		
		void read() throws IOException {
			socket.read(input);
			if(inputIsComplete()) {
				process();
				state = SENDING;
				sk.interestOps(SelectionKey.OP_WRITE);
			}
		}
		
		void send() throws IOException {
			socket.write(output);
			if(outputIsComplete())
				sk.cancel();
		}
		
	}
}
