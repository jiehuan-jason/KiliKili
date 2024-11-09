package top.jiehuan.kilikili.Exception;

public class ErrorVideoStatusException extends Exception {
	int code;
	public ErrorVideoStatusException(int code){
		this.code = code;
	}
	public int getCode(){
		return code;
	}
}
