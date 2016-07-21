package daily.template.jdk.src.java.io;

abstract class FileSystem {

	public static native FileSystem getFileSystem();
	
	public abstract char getSeparator();
	
	public abstract char getPathSeparator();
	
	public abstract String normalize(String path);
	
	public abstract int prefixLength(String path);
	
	public abstract String resolve(String parent, String child);
	
	public abstract String getDefaultParent();
	
	public abstract String formURIPath(String path);
	
	public abstract boolean isAbsolute(File f);
	
	public abstract String resolve(File f);
	
	
}
