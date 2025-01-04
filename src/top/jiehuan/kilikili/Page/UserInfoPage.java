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
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.StringItem;

import top.jiehuan.kilikili.MainMIDlet;
import top.jiehuan.kilikili.PageInfo;
import top.jiehuan.kilikili.VideoInfo;
import top.jiehuan.kilikili.Exception.ErrorVideoStatusException;
import top.jiehuan.kilikili.Exception.PageInfoEmptyException;
import top.jiehuan.kilikili.Exception.WebReturnErrorCodeException;
import top.jiehuan.kilikili.util.FindString;
import top.jiehuan.kilikili.util.GetLangRes;
import top.jiehuan.kilikili.util.URLget;

public class UserInfoPage implements CommandListener {
	
	public static final short PageID = 4;
	
	private MainMIDlet ml;
	GetLangRes lang_res;
	Display display;
	Command back;
	Command exit;
	Command showUserFace;
	Command getUserVideo;
	Form form;
	Image level_img;
	Image face_img;
	StringItem nameitem;
	StringItem signitem;
	StringItem fansitem;
	StringItem attentionsitem;
	StringItem levelitem;
	
	private String face_url;
	private String text;
	private String name;
	private String sign;
	private String fans;
	private String attentions;
	private String level;
	
	private VideoInfo video_info;
	private Vector page_info_list;
	
	public UserInfoPage(Vector page_info_list){
		
		this.page_info_list = page_info_list;
		PageInfo page_info = (PageInfo) page_info_list.lastElement();
		this.ml=page_info.getMainMIDletObject();

		loadMessages();

		display = Display.getDisplay(ml);
		form=new Form(name+lang_res.getValue("user_info"));
		try {
			video_info = page_info.getVideoInfo();
		} catch (PageInfoEmptyException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			displayErrorAlert("获取错误");
		}
		
		
		try{
			initPageVars();
			initDisplayVars();
			display();
			
		}catch(Exception e){
			displayErrorAlert(e.getMessage());          
		}
	}
	
	
	public void commandAction(Command c, Displayable d) {
	 	// 返回上级界面
        if (c == back) {
            new Thread(new Runnable() {
                public void run() {
                	System.out.println("page "+PageID+" search_word:"+video_info.getSearchKeyword());
                	page_info_list.removeElementAt(page_info_list.size()-1);
                	new GetVideoInfoPage(page_info_list);
                }
            }).start();
        }
        // 退出app
        else if(c==exit){
        	ml.exitApp();
        }
        else if(c==showUserFace){
        	new Thread(new Runnable() {
                public void run() {
                	try {
                		System.out.println("face_url is:"+face_url);
						ml.platformRequest(face_url);
                	} catch (ConnectionNotFoundException e) {
						e.printStackTrace();
					}
                	
                }
            }).start();
        }
        else if(c==getUserVideo){
        	new Thread(new Runnable() {
                public void run() {
                	PageInfo newpage = new PageInfo(UserVideoListPage.PageID,ml);
                	newpage.setVideoInfo(video_info);
                	page_info_list.addElement(newpage);
                	new UserVideoListPage(page_info_list);
                }
            }).start();
        }
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
	private void initPageVars() throws IOException,WebReturnErrorCodeException, ErrorVideoStatusException{
		System.out.println(video_info.getUserMID());
		text = URLget.BackWeb(URLget.USER_INFO_URL+video_info.getUserMID());
		name = FindString.findValue(text, "name");
		sign = FindString.findValue(text, "sign");
		fans = FindString.findValueInt(text, "fans");
		attentions = FindString.findValueInt(text, "attention");
		level = FindString.findValueInt(text, "current_level");
		face_url = FindString.findValue(text, "face");
	}
	
	private void initDisplayVars() throws IOException{
		nameitem=new StringItem("",name+"  ");
		signitem=new StringItem("",lang_res.getValue("introduction")+sign+"\n");
		fansitem=new StringItem("",lang_res.getValue("fans")+fans+"\n");
		attentionsitem=new StringItem("",lang_res.getValue("attentions")+attentions+"\n");
	
		System.out.println("/level_img/lv"+level+".png");
		level_img = Image.createImage("/level_img/lv"+level+".png");
		
		back=new Command(lang_res.getValue("back"),Command.BACK,1);
		exit=new Command(lang_res.getValue("exit"),Command.EXIT,0);
		showUserFace = new Command(lang_res.getValue("show_user_face"),Command.OK,1);
		getUserVideo = new Command(lang_res.getValue("get_user_video"),Command.OK,1);
	}
	
	private void display(){
		form.append(nameitem);
		//form.append(levelitem);
		form.append(level_img);
		form.append(signitem);
		form.append(fansitem);
		form.append(attentionsitem);
		form.addCommand(back);
		form.addCommand(exit);
		form.addCommand(showUserFace);
		form.addCommand(getUserVideo);
		form.setCommandListener(this);
		display.setCurrent(form);
	}
	private void displayErrorAlert(String error){
		 Alert alert = new Alert("Error", error, null, AlertType.ERROR);
	     alert.setTimeout(Alert.FOREVER); // 设置为永远显示，直到用户操作
	     back=new Command(lang_res.getValue("back"),Command.BACK,1);
	     alert.addCommand(back);
	     alert.setCommandListener(this);
	     display.setCurrent(alert, form);	
	 }
}
