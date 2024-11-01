package top.jiehuan.kilikili;

import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;

public class DownloadPage implements CommandListener{
	
	public static String PageID = "5";
	
	private MainMIDlet ml;
	Display display;
	Form form;
	Command back;
	Command exit;
	Command download;
	TextField videoURL;
	StringItem tips;
	
	String video_url;
	String bvid;
	
	VideoInfo video_info;
	
	public DownloadPage(MainMIDlet midlet,VideoInfo video_info){
		this.video_info=video_info;
		ml=midlet;
		bvid = video_info.getBVID();
		display = Display.getDisplay(midlet);
		video_info.setPageNum(MainMIDlet.addPageNum(PageID,video_info));
		this.video_url=video_info.getVideoURL();
		videoURL = new TextField("",this.video_url,10000,TextField.ANY);
		tips = new StringItem("","tips:如果下载按钮无法下载，请把光标移到下方的链接处并复制到浏览器打开下载");
		download=new Command("下载视频",Command.ITEM,1);
		
		form=new Form("下载");
		back=new Command("Back",Command.BACK,1);
		exit=new Command("Exit",Command.EXIT,0);
		form.addCommand(back);
		form.addCommand(exit);
		form.addCommand(download);
		form.append(tips);
		form.append(videoURL);
		form.setCommandListener(this);
		display.setCurrent(form);

	}
	
	 public void commandAction(Command c, Displayable d) {
		 	// 返回上一级
	        if (c == back) {
	            new Thread(new Runnable() {
	                public void run() {
	                	System.out.println("page "+PageID+" search_word:"+video_info.getSearchKeyword());
	                	System.out.println("now "+video_info.getPageNum());
	                	video_info.setPageNum(video_info.getPageNum()-1);
	                	new GetVideoInfoPage(ml, video_info);
	                }
	            }).start();
	        }
	        // 退出app
	        if(c==exit){
	        	ml.exitApp();
	        }
	        if(c==download){
	        	new Thread(new Runnable() {
                    public void run() {
                    	try {
                    		System.out.println("video_url is:"+video_url);
							ml.platformRequest(video_url);
                    	} catch (ConnectionNotFoundException e) {
							e.printStackTrace();
						}
                    	
                    }
	            }).start();
	        }
	    }
}
