package top.jiehuan.kilikili;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

public class URLget {
	static int maxBytes = 10240;
	public static String IP_ADDRESS="localhost";
	public static String SEARCH_URL="http://"+IP_ADDRESS+":3000/search?keyword=";
	public static String USER_INFO_URL="http://"+IP_ADDRESS+":3000/user?mid=";
	public static String RCMD_URL="http://"+IP_ADDRESS+":3232";
	
	public static String[] sendGetRequest(String bvid) {
	        HttpConnection connection = null;
	        DataInputStream dis =null;
	        
	        InputStream inputStream = null;
	        
	        int num=0;

	        try {
	            // 打开连接 设置请求方式和请求类型
	            connection = (HttpConnection) Connector.open("http://"+IP_ADDRESS+":3000/view?bvid="+bvid);
	            connection.setRequestMethod(HttpConnection.GET);
	            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8"); 
	            
	            // 连接
	            num = connection.getResponseCode();
	            
	            // 输出返回的网页代码
	            System.out.println("get now");
	            System.out.println(num);
	            
	            if(num==200){
	            	dis=connection.openDataInputStream();
	            	int length = (int)connection.getLength();
	            	String str=null;
	            	
	            	if(length!=-1){
	            		byte[] buffer = new byte[2048]; // 1KB 缓冲区
	                    int bytesRead = 0;
	                    int totalBytesRead = 0;
	                    StringBuffer jsonPart = new StringBuffer();

	                    // 逐块读取数据
	                    while ((bytesRead = dis.read(buffer)) != -1) {
	                        if (totalBytesRead + bytesRead > maxBytes) {
	                            bytesRead = maxBytes - totalBytesRead; // 只读取剩余的字节
	                            System.out.println("reading...");
	                        }
	                        jsonPart.append(new String(buffer, 0, bytesRead,"UTF-8"));
	                        totalBytesRead += bytesRead;
	                        
	                        if (totalBytesRead >= maxBytes) {
	                            break; // 达到最大字节数，停止读取
	                        }
	                    }
	                    System.out.println(jsonPart.toString()); // 输出获取到的内容
	                    str=jsonPart.toString();
	            	}else{
	            		System.out.println("length==-1");
	            		ByteArrayOutputStream bs=new ByteArrayOutputStream();
	            		int ch = 0;
	            		while((ch=dis.read())!=-1){
	            			bs.write(ch);
	            		}
	            		str=new String(bs.toByteArray(),"UTF-8");
	            		bs.close();
	            	}
	            	// 关闭HTTP连接
	            	try {
						connection.close();
					} catch (IOException e1) {
						return new String[]{"error",e1.getMessage()};
					}
	            	// 关闭DataInputStream
		        	if(dis!=null){
		        		try{
		        			dis.close();
		        		}catch(Exception e){
		        			return new String[]{"error",e.getMessage()};
		        		}
		        	}
		        	// 打印获取到的bilibili返回代码，并检查是否返回错误代码
		        	String code = FindString.findValueInt(str,"code");
		        	System.out.println("code:"+code);
		        	if(code.equals("-400")){
		        		return new String[]{"error","No This BVID."};
		        	}
	            	return new String[]{"ok",str};
	            }
	        }catch(Exception e){
	        	e.printStackTrace();
	        	return new String[]{"error",e.getMessage()};
	        }finally{
	        	// 关闭HTTP连接
	        	try {
					connection.close();
				} catch (IOException e1) {
					return new String[]{"error",e1.getMessage()};
				}
	        	// 关闭inputStream连接
	        	if(inputStream!=null){
	        		try{
	        			inputStream.close();
	        		}catch(Exception e){
	        			return new String[]{"error",e.getMessage()};
	        		}	
	        	}
	        	// 关闭DataInputStream连接
	        	if(dis!=null){
	        		try{
	        			dis.close();
	        		}catch(Exception e){
	        			return new String[]{"error"+e.getMessage()};
	        		}
	        	}
	        	
	        }
	        return new String[]{"error","back "+num};
	    }
	public static String BackVideoLink(String bvid,String cid){
		// 定义与连接有关的局部变量
		HttpConnection connection = null;
        DataInputStream dis =null;
        
        InputStream inputStream = null;
        
        int num=0;

        try {
        	// 打开连接 设置请求方式和请求类型
            connection = (HttpConnection) Connector.open("http://"+IP_ADDRESS+":2121/api/playurl?bvid="+bvid+"&cid="+cid);
            connection.setRequestMethod(HttpConnection.GET);
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                        
            // 连接
            num = connection.getResponseCode();
            
            // 输出返回的网页代码
            System.out.println("get now");
            System.out.println(num);
            
            if(num==200){
            	
            	// 初始化DataInputStream
            	dis=connection.openDataInputStream();
            	int length = (int)connection.getLength();
            	String str=null;
            	
            	if(length!=-1){
            		System.out.println("length!=-1");
            		byte[] incomingData = new byte[length];
            		dis.read(incomingData);
            		System.out.println("read over");
            		System.out.println("length: " + length);
            		str=new String(incomingData,"UTF-8");
            		System.out.println("Test OK");
            		System.out.println(str);
            	}else{
            		System.out.println("length==-1");
            		ByteArrayOutputStream bs=new ByteArrayOutputStream();
            		int ch = 0;
            		while((ch=dis.read())!=-1){
            			bs.write(ch);
            		}
            		str=new String(bs.toByteArray(),"UTF-8");
            		bs.close();
            	}
            	return FindString.findValue(str,"url"); // 直接返回在内容中找到的链接
            }
        }catch(Exception e){
        	e.printStackTrace();
        	return "error";
        }finally{
        	//此部分注释与sendGetRequest注释中的内容相同
        	try {
				connection.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				return "connection close error";
			}
        	if(inputStream!=null){
        		try{
        			inputStream.close();
        		}catch(Exception e){
        			return "inputStream close error";
        		}
        	}if(dis!=null){
        		try{
        			dis.close();
        		}catch(Exception e){
        			return "DataInputStream close error";
        		}
        	}
        	
        }
        return "back"+num;
            
    
	}
	 public static String BackWeb(String url){
			HttpConnection connection = null;
	        DataInputStream dis =null;
	        
	        InputStream inputStream = null;
	        
	        int num=0;

	        try {
	            // 打开连接
	        	
	            connection = (HttpConnection) Connector.open(url);
	            connection.setRequestMethod(HttpConnection.GET);
	            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
	           
	            num = connection.getResponseCode();
	            
	            System.out.println("get now");
	            System.out.println(connection.getResponseCode());
	            
	            if(num==200){

	            	dis=connection.openDataInputStream();
	            	int length = (int)connection.getLength();
	            	String str=null;
	            	
	            	if(length!=-1){
	            		System.out.println("length!=-1");
	            		byte[] incomingData = new byte[length];
	            		dis.read(incomingData);
	            		System.out.println("read over");
	            		System.out.println("length: " + length);
	            		System.gc();
	            		str=new String(incomingData,"UTF-8");
	            		System.out.println("Test OK");
	            		System.out.println(str);
	            	}else{
	            		System.out.println("length==-1");
	            		ByteArrayOutputStream bs=new ByteArrayOutputStream();
	            		int ch = 0;
	            		while((ch=dis.read())!=-1){
	            			bs.write(ch);
	            		}
	            		str=new String(bs.toByteArray(),"UTF-8");
	            		bs.close();
	            	}
	            	System.out.println(str);
	            	return str;
	            }
	        }catch(Exception e){
	        	e.printStackTrace();
	        	return "error";
	        }finally{
	        	try {
					connection.close();
				} catch (IOException e1) {
					return "connection close error";
				}
	        	if(inputStream!=null){
	        		try{
	        			inputStream.close();
	        		}catch(Exception e){
	        			return "inputStream close error";
	        		}
	        	}if(dis!=null){
	        		try{
	        			dis.close();
	        		}catch(Exception e){
	        			return "DataInputStream close error";
	        		}
	        	}
	        	
	        }
	        return "back"+num;
	            
	    
		}
}