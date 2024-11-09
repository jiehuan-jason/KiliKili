package top.jiehuan.kilikili.Exception;

public class WebReturnErrorCodeException extends Exception {
	int code;
	public WebReturnErrorCodeException(int code){
		this.code = code;
	}
	public int getCode(){
		return code;
	}
}
