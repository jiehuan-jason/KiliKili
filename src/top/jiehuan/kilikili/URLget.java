package top.jiehuan.kilikili;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

/**
 * URLget is a class about get the WEB pages code(info) and deal with the info.
 * 
 * @since 0.1.0
 * @author jiehuan, jiehuan233@outlook.com
 *
 */
public class URLget {
	static int maxHttpGetBytes = 40960;
	static int bufferZoneBytes = 2048;
	public static String IP_ADDRESS="localhost";
	public static String SEARCH_URL="http://"+IP_ADDRESS+":3000/search?keyword=";
	public static String USER_INFO_URL="http://"+IP_ADDRESS+":3000/user?mid=";
	public static String RCMD_URL="http://"+IP_ADDRESS+":3232";
	public static String GET_INFO_URL="http://"+IP_ADDRESS+":3000/view?";
	public static String GET_VIDEO_DOWNLOAD_LINK_URL="http://"+IP_ADDRESS+":2121/api/playurl?";
	
	public static String[] sendGetRequest(String bvid) {
	        String content = BackWeb(GET_INFO_URL+"bvid="+bvid+"&version="+AboutPage.version);
	        if(content.startsWith("error")){
	        	return new String[]{"error",content};
	        }
	        return new String[]{"ok",content};
	    }
	public static String BackVideoLink(String bvid,String cid){
		String url = GET_VIDEO_DOWNLOAD_LINK_URL+"bvid="+bvid+"&cid="+cid;
		System.out.println(url);
		String content = BackWeb(url);
		System.out.println(content);
		if(content.startsWith("error")){
			return content;
		}else{
			return FindString.findValue(content,"url");
		}
	}
	 public static String BackWeb(String url){
		 	HttpConnection connection = null;
	        DataInputStream dis =null;
	        
	        InputStream inputStream = null;
	        
	        int num=0;
	        String content="error";

	        try {
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
	            		content = "error"+e1.getMessage();
	            	}
	            }
	        }catch(Exception e){
	        	e.printStackTrace();
	        	return "error"+e.getMessage();
	        }finally{
	        	// 关闭HTTP连接
	        	try {
					connection.close();
				} catch (IOException e1) {
					return "error"+e1.getMessage();
				}
	        	// 关闭inputStream连接
	        	if(inputStream!=null){
	        		try{
	        			inputStream.close();
	        		}catch(Exception e){
	        			return "error"+e.getMessage();
	        		}	
	        	}
	        	// 关闭DataInputStream连接
	        	if(dis!=null){
	        		try{
	        			dis.close();
	        		}catch(Exception e){
	        			return "error"+e.getMessage();
	        		}
	        	}
	        	
	        }
	        if(num==200){
	        	return content;
	        }
	        return "error "+num;
	            
	    
		}
	 	private static String getInfoFromHttpConnection(HttpConnection connection) throws UnsupportedEncodingException, IOException{
	 		DataInputStream dis =connection.openDataInputStream();
        	int connectionLength = (int)connection.getLength();
        	
        	if(connectionLength!=-1){
        		byte[] buffer = new byte[bufferZoneBytes]; // 2KB 缓冲区
                int bytesRead = 0;
                int totalBytesRead = 0;
                StringBuffer webPageBuffer = new StringBuffer();

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
}