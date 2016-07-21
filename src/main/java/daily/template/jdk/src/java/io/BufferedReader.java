package daily.template.jdk.src.java.io;

public class BufferedReader extends Reader {

	private Reader in;
	
	private char cb[];
	private int nChars, nextChar;
	
	private static final int INVALIDATED = -2;
	private static final int UNMARKED = -1;
	private int markedChar = UNMARKED;
	private int readAheadLimit = 0;
	
	private boolean skipLF = false;
	
	private boolean markedSkipLF = false;
	
	private static int defaultCharBufferSize = 8192;
	private static int defaultExpectedLineLength = 80;
	
	public BufferedReader(Reader in, int sz) {
		super(in);
		if(sz<=0)
			throw new IllegalArgumentException("Buffer size<=0");
		this.in = in;
		cb = new char[sz];
		nextChar = nChars = 0;
	}
	
	public BufferedReader(Reader in) {
		this(in, defaultCharBufferSize);
	}
	
	private void ensureOpen() throws IOException {
		if(in ==null) 
			throw new IOException("Stream closed");
	}
	
	private void fill() throws IOException {
		int dst;
		if(markedChar <= UNMARKED) {
			dst = 0;
		} else {
			int delta = nextChar - markedChar;
			if(delta >= readAheadLimit) {
				markedChar = INVALIDATED;
				readAheadLimit = 0;
				dst = 0;
			} else {
				if(readAheadLimit <= cb.length) {
					System.arraycopy(cb, markedChar, cb, 0, delta);
					markedChar = 0;
					dst = delta;
				} else {
					char ncb[] = new char[readAheadLimit];
					System.arraycopy(cb, markedChar, ncb, 0, delta);
					cb = ncb;
					markedChar = 0;
					dst = delta;
				}
				
				nextChar = nChars = delta;
			}
		}
		
		int n;
		do {
			n = in.read(cb, dst, cb.length - dst);
		} while(n==0);
		if(n>0) {
			nChars = dst + n;
			nextChar = dst;
		}
	}
	
	public int read() throws IOException{
		synchronized (lock) {
			ensureOpen();
			for(;;) {
				if(nextChar >= nChars) {
					fill();
					if(nextChar >= nChars) 
						return -1;
				}
				if(skipLF) {
					skipLF = false;
					if(cb[nextChar] == '\n') {
						nextChar++;
						continue;
					}
				}
				return cb[nextChar++];
			}
		}
	}
	
	private int read1(char[] cbuf, int off, int len) throws IOException {
		if(nextChar >= nChars) {
			if(len >= cb.length && markedChar <= UNMARKED && !skipLF) {
				return in.read(cbuf, off, len);
			}
			fill();
		}
		if(nextChar >= nChars) return -1;
		if(skipLF) {
			skipLF = false;
			if(cb[nextChar] == '\n') {
				nextChar++;
				if(nextChar >= nChars) 
					fill();
				if(nextChar >= nChars) 
					return -1;
			}
		}
		int n = Math.min(len, nChars - nextChar) ;
		System.arraycopy(cb, nextChar, cbuf, off, n);
		nextChar += n;
		return n;
	}
	
	public int read(char cbuf[] , int off, int len) throws IOException {
		synchronized(lock) {
			ensureOpen();
			if((off<0) || (off>cbuf.length) ||(len<0) ||
					((off+len)>cbuf.length) || ((off+len) <0)) {
				throw new IndexOutOfBoundsException();
			} else if(len==0){
				return 0;
			}
			
			int n = read1(cbuf, off, len);
			if(n<=0) return n;
			while((n<len) && in.ready()) {
				int n1 = read1(cbuf, off+n, len-n);
				if(n1<=0) break;
				n += n1;
			}
			return n;
		}
		
		
	}
}
