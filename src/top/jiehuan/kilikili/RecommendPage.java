package top.jiehuan.kilikili;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.lcdui.*;

public class RecommendPage implements CommandListener {
	
	private MainMIDlet ml;
	List rcmd_list;
	Display display;
	Command back;
	Command exit;
	Command go;
	
	String[] titles;
	String[] bvids;
	
	public RecommendPage(MainMIDlet midlet){
		ml=midlet;
		display = Display.getDisplay(midlet);
		String rcmd_data=BackRCMDVideos();
		
		titles=extractContents(rcmd_data,"title");
		bvids=extractContents(rcmd_data,"bvid");
		
		rcmd_list=new List("推荐列表",List.IMPLICIT);
		
		for(int i=0;i<20;i++){
			System.out.println(titles[i]);
			System.out.println(bvids[i]);
			rcmd_list.append(titles[i], null);
		}
		back=new Command("Back",Command.BACK,1);
		go=new Command("Go",Command.OK,1);
		exit=new Command("Exit",Command.EXIT,0);
		rcmd_list.addCommand(back);
		rcmd_list.addCommand(exit);
		rcmd_list.setSelectCommand(go);
		rcmd_list.setCommandListener(this);
		display.setCurrent(rcmd_list);
	}
	
	public void commandAction(Command c, Displayable d) {
        if (c == back) {
        	
           
            new Thread(new Runnable() {
                public void run() {
                	ml.display.setCurrent(ml.form);
                }
            }).start();
            //GetVideoInfoPage secondMIDlet = new GetVideoInfoPage(text);
            //display.setCurrent(secondMIDlet.getForm());
            //notifyPaused();
        }
        else if(c==exit){
        	ml.exitApp();
        }else if(c==go){
        	new Thread(new Runnable() {
                public void run() {
                	String bvid = bvids[rcmd_list.getSelectedIndex()];
                    new GetVideoInfoPage(ml, bvid);
                }
            }).start();
        }
    }
	
	 public static String[] extractContents(String input, String keyword) {
	        //Vector resultVector = new Vector();  // 动态数组，用于存储匹配的内容
		    String[] resultArray = new String[40];
	        int keywordLength = keyword.length()+2;
	        int currentIndex = 0;
	        int num=0;
	        input="\""+input+"\"";

	        // 循环查找"xxx"字符串
	        while ((currentIndex = input.indexOf(keyword, currentIndex)) != -1) {
	            // 查找第一个双引号
	            int startQuote = input.indexOf("\"", currentIndex + keywordLength);
	            if (startQuote == -1) {
	                break;  // 如果没有找到双引号，退出循环
	            }

	            // 查找第二个双引号
	            int endQuote = input.indexOf("\"", startQuote + 1);
	            if (endQuote == -1) {
	                break;  // 如果没有找到结束双引号，退出循环
	            }

	            // 提取双引号内的内容
	            String content = input.substring(startQuote + 1, endQuote);
	            resultArray[num]=content;
	            num++;// 将内容存入Vector

	            // 更新currentIndex以查找下一个"xxx"
	            currentIndex = endQuote + 1;
	        }

	        // 将Vector中的内容转换为String数组

	        return resultArray;
	    }
	 
	 public static String BackRCMDVideos(){
			HttpConnection connection = null;
	        DataInputStream dis =null;
	        
	        InputStream inputStream = null;
	        //StringBuffer response = new StringBuffer();
	        
	        int num=0;

	        try {
	            // 打开连接
	            connection = (HttpConnection) Connector.open("http://localhost:3232");
	            connection.setRequestMethod(HttpConnection.GET);
	            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
	            //connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 7.1.1; OPPO R9sk) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.111 Mobile Safari/537.36");
	            
	            // 连接
	            num = connection.getResponseCode();
	            
	            System.out.println("get now");
	            System.out.println(connection.getResponseCode());
	            
	            if(num==200){
	            	
	            	//inputStream=connection.openInputStream();
	            	dis=connection.openDataInputStream();
	            	int length = (int)connection.getLength();
	            	String str=null;
	            	
	            	if(length!=-1){
	            		/*int bufferSize = 1024; // 例如 1KB
	            		byte[] buffer = new byte[bufferSize];
	            		StringBuffer sb = new StringBuffer();
	            		int bytesRead;

	            		while ((bytesRead = inputStream.read(buffer)) != -1) {
	            		    sb.append(new String(buffer, 0, bytesRead, "UTF-8"));
	            		}

	            		str = sb.toString();*/
	            		System.out.println("length!=-1");
	            		byte[] incomingData = new byte[length];
	            		dis.read(incomingData);
	            		Runtime rt = Runtime.getRuntime(); 
	            		System.out.println("read over");
	            		System.out.println("length: " + length);
	            		System.out.println(rt.freeMemory());
	            		System.gc();
	            		str=new String(incomingData,"UTF-8");
	            		System.out.println("Test OK");
	            		//String jsonPart = reader.readJsonPart("file:///path/to/your/large.json", 10240);
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
}
