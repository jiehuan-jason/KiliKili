package top.jiehuan.kilikili.Page;

import java.io.IOException;
import java.util.Vector;

import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;

import top.jiehuan.kilikili.MainMIDlet;
import top.jiehuan.kilikili.PageInfo;
import top.jiehuan.kilikili.VideoInfo;
import top.jiehuan.kilikili.Exception.*;
import top.jiehuan.kilikili.util.GetLangRes;
import top.jiehuan.kilikili.util.URLget;

public class DownloadPage implements CommandListener{
	
	public static short PageID = 5;
	
	private MainMIDlet ml;
	GetLangRes lang_res;
	Display display;
	Form form;
	Command back;
	Command exit;
	Command download;
	Command transcoding_download;
	Command goto_transcoding_site;
	TextField videoURL;
	TextField bvid_for_user;
	StringItem tips;
	
	String video_url;
	String bvid;
	boolean isTranscodingGet;
	public static final String TRANSCODING_WEBSITE_URL = "http://www.kinsler.top/downloads/";
	
	VideoInfo video_info;
	Vector page_info_list;
	PageInfo page_info;
	
	public DownloadPage(MainMIDlet midlet,Vector page_info_list){
		//this.video_info=video_info;
		ml=midlet;
		this.page_info_list = page_info_list;
		page_info = (PageInfo) page_info_list.lastElement();
		loadMessages();
		try {
			video_info = page_info.getVideoInfo();
		} catch (PageInfoEmptyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			displayErrorAlert(e.getMessage());
		}
		
		
		initVideoVars();
		initDisplayVars();
		display();
	}
	
	 public void commandAction(Command c, Displayable d) {
		 	// 返回上一级
	        if (c == back) {
	            new Thread(new Runnable() {
	                public void run() {
	                	back();
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
	        }if(c==transcoding_download){
	        	try{
	        		URLget.BackWeb(URLget.SEND_TRANSCODING_REQUEST_URL+"bvid="+bvid);
	        		isTranscodingGet = true;
	        		Alert alert = new Alert("Task", lang_res.getValue("task_add_ok"), null, AlertType.INFO);
        	        alert.setTimeout(Alert.FOREVER); // 设置为永远显示，直到用户操作
        	        display.setCurrent(alert, form);
	        	}catch(ErrorVideoStatusException e){
	        		if(e.getCode()==12){
	        			isTranscodingGet = true;
	        			Alert alert = new Alert("Task", lang_res.getValue("task_exists"), null, AlertType.INFO);
	        	        alert.setTimeout(Alert.FOREVER); // 设置为永远显示，直到用户操作
	        	        display.setCurrent(alert, form);
	        		}else{
	        			displayErrorAlert("Error code:"+e.getCode());
	        		}
	        	}catch(Exception e1){
	        		displayErrorAlert("Error code:"+e1.getMessage());
	        	}
	        }if(c==goto_transcoding_site){
	        	Alert alert = new Alert("Task", lang_res.getValue("task_exists"), null, AlertType.INFO);
    	        alert.setTimeout(Alert.FOREVER); // 设置为永远显示，直到用户操作
    	        display.setCurrent(alert, form);
	        	/*if(isTranscodingGet){
	        		try {
						ml.platformRequest(TRANSCODING_WEBSITE_URL);
					} catch (ConnectionNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						displayErrorAlert(e.getMessage());
					}
	        	}else{
	        		displayErrorAlert(lang_res.getValue("not_get_transcoding"));
	        	}*/
	        }
	    }
	 
	 private void initVideoVars(){
		bvid = video_info.getBVID();
		display = Display.getDisplay(ml);
		isTranscodingGet = false;
		try {
			this.video_url=page_info.getVideoInfo().getVideoURL();
		} catch (PageInfoEmptyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			displayErrorAlert("获取错误");
		}
	 }
	 
	 private void initDisplayVars(){
		
		form=new Form(lang_res.getValue("download"));
		videoURL = new TextField("",this.video_url,10000,TextField.ANY);
		bvid_for_user = new TextField("",this.bvid,12,TextField.ANY);
		tips = new StringItem("",lang_res.getValue("download_tips"));
		
		transcoding_download = new Command(lang_res.getValue("transcoding_download"),Command.ITEM,1);
		download=new Command(lang_res.getValue("download"),Command.ITEM,1);	
		back=new Command(lang_res.getValue("back"),Command.BACK,1);
		exit=new Command(lang_res.getValue("exit"),Command.EXIT,0);
		goto_transcoding_site=new Command(lang_res.getValue("goto_transcoding_site"),Command.ITEM,1);
	 }
	 private void display(){
		form.addCommand(back);
		form.addCommand(exit);
		form.addCommand(download);
		form.addCommand(transcoding_download);
		form.addCommand(goto_transcoding_site);
		form.append(bvid_for_user);
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
	 private void displayErrorAlert(String error){
			form=new Form(lang_res.getValue("findError"));
			back=new Command(lang_res.getValue("back"),Command.BACK,1);
			exit=new Command(lang_res.getValue("exit"),Command.EXIT,0);
			form.addCommand(back);
			form.addCommand(exit);
			form.setCommandListener(this);
		 Alert alert = new Alert("Error", error, null, AlertType.ERROR);
         alert.setTimeout(Alert.FOREVER); // 设置为永远显示，直到用户操作
         display.setCurrent(alert, form);
         //ml.display.setCurrent(ml.form);
	}
	 private void back(){
		 page_info_list.removeElementAt(page_info_list.size()-1);
		 PageInfo last_page = (PageInfo) page_info_list.lastElement();
     	 if(last_page.pageID == GetVideoInfoPage.PageID){
     		new GetVideoInfoPage(ml, page_info_list);
     	 }else{
     		 new MainPage(ml);
     	 }
	 }
	 

}
