package top.jiehuan.kilikili.util;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.io.HttpsConnection;
import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordStoreException;

import top.jiehuan.kilikili.Exception.ErrorVideoStatusException;
import top.jiehuan.kilikili.Exception.WebReturnErrorCodeException;
import top.jiehuan.kilikili.Model.WebModel;

/**
 * URLget is a class about get the WEB pages code(info) and deal with the info.
 * 
 * @since 0.1.0
 * @author jiehuan, jiehuan233@outlook.com
 *
 */
public class URLget {
	final static int MAX_HTTP_GET_BYTES = 1024*1024;
	final static int bufferZoneBytes = 4096;
	
	//KiliKili Server
	public static final String IP_ADDRESS="localhost";
	public static final String GET_USER_VIDEOS_URL="http://"+IP_ADDRESS+":3000/user/video?";
	public static final String GET_CorrespondPath_TIMESTAMP_URL = "http://"+IP_ADDRESS+":3000/timestamp";
	public static final String GET_UUID_URL = "http://"+IP_ADDRESS+":3000/uuid";
	//public static final String SEARCH_URL="http://"+IP_ADDRESS+":3000/search?keyword=";
	//public static final String USER_INFO_URL="http://"+IP_ADDRESS+":3000/user?mid=";
	//public static final String RCMD_URL="http://"+IP_ADDRESS+":3232";
	//public static final String GET_INFO_URL="http://"+IP_ADDRESS+":3000/view?";
	//public static final String GET_VIDEO_DOWNLOAD_LINK_URL="http://"+IP_ADDRESS+":2121/api/playurl?";
	//public static final String GET_VIDEOS_PAGE_LIST_URL="http://"+IP_ADDRESS+":3000/list?";
	//public static final String GET_QRCODE_URL="http://"+IP_ADDRESS+":3232/login";
	//public static final String GET_QRCODE_LOGIN_STATUS="http://"+IP_ADDRESS+":3232/lstatus?";
	
	//KiliKili Video Server
	public static final String DOWNLOAD_ADDRESS=IP_ADDRESS;
	public static final String SEND_TRANSCODING_REQUEST_URL="http://"+DOWNLOAD_ADDRESS+":4000/api/download?";
	public static final String GET_TRANSCODING_STATUS_URL="http://"+DOWNLOAD_ADDRESS+":4000/api/status?";
	public static final String DOWNLOAD_TRANSCODING_VIDEO_URL="http://"+DOWNLOAD_ADDRESS+":4000/api/output/";
	
	
	//BiliBili Server
	public static final String BILIBILI_MAIN_URL = "https://bilibili.com";
	
	public static final String RCMD_URL = "https://api.bilibili.com/x/web-interface/wbi/index/top/feed/rcmd";
	public static final String SEARCH_URL = "https://api.bilibili.com/x/web-interface/wbi/search/type";
	public static final String USER_INFO_URL = "https://api.bilibili.com/x/web-interface/card?mid=";
	public static final String GET_VIDEOS_PAGE_LIST_URL="https://api.bilibili.com/x/player/pagelist?";
	public static final String GET_INFO_URL = "https://api.bilibili.com/x/web-interface/view?";
	public static final String GET_VIDEO_DOWNLOAD_LINK_URL = "https://api.bilibili.com/x/player/playurl?"; //&qn=6&platform=html5&high_quality=1
	public static final String GET_QRCODE_URL = "https://passport.bilibili.com/x/passport-login/web/qrcode/generate";
	public static final String GET_QRCODE_LOGIN_STATUS = "https://passport.bilibili.com/x/passport-login/web/qrcode/poll?";
	public static final String GET_PERSONAL_INFO_URL = "https://api.bilibili.com/x/member/web/account";
	public static final String GET_LOGIN_COOKIES_STATUS_URL = "https://passport.bilibili.com/x/passport-login/web/cookie/info";
	public static final String GET_CSRF_REFRESH_TOKEN_STATUTS_URL = "https://passport.bilibili.com/x/passport-login/web/cookie/info";
	public static final String GET_REFRESH_CSRF_URL = "https://www.bilibili.com/correspond/1/";
	public static final String GET_VIDEO_LIKE_STATUS_URL = "https://api.bilibili.com/x/web-interface/archive/has/like";
	public static final String GET_VIDEO_FAVORITE_STATUS_URL = "https://api.bilibili.com/x/v2/fav/video/favoured";
	public static final String GET_VIDEO_COIN_STATUS_URL = "https://api.bilibili.com/x/web-interface/archive/coins";
	public static final String GET_BUVID3_URL = "https://api.bilibili.com/x/frontend/finger/spi";
	public static final String GET_BUVID_FP_URL = "https://api.bilibili.com/x/frontend/finger/fpfmc";
	public static final String GET_DYNAMIC_INFO_URL = "https://api.bilibili.com/x/polymer/web-dynamic/v1/detail";
	public static final String GET_FAV_FOLDER_INFO_URL = "https://api.bilibili.com/x/v3/fav/folder/info";
	public static final String GET_USER_ALL_FAV_FOLDERS_URL = "https://api.bilibili.com/x/v3/fav/folder/created/list-all";
	public static final String GET_FAV_LIST_URL = "https://api.bilibili.com/x/v3/fav/resource/list";
	public static final String GET_TO_VIEW_LIST_URL = "https://api.bilibili.com/x/v2/history/toview";
	public static final String GET_REPLY_LIST_URL = "https://api.bilibili.com/x/v2/reply";
	public static final String GET_SUB_REPLY_LIST_URL = "https://api.bilibili.com/x/v2/reply/reply";
	public static final String GET_HISTORY_LIST_URL = "https://api.bilibili.com/x/v2/history";
	
	//BILIBILI Server Post
	public static final String REFRESH_COOKIES_URL = "https://passport.bilibili.com/x/passport-login/web/cookie/refresh";
	public static final String REFRESH_COOKIES_COMFIRM_URL = "https://passport.bilibili.com/x/passport-login/web/confirm/refresh";
	public static final String EXCLIMBWUZHI_URL = "https://api.bilibili.com/x/internal/gaia-gateway/ExClimbWuzhi";
	//public static final String LIKE_URL = "https://api.bilibili.com/x/web-interface/archive/like"; //由于接口问题，此URL暂时不被使用
	public static final String COIN_URL = "https://api.bilibili.com/x/web-interface/coin/add";
	public static final String FAVORITE_URL = "https://api.bilibili.com/x/v3/fav/resource/deal";
	public static final String LIKE_DYNAMIC_URL = "https://api.bilibili.com/x/dynamic/feed/dyn/thumb";
	public static final String TO_VIEW_ADD_URL = "https://api.bilibili.com/x/v2/history/toview/add";
	public static final String TO_VIEW_DEL_URL = "https://api.bilibili.com/x/v2/history/toview/del";
	public static final String LIKE_REPLY_URL = "https://api.bilibili.com/x/v2/reply/action";
	public static final String HEARTBEAT_URL = "https://api.bilibili.com/x/click-interface/web/heartbeat";
	
	
	public static String BackVideoLink(String bvid,String cid) throws WebReturnErrorCodeException, IOException, ErrorVideoStatusException{
		String url = GET_VIDEO_DOWNLOAD_LINK_URL+"bvid="+bvid+"&cid="+cid+"&qn=6&platform=html5&high_quality=1";
		String content = BackWeb(url);
		System.out.println(content);
		if(content.startsWith("error")){
			throw new ErrorVideoStatusException(-1,content);
		}else{
			return decodeUnicode(FindString.findValue(content,"url"));
		}
		
	}
	
	//Data Example : username=test&password=123456
	//Content-Type, application/x-www-form-urlencoded
	public static WebModel BackWebAndUserCookiesPost(String url, String postData) throws WebReturnErrorCodeException, IOException, InvalidRecordIDException, RecordStoreException {
    		CookiesUtils cookies_util = new CookiesUtils();
    		if(cookies_util.isTokenStored())
    			return BackWebAndUserCookiesPost(url, postData, cookies_util.loadToken());
    		else throw new WebReturnErrorCodeException(-1);
	}
	
	//Content-Type, application/x-www-form-urlencoded
	public static WebModel BackWebAndUserCookiesPost(String url, String postData, String cookies) throws WebReturnErrorCodeException, IOException {
		return BackWebAndUserCookiesPost(url,postData,cookies,1);
	}
	
	public static WebModel BackWebAndUserCookiesPost(String url, String postData, int content_type) throws WebReturnErrorCodeException, IOException, InvalidRecordIDException, RecordStoreException {
		CookiesUtils cookies_util = new CookiesUtils();
		if(cookies_util.isTokenStored())
			return BackWebAndUserCookiesPost(url, postData, cookies_util.loadToken(), content_type);
		else throw new WebReturnErrorCodeException(-1);
}
	
	public static WebModel BackWebAndUserCookiesPost(String url, String postData, String cookies, int content_type) throws WebReturnErrorCodeException, IOException {
	    DataInputStream dis = null;
	    InputStream inputStream = null;

	    //HttpsConnection https_connection = null;
	    HttpConnection connection = null;

	    String content = "error";
	    WebModel web = new WebModel();
	    try {
	        System.gc();
	        System.out.println("before open connection, free memory is: " + Runtime.getRuntime().freeMemory());
	        System.out.println("open the connection: " + url);

	        /*if (url.startsWith("https")) {
	            https_connection = (HttpsConnection) Connector.open(url);
	            https_connection.setRequestMethod(HttpsConnection.POST);
	            if(content_type == 1)
	            	https_connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	            else if(content_type == 2)
	            	https_connection.setRequestProperty("Content-Type", "application/json");
	            https_connection.setRequestProperty("Content-Length", String.valueOf(postData.length()));
	            https_connection.setRequestProperty("Referer", "https://www.bilibili.com/");
	            https_connection.setRequestProperty("Cookie", cookies);
	            
	            // 发送POST数据
	            OutputStream os = https_connection.openOutputStream();
	            os.write(postData.getBytes());
	            os.flush();

	            int num = https_connection.getResponseCode();
	            System.out.println("post now");
	            System.out.println(num);
	            web.code = num;

	            if (num == 200) {
	                String cookie = https_connection.getHeaderField("Set-Cookie");
	                if (cookie != null) {
	                    web.cookies = cookie;
	                    System.out.println("Stored cookie: " + cookie);
	                }
	                content = getInfoFromHttpsConnection(https_connection);
	                web.content = content;
	            } else {
	                throw new WebReturnErrorCodeException(num);
	            }
	        } else {*/
	            connection = (HttpConnection) Connector.open(url);
	            connection.setRequestMethod(HttpConnection.POST);
	            if(content_type == 1)
	            	connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	            else
	            	connection.setRequestProperty("Content-Type", "application/json");
	            connection.setRequestProperty("Content-Length", String.valueOf(postData.length()));
	            connection.setRequestProperty("Referer", "https://www.bilibili.com/");
	            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/135.0.0.0 Safari/537.36 Edg/135.0.0.0");
	            connection.setRequestProperty("Cookie", cookies);

	            // 发送POST数据
	            OutputStream os = connection.openOutputStream();
	            os.write(postData.getBytes());
	            os.flush();

	            int num = connection.getResponseCode();
	            System.out.println("post now");
	            System.out.println(num);
	            web.code = num;
	            
	            

	            if (num == 200) {
	                String cookie = connection.getHeaderField("Set-Cookie");
	                if (cookie != null) {
	                    web.cookies = cookie;
	                    System.out.println("Stored cookie: " + cookie);
	                }
	                content = getInfoFromHttpConnection(connection);
	                web.content = content;
	            } else {
	                throw new WebReturnErrorCodeException(num);
	            }
	        //}
	    } catch (IOException e) {
	        throw e;
	    } finally {
	        if (inputStream != null) inputStream.close();
	        if (dis != null) dis.close();
	        //if (url.startsWith("https") && https_connection != null) https_connection.close();
	        //if (!url.startsWith("https") && connection != null) 
	        connection.close();
	    }

	    System.out.println("after post connection, free memory is: " + Runtime.getRuntime().freeMemory());
	    System.out.println("URL POST:return successfully");
	    return web;
	}

	public static WebModel BackWebWithMoreInfo(String url)throws WebReturnErrorCodeException, IOException{
		try{
    		CookiesUtils cookies_util = new CookiesUtils();
    		if(cookies_util.isTokenStored())
    			return BackWebWithMoreInfo(url, cookies_util.loadToken(), 1);
    	}catch(Exception e){
    		//什么都不做
    	}
		return BackWebWithMoreInfo(url, "", 1);
	}
	
	public static WebModel BackWebWithMoreInfo(String url, String cookies, int type)throws WebReturnErrorCodeException, IOException{
		DataInputStream dis =null;
        InputStream inputStream = null;

		HttpConnection connection = null;
		//HttpsConnection https_connection=null;
		
		WebModel web = new WebModel();
		
        try{
    	        int num=0;
    	        
    	        System.gc();
	        	System.out.println("before open connection,free memory is:"+Runtime.getRuntime().freeMemory());
	        	System.out.println("open the connection :"+url);
	            // 打开连接 设置请求方式和请求类型
	            connection = (HttpConnection) Connector.open(url);
	            connection.setRequestMethod(HttpConnection.GET);
	            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8"); 
	            if(type == 1)
	            	connection.setRequestProperty("Referer", "https://www.bilibili.com/");
	            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/135.0.0.0 Safari/537.36 Edg/135.0.0.0");
	            connection.setRequestProperty("Cookie", cookies);
	        	
	            // 连接
	            num = connection.getResponseCode();
	            
	            // 输出返回的网页代码
	            System.out.println("get now");
	            System.out.println("Num:"+num);
	            web.code = num;
	            System.out.println(connection.getHeaderField("Set-Cookie"));
	            
	            if(num==200){
	            	try{
	            		String cookie = connection.getHeaderField("Set-Cookie");
		                if (cookie != null) {
		                    web.cookies = cookie;
		                    System.out.println("Stored cookie: " + cookie);
		                }
		            	web.content = getInfoFromHttpConnection(connection);
	            	}catch(IOException e1){
	            		e1.printStackTrace();
	            		throw e1;
	            	}
	            }else if(num == 301 || num == 302){
	            	String location = connection.getHeaderField("Location");
	            	String cookiesNew = connection.getHeaderField("Set-Cookie");
	            	if(cookiesNew == null)
	            		cookiesNew = "";
	                if (location != null) {
	                	return BackWebWithMoreInfo(location, cookiesNew, 2);
	                }
	            }
	            else{
	            	throw new WebReturnErrorCodeException(num);
	            }
	            
	           
        	//}
        }catch(IOException e){
        	throw e;
        }finally{
        	if(inputStream!=null)
				inputStream.close();
			if(dis!=null)
				dis.close();
        	/*if(url.startsWith("https")&&https_connection!=null)
	        	// 关闭连接
				https_connection.close();
        	else if(connection!=null)*/
        		connection.close();
        }
        System.out.println("after open connection,free memory is:"+Runtime.getRuntime().freeMemory());
        System.out.println("URLget:return successfully");
        return web;
	}
	
	//GET
	 public static String BackWeb(String url) throws WebReturnErrorCodeException, IOException, ErrorVideoStatusException{
		 	return BackWebWithMoreInfo(url).content;
	}
	 
	 private static String getInfoFromHttpConnection(HttpConnection connection) throws UnsupportedEncodingException, IOException{
		 DataInputStream dis =connection.openDataInputStream();
		 int connectionLength = (int)connection.getLength();
		 return getInfoFromConnection(dis,connectionLength);
	 }
	 
	/* private static String getInfoFromHttpsConnection(HttpsConnection connection) throws UnsupportedEncodingException, IOException{
		 DataInputStream dis =connection.openDataInputStream();
		 int connectionLength = (int)connection.getLength();
		 return getInfoFromConnection(dis,connectionLength);
	 }*/
	 
	 private static String getInfoFromConnection(DataInputStream dis, int connectionLength) 
		        throws IOException {
		    ByteArrayOutputStream baos = new ByteArrayOutputStream();
		    byte[] buffer = new byte[bufferZoneBytes]; // 缓冲区大小建议为1KB或4KB
		    int bytesRead;
		    int totalBytesRead = 0;

		    while ((bytesRead = dis.read(buffer)) != -1) {
		        // 检查是否超过最大限制
		        if (totalBytesRead + bytesRead > MAX_HTTP_GET_BYTES) {
		            bytesRead = MAX_HTTP_GET_BYTES - totalBytesRead;
		            if (bytesRead <= 0) {
		                break; // 达到限制，停止读取
		            }
		        }
		        baos.write(buffer, 0, bytesRead);
		        totalBytesRead += bytesRead;
		        if (totalBytesRead >= MAX_HTTP_GET_BYTES) {
		            break; // 防止最后一次读取后超出限制
		        }
		    }
		    // 使用正确的字符编码转换字节数据
		    return new String(baos.toByteArray(), "UTF-8");
		}
	    static public String urlEncode(String text) {
	        StringBuffer encoded = new StringBuffer();
	        try {
	            byte[] bytes = text.getBytes("UTF-8"); // 获取 UTF-8 编码的字节数组
	            for (int i = 0; i < bytes.length; i++) {
	                int b = bytes[i] & 0xFF; // 将字节转换为无符号整数
	                if (b == 32) { // 空格转换为 "+"
	                    encoded.append("+");
	                } else if (b < 128) { // ASCII 字符直接添加
	                    encoded.append((char) b);
	                } else { // 其他字符转换为 URL 编码格式
	                    encoded.append("%");
	                    String hex = Integer.toHexString(b);
	                    if (hex.length() == 1) {
	                        encoded.append("0"); // 确保两位十六进制
	                    }
	                    encoded.append(hex.toUpperCase()); // 转换为大写
	                }
	            }
	        } catch (java.io.UnsupportedEncodingException e) {
	            e.printStackTrace();
	        }
	        return encoded.toString();
	    }
	    
	    static public String urlEncodeWithoutCookiesChars(String text) {
	        StringBuffer encoded = new StringBuffer();
	        try {
	            byte[] bytes = text.getBytes("UTF-8");
	            for (int i = 0; i < bytes.length; i++) {
	                int b = bytes[i] & 0xFF;
	                if (b < 128 && b != '&' && b != '=' && b != ';') {
	                    // ASCII 且不是 & 或 =，直接添加
	                    encoded.append((char) b);
	                } else if (b == '&' || b == '=' || b == ';') {
	                    // 保留 & 和 =
	                    encoded.append((char) b);
	                } else {
	                    // 其他字符转为 %XX
	                    encoded.append("%");
	                    String hex = Integer.toHexString(b);
	                    if (hex.length() == 1) {
	                        encoded.append("0");
	                    }
	                    encoded.append(hex.toUpperCase());
	                }
	            }
	        } catch (java.io.UnsupportedEncodingException e) {
	            e.printStackTrace();
	        }
	        return encoded.toString();
	    }

	    
	    //Deepseek编写
	    public static String decodeUnicode(String encodedUrl) {
	        StringBuffer decodedUrl = new StringBuffer();
	        char[] chars = encodedUrl.toCharArray();
	        int length = chars.length;
	        int i = 0;

	        while (i < length) {
	            // 检查当前字符是否为转义起始符
	            if (chars[i] == '\\' && (i + 1 < length) && chars[i + 1] == 'u') {
	                try {
	                    // 提取十六进制编码部分（4位）
	                    String hex = new String(chars, i + 2, 4);
	                    // 将十六进制转换为字符
	                    char decodedChar = (char) Integer.parseInt(hex, 16);
	                    decodedUrl.append(decodedChar);
	                    // 跳过已处理的6个字符
	                    i += 6;
	                } catch (Exception e) {
	                    // 格式错误时保留原始字符
	                    decodedUrl.append(chars[i]);
	                    i++;
	                }
	            } else {
	                // 直接追加非转义字符
	                decodedUrl.append(chars[i]);
	                i++;
	            }
	        }
	        return decodedUrl.toString();
	    }
	    
	     public static int getAPIBackCode(String content){
	    	return Integer.parseInt(FindString.findValueInt(content, "code"));
	    }
}