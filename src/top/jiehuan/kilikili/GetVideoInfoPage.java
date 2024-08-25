package top.jiehuan.kilikili;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.io.file.FileConnection;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.ImageItem;
import javax.microedition.lcdui.StringItem;
import javax.microedition.midlet.MIDlet;


public class GetVideoInfoPage implements CommandListener{

	public Display display;
	public Form form;
	public StringItem string;
	public StringItem up_name=null;
	StringItem view;
	StringItem reply;
	StringItem coin;
	StringItem share;
	StringItem like;
	//StringItem link;
	public String bvid;
	Image image;
	Command back;
	Command exit;
	Command download;
	private MainMIDlet ml;
	String video_url;
	String cid;
    /*public MainMIDlet(){
        display = Display.getDisplay(this);
        form = new Form("JSON Response");
        exitCommand = new Command("Exit", Command.EXIT, 1);
        form.addCommand(exitCommand);
        form.setCommandListener((CommandListener) this);
    }*/
	public GetVideoInfoPage(MainMIDlet midlet,String bvid){
		this.bvid=bvid;
		ml=midlet;
		display = Display.getDisplay(midlet);
		URLget video_info = new URLget("http://192.168.1.2:3000/view?bvid="+bvid);
		String[] s_info= video_info.sendGetRequest();
		String title = null;
		if(s_info[0].equals("error")){
			title="error";
			string=new StringItem(null, s_info[1]);
			up_name=new StringItem(null,"null");
		}else{
			//title=get_Title(s_info[1]);
			title=URLget.findValue(s_info[1],"title");
			string=new StringItem(null, title);
			up_name=new StringItem(null,"\nup主："+URLget.findValue(s_info[1], "name"));
			view = new StringItem(null,"\n看"+URLget.findValueInt(s_info[1],"view")+"次");
			reply = new StringItem(null,"\n回"+URLget.findValueInt(s_info[1],"reply")+"条");
			coin = new StringItem(null,"\n币"+URLget.findValueInt(s_info[1],"coin")+"个  ");
			share = new StringItem(null,"转"+URLget.findValueInt(s_info[1],"share")+"次  ");
			like = new StringItem(null,"赞"+URLget.findValueInt(s_info[1],"like")+"次");
			cid=URLget.findValueInt(s_info[1], "cid");
			video_url=URLget.BackVideoLink(bvid, cid);
			//link=new StringItem(null,URLget.BackVideoLink(bvid, cid));
		}
		download=new Command("Download",Command.ITEM,1);
		back=new Command("Back",Command.BACK,1);
		exit=new Command("Exit",Command.EXIT,0);
		//loadImage(URLget.findValue(s_info[1], "name"));
		form=new Form("视频界面");
		form.append(string);
		form.append(up_name);
		form.append(view);
		form.append(like);
		form.append(coin);
		form.append(share);
		form.append(reply);
		//form.append(link);
		form.addCommand(back);
		form.addCommand(exit);
		form.addCommand(download);
		form.setCommandListener(this);
		//loadImage(URLget.findValue(s_info[1], "pic"));
		display.setCurrent(form);
	}
	 /*private void loadImage(final String url) {
	        new Thread(new Runnable() {
	            public void run() {
	                try {
	                    image = Image.createImage(url);
	                    // 创建 ImageItem 并添加到表单
	                    ImageItem imageItem = new ImageItem(null, image, ImageItem.LAYOUT_CENTER, null);
	                    form.append(imageItem);
	                    display.setCurrent(form); // 显示表单
	                } catch (IOException e) {
	                	System.out.println(e.getMessage());
	                    e.printStackTrace();
	                }
	            }
	        }).start();
	    }*/
	
	/*private String get_Title(String j_info){
		try {
			JSONObject data=new JSONObject(new JSONObject(j_info).getString("data"));
			return data.getString("title");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			try {
				if(new JSONObject(j_info).getString("data").toString().equals("-400")){
					return "找不到对应的视频";
				}
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return "get_Title error: "+e1.getMessage()+'\n'+j_info;
			}
			return "get_Title error: "+e.getMessage()+'\n'+j_info;
		}
	}*/
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
	        if(c==exit){
	        	ml.exitApp();
	        }
	        if(c==download){
	        	new Thread(new Runnable() {
                    public void run() {
                    	try {
							ml.platformRequest(new String(video_url.getBytes("UTF-8"),"UTF-8"));
						} catch (ConnectionNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                    	
                    	//downloadVideo("https://cn-xj-cm-02-05.bilivideo.com/upgcxcode/78/11/500001649511178/500001649511178-1-16.mp4?e=ig8euxZM2rNcNbRVhwdVhwdlhWdVhwdVhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1724553124&gen=playurlv2&os=bcache&oi=666712603&trid=00007d2be73e205948c4893fc65f0fbcd0c3u&mid=1798113850&platform=pc&og=cos&upsig=456cfd9c8fb03583bb6fecc534dead5e&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform,og&cdnid=61905&bvc=vod&nettype=0&orderid=0,3&buvid=6FB31018-8CDA-5F30-8740-45E9AFA32F3E38170infoc&build=0&f=u_0_0&agrr=1&bw=52434&logo=80000000","/root1/kilikili","test.mp4");
                    }
	            }).start();
	       }
	    }
	 
	 /*private void downloadVideo(String url, String folderPath, String newFileName) {
	        HttpConnection connection = null;
	        InputStream inputStream = null;
	        OutputStream outputStream = null;

	        try {
	            // 创建目标文件夹
	            FileConnection folder = (FileConnection) Connector.open("file://" + folderPath);
	            if (!folder.exists()) {
	                folder.mkdir(); // 创建文件夹
	            }
	            folder.close();

	            // 设置 HTTPS 连接
	            try{
	            	connection = (HttpConnection) Connector.open(url);
	            	connection.setRequestMethod(HttpConnection.GET);
	            	connection.setRequestProperty("Referer", "https://www.bilibili.com"); // 设置 Referer
	            }catch(javax.microedition.pki.CertificateException e){
	            	System.out.println(e.getMessage());
	            	System.out.println(e.getCertificate().toString());
	            	e.printStackTrace();
	            }
	            

	            // 检查响应
	            int responseCode = connection.getResponseCode();
	            if (responseCode == HttpConnection.HTTP_OK) {
	                inputStream = connection.openInputStream();
	                
	                // 创建文件输出流
	                FileConnection fileConnection = (FileConnection) Connector.open("file://" + folderPath + "/" + newFileName);
	                if (!fileConnection.exists()) {
	                    fileConnection.create(); // 创建文件
	                }
	                outputStream = fileConnection.openOutputStream();

	                // 下载文件
	                byte[] buffer = new byte[1024];
	                int bytesRead;
	                while ((bytesRead = inputStream.read(buffer)) != -1) {
	                    outputStream.write(buffer, 0, bytesRead);
	                }

	                // 完成下载
	                outputStream.close();
	                inputStream.close();
	                fileConnection.close();

	                Alert alert = new Alert("Success", "Video downloaded successfully!", null, AlertType.INFO);
	                display.setCurrent(alert, form);
	            } else {
	                Alert alert = new Alert("Error", "Failed to download video. Response code: " + responseCode, null, AlertType.ERROR);
	                display.setCurrent(alert, form);
	            }
	        } catch (Exception e) {
	            Alert alert = new Alert("Error", "IOException: " + e.getMessage(), null, AlertType.ERROR);
	            display.setCurrent(alert, form);
	            e.printStackTrace();
	            System.out.println(e.getMessage());
	        } finally {
	            try {
	                if (inputStream != null) inputStream.close();
	                if (outputStream != null) outputStream.close();
	                if (connection != null) connection.close();
	            } catch (IOException e) {
	                // 处理关闭流时的异常
	            }
	        }
	    }*/
	 
}
