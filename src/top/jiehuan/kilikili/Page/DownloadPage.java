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
import top.jiehuan.kilikili.util.FindString;
import top.jiehuan.kilikili.util.GetLangRes;
import top.jiehuan.kilikili.util.URLget;

public class DownloadPage implements CommandListener{
	
	public static final short PageID = 5;
	
	private MainMIDlet ml;
	GetLangRes lang_res;
	Display display;
	Form form;
	Form alert_form;
	Command back;
	Command alert_back;
	Command alert_download;
	Command exit;
	Command download;
	Command transcoding_download;
	Command goto_transcoding_site;
	TextField videoURL;
	StringItem tips;
	
	String video_url;
	String bvid;
	String cid;
	
	VideoInfo video_info;
	Vector page_info_list;
	PageInfo page_info;
	
	public DownloadPage(Vector page_info_list){
		//this.video_info=video_info;
		
		this.page_info_list = page_info_list;
		page_info = (PageInfo) page_info_list.lastElement();
		ml=page_info.getMainMIDletObject();
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
	                	goLastPage();
	                }
	            }).start();
	        }
	        // 退出app
	        else if(c==exit){
	        	ml.exitApp();
	        }
	        else if(c==download){
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
	        }else if(c==transcoding_download){
	        	try{
	        		URLget.BackWeb(URLget.SEND_TRANSCODING_REQUEST_URL+"bvid="+bvid+"&cid="+video_info.getCID());
	        		Alert alert = new Alert("Task", lang_res.getValue("task_add_ok"), null, AlertType.INFO);
        	        alert.setTimeout(Alert.FOREVER); // 设置为永远显示，直到用户操作
        	        display.setCurrent(alert, form);
	        	}catch(ErrorVideoStatusException e){
	        		if(e.getCode()==12){
	        			Alert alert = new Alert("Task", lang_res.getValue("task_exists"), null, AlertType.INFO);
	        	        alert.setTimeout(Alert.FOREVER); // 设置为永远显示，直到用户操作
	        	        display.setCurrent(alert, form);
	        		}else{
	        			displayErrorAlert("Error code:"+e.getCode());
	        		}
	        	}catch(Exception e1){
	        		displayErrorAlert("Error code:"+e1.getMessage());
	        	}
	        }else if(c==goto_transcoding_site){
	        	try{
	        		URLget.BackWeb(URLget.GET_TRANSCODING_STATUS_URL+"taskId="+video_info.getCID());
	        		Alert alert = new Alert("Task", lang_res.getValue("task_is_ok"), null, AlertType.INFO);
        	        alert.setTimeout(Alert.FOREVER); // 设置为永远显示，直到用户操作
        	        alert.setCommandListener(new CommandListener() {
        	            public void commandAction(Command c, Displayable d) {
        	                // 此处可以处理 Alert 的关闭事件
        	            	new Thread(new Runnable() {
        	                    public void run() {
        	                    	try {
        								ml.platformRequest(URLget.DOWNLOAD_TRANSCODING_VIDEO_URL+video_info.getCID()+"_240p.mp4");
        	                    	} catch (ConnectionNotFoundException e) {
        								e.printStackTrace();
        							}
        	                    	
        	                    }
        		            }).start();
        	            }
        	        });
        	        display.setCurrent(alert, form);
	        	}catch(ErrorVideoStatusException e){
	        		if(e.getCode()==10){
	        			Alert alert = new Alert("Task", lang_res.getValue("task_is_being_processed"), null, AlertType.INFO);
	        	        alert.setTimeout(Alert.FOREVER); // 设置为永远显示，直到用户操作
	        	        display.setCurrent(alert, form);
	        		}else if(e.getCode() == 11){
	        			Alert alert = new Alert("Task", lang_res.getValue("task_is_being_processed")+FindString.findValueInt(e.getContent(), "queueLength"), null, AlertType.INFO);
	        			alert.setTimeout(Alert.FOREVER); // 设置为永远显示，直到用户操作
	        	        display.setCurrent(alert, form);
	        		}else{
	        			displayErrorAlert("Error code:"+e.getCode());
	        		}
	        	}catch(Exception e1){
	        		displayErrorAlert("Error code:"+e1.getMessage());
	        	}
	        }
	        
	    }
	 
	 private void initVideoVars(){
		bvid = video_info.getBVID();
		display = Display.getDisplay(ml);
		try {
			this.video_url=page_info.getVideoInfo().getVideoURL();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			displayErrorAlert("获取错误");
		}
	 }
	 
	 private void initDisplayVars(){
		
		form=new Form(lang_res.getValue("download"));
		videoURL = new TextField("",this.video_url,10000,TextField.ANY);
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
	 private void goLastPage(){
			
			page_info_list.removeElementAt(page_info_list.size()-1);
			PageInfo last_page = (PageInfo) page_info_list.lastElement();
			System.out.println("call goLastPage.Page now is:"+last_page.pageID);
			
			short page = last_page.pageID;
			page_info.back(page, page_info_list);
	}
	 

}
