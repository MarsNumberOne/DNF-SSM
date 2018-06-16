package Test;

/**
 * @author hly 
 * @date 2016年6月24日 下午5:08:14
 * @Description:请求第三方异常
 */
public class ThirdException extends RuntimeException{
	
	private static final long serialVersionUID = 8018459712096334283L;

	public ThirdException() {
		super();
	}

	public ThirdException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ThirdException(String message, Throwable cause) {
		super(message, cause);
	}

	public ThirdException(String message) {
		super(message);
	}

	public ThirdException(Throwable cause) {
		super(cause);
	}

	
}
