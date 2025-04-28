package top.jiehuan.kilikili.Exception;

public class WebReturnErrorCodeException extends Exception {
	int code;
	String message;
	public WebReturnErrorCodeException(int code){
		this(code, "");
	}
	public WebReturnErrorCodeException(int code, String message){
		this.code = code;
		this.message = message;
	}
	public int getCode(){
		return code;
	}
	public String getMessage(){
		return "WebReturnErrorCodeException Code:"+code+" Message:"+message;
	}
}
