package top.jiehuan.kilikili;

import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;

public class DownloadPage implements CommandListener{
	private MainMIDlet ml;
	Display display;
	Form form;
	Command back;
	Command exit;
	Command download;
	StringItem videoURL;
	StringItem tips;
	
	String video_url;
	
	public DownloadPage(MainMIDlet midlet,String video_url){
		ml=midlet;
		display = Display.getDisplay(midlet);
		this.video_url=video_url;
		videoURL = new StringItem("","视频链接:"+video_url);
		tips = new StringItem("","\n"+"tips:如果下载按钮无法下载，请把光标移到上方的链接处并复制到浏览器打开下载");
		download=new Command("下载视频",Command.ITEM,1);
		
		form=new Form("下载");
		back=new Command("Back",Command.BACK,1);
		exit=new Command("Exit",Command.EXIT,0);
		form.addCommand(back);
		form.addCommand(exit);
		form.addCommand(download);
		form.append(videoURL);
		form.append(tips);
		form.setCommandListener(this);
		display.setCurrent(form);

	}
	
	 public void commandAction(Command c, Displayable d) {
		 	// 返回主界面
	        if (c == back) {
	            new Thread(new Runnable() {
	                public void run() {
	                	ml.display.setCurrent(ml.form);
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
