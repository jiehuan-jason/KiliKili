package top.jiehuan.kilikili.util;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.io.HttpsConnection;

import top.jiehuan.kilikili.Exception.ErrorVideoStatusException;
import top.jiehuan.kilikili.Exception.WebReturnErrorCodeException;

/**
 * URLget is a class about get the WEB pages code(info) and deal with the info.
 * 
 * @since 0.1.0
 * @author jiehuan, jiehuan233@outlook.com
 *
 */
public class URLget {
	static int maxHttpGetBytes = 60000;
	static int bufferZoneBytes = 3072;
	
	//KiliKili Server
	public static String IP_ADDRESS="localhost";
	public static String DOWNLOAD_ADDRESS=IP_ADDRESS;
	public static String SEARCH_URL="http://"+IP_ADDRESS+":3000/search?keyword=";
	public static String USER_INFO_URL="http://"+IP_ADDRESS+":3000/user?mid=";
	//public static String RCMD_URL="http://"+IP_ADDRESS+":3232";
	public static String GET_INFO_URL="http://"+IP_ADDRESS+":3000/view?";
	public static String GET_VIDEO_DOWNLOAD_LINK_URL="http://"+IP_ADDRESS+":2121/api/playurl?";
	public static String GET_USER_VIDEOS_URL="http://"+IP_ADDRESS+":3000/user/video?";
	public static String GET_VIDEOS_PAGE_LIST_URL="http://"+IP_ADDRESS+":3000/list?";
	public static String SEND_TRANSCODING_REQUEST_URL="http://"+DOWNLOAD_ADDRESS+":4000/api/download?";
	public static String GET_TRANSCODING_STATUS_URL="http://"+DOWNLOAD_ADDRESS+":4000/api/status?";
	public static String DOWNLOAD_TRANSCODING_VIDEO_URL="http://"+DOWNLOAD_ADDRESS+":4000/api/output/";
	
	//BiliBili Server
	public static String RCMD_URL = "http://localhost:3232/test";
	//public static String RCMD_URL="https://api.bilibili.com/x/web-interface/wbi/index/top/feed/rcmd";
	//public static String GET_INFO_URL="https://api.bilibili.com/x/web-interface/view?";
	//public static String GET_VIDEO_DOWNLOAD_LINK_URL="https://api.bilibili.com/x/player/playurl?"; //&qn=6&platform=html5&high_quality=1
	
	public static String BackVideoLink(String bvid,String cid) throws WebReturnErrorCodeException, IOException, ErrorVideoStatusException{
		String url = GET_VIDEO_DOWNLOAD_LINK_URL+"bvid="+bvid+"&cid="+cid+"&qn=6&platform=html5&high_quality=1";
		String content = BackWeb(url);
		System.out.println(content);
		if(content.startsWith("error")){
			return content;
		}else{
			return FindString.findValue(content,"url");
		}
	}
	 public static String BackWeb(String url) throws WebReturnErrorCodeException, IOException, ErrorVideoStatusException{
		 	DataInputStream dis =null;
	        InputStream inputStream = null;

    		HttpConnection connection = null;
    		HttpsConnection https_connection=null;
    		
    		String content="error";
	        try{
	        	if(url.startsWith("https")){
	        		int num=0;

	    	        System.gc();
		        	System.out.println("before open connection,free memory is:"+Runtime.getRuntime().freeMemory());
		        	System.out.println("open the connection :"+url);
		            // 打开连接 设置请求方式和请求类型
		        	https_connection = (HttpsConnection) Connector.open(url);
		        	https_connection.setRequestMethod(HttpsConnection.GET);
		        	https_connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8"); 
		            // 连接
		            num = https_connection.getResponseCode();
		            
		            // 输出返回的网页代码
		            System.out.println("get now");
		            System.out.println(num);
		            
		            
		            if(num==200){
		            	try{
			            	content = getInfoFromHttpsConnection(https_connection);
		            	}catch(IOException e1){
		            		e1.printStackTrace();
		            		throw e1;
		            	}
		            } else{
		            	throw new WebReturnErrorCodeException(num);
		            }
	        	}else{
	    	        int num=0;
	    	        
	    	        
	    	        System.gc();
		        	System.out.println("before open connection,free memory is:"+Runtime.getRuntime().freeMemory());
		        	System.out.println("open the connection :"+url);
		            // 打开连接 设置请求方式和请求类型
		            connection = (HttpConnection) Connector.open(url);
		            connection.setRequestMethod(HttpConnection.GET);
		            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8"); 
		            // 连接
		            num = connection.getResponseCode();
		            
		            // 输出返回的网页代码
		            System.out.println("get now");
		            System.out.println(num);
		            
		            
		            if(num==200){
		            	try{
			            	content = getInfoFromHttpConnection(connection);
		            	}catch(IOException e1){
		            		e1.printStackTrace();
		            		throw e1;
		            	}
		            } else{
		            	throw new WebReturnErrorCodeException(num);
		            }
	        	}
	        }catch(IOException e){
	        	throw e;
	        }finally{
	        	if(inputStream!=null)
					inputStream.close();
				if(dis!=null)
					dis.close();
	        	if(url.startsWith("https"))
		        	// 关闭连接
					https_connection.close();
	        	else
	        		connection.close();
	        }
	        System.out.println("after open connection,free memory is:"+Runtime.getRuntime().freeMemory());
	        if(!((getAPIBackCode(content) == 0 )||(getAPIBackCode(content) == 1 ))){
	        	System.out.println("URLget: get api code error = "+getAPIBackCode(content));
	        	throw new ErrorVideoStatusException(getAPIBackCode(content),content);
	        }
	        System.out.println("URLget:return successfully");
	        return content;
		}
	 private static String getInfoFromHttpConnection(HttpConnection connection) throws UnsupportedEncodingException, IOException{
		 DataInputStream dis =connection.openDataInputStream();
		 int connectionLength = (int)connection.getLength();
		 return getInfoFromConnection(dis,connectionLength);
	 }
	 private static String getInfoFromHttpsConnection(HttpsConnection connection) throws UnsupportedEncodingException, IOException{
		 DataInputStream dis =connection.openDataInputStream();
		 int connectionLength = (int)connection.getLength();
		 return getInfoFromConnection(dis,connectionLength);
	 }
	 	private static String getInfoFromConnection(DataInputStream dis, int connectionLength) throws UnsupportedEncodingException, IOException{ 	
        	if(connectionLength!=-1){
        		byte[] buffer = new byte[bufferZoneBytes]; // 缓冲区
                int bytesRead = 0;
                int totalBytesRead = 0;
                StringBuffer webPageBuffer = new StringBuffer();
                System.out.println("begin to read data");
                // 逐块读取数据
                while ((bytesRead = dis.read(buffer)) != -1) {
                    if (totalBytesRead + bytesRead > maxHttpGetBytes) {
                        bytesRead = maxHttpGetBytes - totalBytesRead; // 只读取剩余的字节
                        System.out.println("reading...");
                    }
                    webPageBuffer.append(new String(buffer, 0, bytesRead,"UTF-8"));
                    totalBytesRead += bytesRead;
                    
                    if (totalBytesRead >= maxHttpGetBytes) {
                        break; // 达到最大字节数，停止读取
                    }
                }
                System.out.println(webPageBuffer.toString()); 
                return webPageBuffer.toString();    // 输出获取到的内容
        	}else{
        		System.out.println("length==-1");
        		ByteArrayOutputStream bs=new ByteArrayOutputStream();
        		int ch = 0;
        		while((ch=dis.read())!=-1){
        			bs.write(ch);
        		}
        		bs.close();        	
        		return new String(bs.toByteArray(),"UTF-8");
        	}
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
	    
	    
	     private static int getAPIBackCode(String content){
	    	return Integer.parseInt(FindString.findValueInt(content, "code"));
	    }
}