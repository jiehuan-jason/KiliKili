package top.jiehuan.kilikili.Exception;

public class ErrorVideoStatusException extends Exception {
	int code;
	String content;
	public ErrorVideoStatusException(int code, String content){
		this.code = code;
		this.content = content;
	}
	public int getCode(){
		return code;
	}
	public String getContent(){
		return content;
	}
}
