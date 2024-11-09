package top.jiehuan.kilikili;

import java.io.IOException;

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
	GetLangRes lang_res;
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
		loadMessages();
		initVideoVars();
		initDisplayVars();
		display();
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
	 
	 private void initVideoVars(){
		bvid = video_info.getBVID();
		display = Display.getDisplay(ml);
		video_info.setPageNum(MainPage.addPageNum(PageID,video_info));
		this.video_url=video_info.getVideoURL();
	 }
	 
	 private void initDisplayVars(){
		form=new Form(lang_res.getValue("download"));
		videoURL = new TextField("",this.video_url,10000,TextField.ANY);
		tips = new StringItem("",lang_res.getValue("download_tips"));

		download=new Command(lang_res.getValue("download"),Command.ITEM,1);	
		back=new Command(lang_res.getValue("back"),Command.BACK,1);
		exit=new Command(lang_res.getValue("exit"),Command.EXIT,0);
	 }
	 private void display(){
		form.addCommand(back);
		form.addCommand(exit);
		form.addCommand(download);
		form.append(tips);
		form.append(videoURL);
		form.setCommandListener(this);
		display.setCurrent(form);
	 }
	 private void loadMessages() {
	        // 根据系统语言加载相应的资源文件
	        try {
				lang_res = new GetLangRes(System.getProperty("microedition.locale"));
				//System.out.println(lang_res.getLangFileContent());
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }

}
