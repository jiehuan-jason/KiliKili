package top.jiehuan.kilikili.util;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;


public class GetLangRes {
	private String lang_file_content;
	public GetLangRes(String lang) throws IOException{
		System.out.println(lang);
		if(lang.equals("zh-CN")){
			readLangFile("zh_cn");
		}else{
			readLangFile("en_us");
		}
	}
	private boolean readLangFile(String lang) throws IOException{
		InputStream is = null;
		DataInputStream dis = null;
		is = this.getClass().getResourceAsStream("/lang/"+lang+".lang");
		if (is == null)
		{
		    return false;
		} 
		int count = is.available();
		System.out.println("count is:"+count);
		byte[] dataBuf = new byte[count];
		dis = new DataInputStream(is);
		int hasReadLen = dis.read(dataBuf, 0, count);
		if(hasReadLen == -1){
			return false;
		}
		lang_file_content = new String(dataBuf);
		//System.out.println(lang_file_content);
		return true;
	}
	public String getLangFileContent(){
		return lang_file_content;
	}
	public String getValue(String key){
		String value = FindString.findValue(lang_file_content, key);
		if(value.equals("No Find Text")){
			return key;
		}
		//System.out.println("Start get:"+key+"  "+value);
		return value;
	}
}
