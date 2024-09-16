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
	String desc;
	StringItem info;
	StringItem ln;
	String pic;
	
	//StringItem link;
	public String bvid;
	Image image;
	Command back;
	Command exit;
	Command download;
	Command view_cover;
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
		URLget video_info = new URLget("http://220.231.146.67:3000/view?bvid="+bvid);
		String[] s_info= video_info.sendGetRequest();
		String title = null;
		System.out.println("status:"+s_info[0]);
		if(s_info[0].equals("error")){
			form=new Form("未找到该视频");
			back=new Command("Back",Command.BACK,1);
			exit=new Command("Exit",Command.EXIT,0);
			form.addCommand(back);
			form.addCommand(exit);
			form.setCommandListener(this);
			Alert alert = new Alert("Error", s_info[1], null, AlertType.ERROR);
            alert.setTimeout(Alert.FOREVER); // 设置为永远显示，直到用户操作
            display.setCurrent(alert, form);
            ml.display.setCurrent(ml.form);
            
		}else{
			//title=get_Title(s_info[1]);
			title=URLget.findValue(s_info[1],"title");
			string=new StringItem(null, title);
			up_name=new StringItem(null,"\nup主："+URLget.findValue(s_info[1], "name"));
			desc = "\n简介:"+URLget.findValueInt(s_info[1],"desc").substring(1, URLget.findValueInt(s_info[1],"desc").length() - 1);
			/*view = new StringItem(null,"\n看"+URLget.findValueInt(s_info[1],"view")+"次");
			reply = new StringItem(null,"\n回"+URLget.findValueInt(s_info[1],"reply")+"条");
			coin = new StringItem(null,"\n币"+URLget.findValueInt(s_info[1],"coin")+"个  ");
			share = new StringItem(null,"转"+URLget.findValueInt(s_info[1],"share")+"次  ");
			like = new StringItem(null,"赞"+URLget.findValueInt(s_info[1],"like")+"次");*/
			info = new StringItem(null,"\n看"+URLget.findValueInt(s_info[1],"view")+"次  "+"回"+URLget.findValueInt(s_info[1],"reply")+"条  "+"币"+URLget.findValueInt(s_info[1],"coin")+"个  "+"转"+URLget.findValueInt(s_info[1],"share")+"次  "+"赞"+URLget.findValueInt(s_info[1],"like")+"次");
			cid=URLget.findValueInt(s_info[1], "cid");
			pic=URLget.findValue(s_info[1], "pic");
			System.out.println(cid);
			video_url=URLget.BackVideoLink(bvid, cid);
			System.out.println("Get Already");
			//link=new StringItem(null,URLget.BackVideoLink(bvid, cid));
			download=new Command("Download",Command.ITEM,1);
			back=new Command("Back",Command.BACK,1);
			exit=new Command("Exit",Command.EXIT,0);
			view_cover=new Command("View the Cover",Command.ITEM,2);
			//loadImage(URLget.findValue(s_info[1], "name"));
			form=new Form("视频界面");
			form.append(string);
			//form.append(new StringItem(null,""));
			form.append(up_name);
			//form.append(new StringItem(null,""));
			if(!desc.equals("\n简介:")){
				Display_Desc(desc);
				form.append(new StringItem(null,""));
			}
			
			
			//form.append(desc);
			/*form.append(view);
			form.append(like);
			form.append(coin);
			form.append(share);
			form.append(reply);*/
			form.append(info);
			//form.append(link);
			form.addCommand(back);
			form.addCommand(exit);
			form.addCommand(download);
			form.addCommand(view_cover);
			form.setCommandListener(this);
			//loadImage(URLget.findValue(s_info[1], "pic"));
			display.setCurrent(form);
		}
		
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
                    		//video_url=URLget.BackVideoLink(bvid, cid);
                    		System.out.println("video_url is:"+video_url);
							ml.platformRequest(video_url);
                    		//ml.platformRequest("https://upos-sz-mirrorcos.bilivideo.com/upgcxcode/06/20/25713772006/25713772006-1-16.mp4?e=ig8euxZM2rNcNbRVhwdVhwdlhWdVhwdVhoNvNC8BqJIzNbfq9rVEuxTEnE8L5F6VnEsSTx0vkX8fqJeYTj_lta53NCM=&uipk=5&nbs=1&deadline=1725792100&gen=playurlv2&os=cosbv&oi=0&trid=c10c84668d634510942836e973834cdah&mid=0&platform=html5&og=cos&upsig=9a152d6fb408ded68e653656e73f3404&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform,og&bvc=vod&nettype=0&f=h_0_0&bw=39783&logo=80000000");
						} catch (ConnectionNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} /*catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}*/
                    	
                    }
	            }).start();
	        }
	        if(c==view_cover){
	        	new Thread(new Runnable() {
                    public void run() {
                    	try {
                    		//video_url=URLget.BackVideoLink(bvid, cid);
                    		System.out.println("cover_url is:"+pic);
							ml.platformRequest(new String(pic.getBytes("UTF-8"),"UTF-8"));
						} catch (ConnectionNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                    	
                    }
	            }).start();
	        }
	    }
	 
	 
	 private void Display_Desc(String str){
		 int count = 0;
	        int start = 0;
	        int end = 0;

	        String delimiter="\\n";
			// 计算分割后的字符串数量
	        while ((end = str.indexOf(delimiter, start)) != -1) {
	            count++;
	            start = end + 2;
	        }
	        count++; // 最后一个元素
	        System.out.println(count);
	        String[] result = new String[count];
	        start = 0;
	        int index = 0;

	        // 进行分割
	        while ((end = str.indexOf(delimiter, start)) != -1) {
	            result[index++] = str.substring(start, end);
	            start = end + 2;
	        }
	        result[index] = str.substring(start); // 添加最后一个元素
	        
	        
	        for (int i = 0; i < result.length; i++) {
	        	StringItem d=new StringItem(null,result[i]);
	        	form.append(d);
	            System.out.println(result[i]);
	        }
	        //return result;

	 }
	 

}