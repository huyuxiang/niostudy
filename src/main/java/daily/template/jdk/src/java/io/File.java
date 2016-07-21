package daily.template.jdk.src.java.io;


public class File implements Serializable , Comparable<File> {

	static private FileSystem fs = FileSystem.getFileSystem();
	
	private String path;
	
	private static enum PathStatus{INVALID, CHECKED};
	
	private transient PathStatus status = null;
	
	final boolean isInvalid() {
		if(status ==null) {
			status = (this.path.indexOf('\u0000')<0)? 
					PathStatus.CHECKED
					:PathStatus.INVALID;
		}
		return status == PathStatus.INVALID;
	}
	
	private transient int prefixLength;
	
	int getPrefixLength() {
		return prefixLength;
	}
	
	public static final char separatorChar = fs.getSeparator();
	
	public static final String separator = "" + separatorChar;
	
	public static final char pathSeparatorChar = fs.getPathSeparator();
	
	public static final String pathSeparator = "" + pathSeparatorChar;
	
	private File(String pathname, int prefixLength) {
		this.path = pathname;
		this.prefixLength = prefixLength;
	}
	
	private File(String child, File parent) {
		assert parent.path !=null;
		assert (!parent.path.equals(""));
		this.path = fs.resolve(parent.path, child);
		this.prefixLength= parent.prefixLength;
	}
	
	public File(String pathname) {
		if(pathname ==null) {
			throw new NullPointerException();
		}
		this.path = fs.normalize(pathname);
		this.prefixLength = fs.prefixLength(this.path);
	}
	
	public File(String parent, String child) {
		if(child ==null) {
			throw new NullPointerException();
		}
		if(parent!=null) {
			if(parent.equals("")) {
				this.path = fs.resolve(fs.getDefaultParent(),
						fs.normalize(child));
			} else {
				this.path = fs.resolve(fs.normalize(parent), 
						fs.normalize(child));
			}
		} else {
			this.path = fs.normalize(child);
		}
		this.prefixLength = fs.prefixLength(this.path);
	}
	
}
