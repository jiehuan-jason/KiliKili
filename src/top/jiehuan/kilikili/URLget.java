package top.jiehuan.kilikili;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Security;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

public class URLget {
	/*static {
        Security.addProvider(new BouncyCastleProvider());
    }*/
	
	String url;
	int maxBytes = 8192;
	
	public URLget(String url){
		this.url=url;
	}
	
	public String[] sendGetRequest() {
	        HttpConnection connection = null;
	        DataInputStream dis =null;
	        
	        InputStream inputStream = null;
	        StringBuffer response = new StringBuffer();
	        
	        int num=0;

	        try {
	            // ������
	            connection = (HttpConnection) Connector.open(url);
	            connection.setRequestMethod(HttpConnection.GET);
	            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
	            //connection.setRequestProperty("User-Agent", "Profile/MIDP-2.0 Configuration/CLDC-1.0");
	            
	            // ����
	            num = connection.getResponseCode();
	            
	            System.out.println("get now");
	            System.out.println(connection.getResponseCode());
	            
	            if(num==200){
	            	
	            	//inputStream=connection.openInputStream();
	            	dis=connection.openDataInputStream();
	            	int length = (int)connection.getLength();
	            	String str=null;
	            	
	            	if(length!=-1){
	            		/*int bufferSize = 1024; // ���� 1KB
	            		byte[] buffer = new byte[bufferSize];
	            		StringBuffer sb = new StringBuffer();
	            		int bytesRead;

	            		while ((bytesRead = inputStream.read(buffer)) != -1) {
	            		    sb.append(new String(buffer, 0, bytesRead, "UTF-8"));
	            		}

	            		str = sb.toString();*/
	            		System.out.println("length!=-1");
	            		/*byte[] incomingData = new byte[length];
	            		dis.read(incomingData);
	            		Runtime rt = Runtime.getRuntime(); 
	            		System.out.println("read over");
	            		System.out.println("length: " + length);
	            		System.out.println(rt.freeMemory());
	            		System.gc();
	            		str=new String(incomingData,"UTF-8");
	            		System.out.println("Test OK");*/
	            		//String jsonPart = reader.readJsonPart("file:///path/to/your/large.json", 10240);
	            		byte[] buffer = new byte[1024]; // 1KB ������
	                    int bytesRead = 0;
	                    int totalBytesRead = 0;
	                    StringBuffer jsonPart = new StringBuffer();

	                    // ����ȡ����
	                    while ((bytesRead = dis.read(buffer)) != -1) {
	                        if (totalBytesRead + bytesRead > maxBytes) {
	                            bytesRead = maxBytes - totalBytesRead; // ֻ��ȡʣ����ֽ�
	                        }
	                        jsonPart.append(new String(buffer, 0, bytesRead,"UTF-8"));
	                        //System.out.print(new String(buffer, 0, bytesRead,"UTF-8"));
	                        totalBytesRead += bytesRead;
	                        
	                        if (totalBytesRead >= maxBytes) {
	                            break; // �ﵽ����ֽ�����ֹͣ��ȡ
	                        }
	                    }
	                    System.out.println(jsonPart.toString());
	                    str=jsonPart.toString();
	                
	            		//System.out.println(str);
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
	            	return new String[]{"ok",str};
	            }else if (connection.getResponseCode() == 307||connection.getResponseCode() == 302) {
	            	System.out.println("307 or 302 now");
	                String newUrl = connection.getHeaderField("Location"); // ��ȡ�µ� URL
	                URLget newu = new URLget(url+'/'+newUrl);
	                System.out.println(url+'/'+newUrl);
	                return newu.sendGetRequest(); // �ݹ鴦���ض���
	            }
	        }catch(Exception e){
	        	e.printStackTrace();
	        	return new String[]{"error",e.getMessage()};
	        }finally{
	        	try {
					connection.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					return new String[]{"error",e1.getMessage()};
				}
	        	if(inputStream!=null){
	        		try{
	        			inputStream.close();
	        		}catch(Exception e){
	        			return new String[]{"error",e.getMessage()};
	        		}
	        	}if(connection!=null){
	        		try{
	        			connection.close();
	        		}catch(Exception e){
	        			return new String[]{"error"+e.getMessage()};
	        		}
	        	}
	        	
	        }
	        return new String[]{"error","back "+num};
	            // �����Ӧ��
	            /*int responseCode = connection.getResponseCode();
	            if (responseCode == HttpConnection.HTTP_OK) {
	                inputStream = connection.openInputStream();
	                int ch;
	                while ((ch = inputStream.read()) != -1) {
	                    response.append((char) ch);
	                }
	                String jsonResponse = response.toString();
	                return "200 OK "+jsonResponse;
	               } else {
	                return "Error: " + responseCode;
	            }
	        } catch (IOException e) {
	            return "IOException: " + e.getMessage();
	        } finally {
	            // �ر�����
	            try {
	                if (inputStream != null) {
	                    inputStream.close();
	                }
	                if (connection != null) {
	                    connection.close();
	                }
	            } catch (IOException e) {
	                return "Error closing connection: " + e.getMessage();
	            }
	        }*/
	    }
	public static String findValue(String jsonString, String findText) {
	    // �ҵ�ָ���ֶε�����
	    int titleIndex = jsonString.indexOf("\"" + findText + "\"");
	    if (titleIndex == -1) {
	        System.out.println("No Find Text");
	        return null; // ���û���ҵ�ָ���ֶΣ����� null
	    }

	    // �ҵ���һ��˫���ŵ�λ��
	    int firstQuoteIndex = jsonString.indexOf("\"", titleIndex + findText.length() + 2); // +2 ��Ϊ�������ֶ����ͺ��������
	    if (firstQuoteIndex == -1) {
	        System.out.println("No Find first");
	        return null; // ���û���ҵ���һ��˫���ţ����� null
	    }

	    // �ҵ��ڶ���˫���ŵ�λ��
	    int secondQuoteIndex = jsonString.indexOf("\"", firstQuoteIndex + 1);
	    if (secondQuoteIndex == -1) {
	        System.out.println("No Find Second");
	        return null; // ���û���ҵ��ڶ���˫���ţ����� null
	    }

	    // ��ȡ������ָ���ֶε�ֵ
	    return jsonString.substring(firstQuoteIndex + 1, secondQuoteIndex);
	}
	
	
	public static String findValueInt(String jsonString, String findText){
		int titleIndex = jsonString.indexOf("\"" + findText + "\"");
	    if (titleIndex == -1) {
	        System.out.println("No Find Text");
	        return null; // ���û���ҵ�ָ���ֶΣ����� null
	    }

	    // �ҵ���һ��˫���ŵ�λ��
	    int firstQuoteIndex = jsonString.indexOf(":", titleIndex + findText.length() + 2); // +2 ��Ϊ�������ֶ����ͺ��������
	    if (firstQuoteIndex == -1) {
	        System.out.println("No Find first");
	        return null; // ���û���ҵ���һ��˫���ţ����� null
	    }

	    // �ҵ��ڶ���˫���ŵ�λ��
	    int secondQuoteIndex = jsonString.indexOf(",", firstQuoteIndex + 1);
	    if (secondQuoteIndex == -1) {
	    	secondQuoteIndex = jsonString.indexOf("}", firstQuoteIndex + 1);
	    	if(secondQuoteIndex==-1){
	    		System.out.println("No Find Second");
	    		return null; // ���û���ҵ��ڶ���˫���ţ����� null
	    	}
	    }

	    // ��ȡ������ָ���ֶε�ֵ
	    return jsonString.substring(firstQuoteIndex + 1, secondQuoteIndex);
	}
}

