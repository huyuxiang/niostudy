package daily.template.rpc.dsf.common;

/**
 * 自定义异常
 * @author ZhongweiLee
 *
 */
public final class RpcException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public static final int UNKNOWN_EXCEPTION = 0;
	
	public static final int CONFIG_EXCEPTION = 1;
	
	
	private int code;
	
    public RpcException(int code) {
        super();
        this.code = code;
    }

    public RpcException(String message) {
        super(message);
    }
    public RpcException(String message,Exception e) {
        super(message,e);
    }
    public RpcException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public RpcException(int code, String message) {
        super(message);
        this.code = code;
    }

    public RpcException(int code, Throwable cause) {
        super(cause);
        this.code = code;
    }

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

    
    
}
