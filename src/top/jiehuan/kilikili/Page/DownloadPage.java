package top.jiehuan.kilikili.Page;

import java.util.Vector;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;

import top.jiehuan.kilikili.Exception.*;
import top.jiehuan.kilikili.util.FindString;
import top.jiehuan.kilikili.util.URLget;

public class DownloadPage extends Page implements CommandListener{
	
	public static final short PageID = 5;
	
	Form form;
	Command alert_back;
	Command alert_download;
	Command download;
	Command transcoding_download;
	Command goto_transcoding_site;
	TextField videoURL;
	StringItem tips;
	
	String video_url;
	String bvid;
	String cid;
	
	public DownloadPage(Vector page_info_list){
		//this.video_info=video_info;
		
		super(page_info_list);
		form=new Form(lang_res.getValue("download"));
		try{
			video_info = page_info.getVideoInfo();
		}catch(PageInfoEmptyException e){
			displayErrorAlert("Page is Empty:"+e.getMessage());
		}
		
		initPageVars();
		initDisplayVars();
		display();
	}
	public short getPageID() {
        return PageID;
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
                    	} catch (Exception e) {
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
        	        final Command closeCommand = new Command("关闭", Command.EXIT, 1);
        	        final Command confirmCommand = new Command("确认", Command.OK, 0);
        	        alert.addCommand(closeCommand);
        	        alert.addCommand(confirmCommand);

        	        alert.setCommandListener(new CommandListener() {
        	            public void commandAction(Command c, Displayable d) {
        	            	if (c == confirmCommand) {
        	                    // 确认按钮被按下，执行任务
        	                    new Thread(new Runnable() {
        	                        public void run() {
        	                            try {
        	                                ml.platformRequest(URLget.DOWNLOAD_TRANSCODING_VIDEO_URL + video_info.getCID() + "_240p.mp4");
        	                            } catch (Exception e) {
        	                                e.printStackTrace();
        	                            }
        	                        }
        	                    }).start();
        	                } else if (c == closeCommand) {
        	                    // 关闭按钮被按下，返回到之前的界面
        	                    display.setCurrent(form);
        	                }
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
	 
	 protected void initPageVars(){
		bvid = video_info.getBVID();
		try {
			this.video_url=page_info.getVideoInfo().getVideoURL();
		} catch (Exception e) {
			e.printStackTrace();
			displayErrorAlert("获取错误");
		}
	 }
	 
	 protected void initDisplayVars(){
		
		
		videoURL = new TextField("",this.video_url,10000,TextField.ANY);
		tips = new StringItem("",lang_res.getValue("download_tips"));
		
		transcoding_download = new Command(lang_res.getValue("transcoding_download"),Command.ITEM,1);
		download=new Command(lang_res.getValue("download"),Command.ITEM,1);	
		initBackAndExitCommand();
		goto_transcoding_site=new Command(lang_res.getValue("goto_transcoding_site"),Command.ITEM,1);
	 }
	 protected void display(){
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

}
