package top.jiehuan.kilikili;

import java.io.IOException;

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

public class UserInfoPage implements CommandListener {
	
	public static String PageID = "4";
	
	private MainMIDlet ml;
	Display display;
	Command back;
	Command exit;
	Command showUserFace;
	Form form;
	Image level_img;
	Image face_img;
	StringItem nameitem;
	StringItem signitem;
	StringItem fansitem;
	StringItem attentionsitem;
	StringItem levelitem;
	
	String bvid;
	String face_url;
	
	VideoInfo video_info;
	
	public UserInfoPage(MainMIDlet ml,VideoInfo video_info){
		this.ml=ml;
		this.bvid=video_info.getBVID();
		this.video_info = video_info;
		
		this.video_info.setPageNum(MainMIDlet.addPageNum(PageID,video_info));
		
		String text = URLget.BackWeb(URLget.USER_INFO_URL+video_info.getUserMID());
		if(text.startsWith("error")){
			form=new Form("Error");
			back=new Command("back",Command.BACK,1);
			exit=new Command("exit",Command.EXIT,0);
			form.append(new StringItem("","获取错误"));
			form.addCommand(back);
			form.addCommand(exit);
			form.setCommandListener(this);
			Alert alert = new Alert("Error", "获取错误", null, AlertType.ERROR);
            alert.setTimeout(Alert.FOREVER); // 设置为永远显示，直到用户操作
            display.setCurrent(alert, form);
            ml.display.setCurrent(ml.form);
		}else{
		
		String name = FindString.findValue(text, "name");
		String sign = FindString.findValue(text, "sign");
		String fans = FindString.findValueInt(text, "fans");
		String attentions = FindString.findValueInt(text, "attention");
		String level = FindString.findValueInt(text, "current_level");
		face_url = FindString.findValue(text, "face");
		
		nameitem=new StringItem("",name+"  ");
		signitem=new StringItem("","简介:"+sign+"\n");
		fansitem=new StringItem("","粉丝:"+fans+"\n");
		attentionsitem=new StringItem("","关注数:"+attentions+"\n");
		try {
			System.out.println("/level_img/lv"+level+".png");
			level_img = Image.createImage("/level_img/lv"+level+".png");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//levelitem=new StringItem("","LV"+level+"\n");
		
		
		
		display = Display.getDisplay(ml);
		form=new Form(name+"的个人信息");
		back=new Command("Back",Command.BACK,1);
		exit=new Command("Exit",Command.EXIT,0);
		showUserFace = new Command("显示用户头像",Command.OK,1);
		form.append(nameitem);
		//form.append(levelitem);
		form.append(level_img);
		form.append(signitem);
		form.append(fansitem);
		form.append(attentionsitem);
		form.addCommand(back);
		form.addCommand(exit);
		form.addCommand(showUserFace);
		form.setCommandListener(this);
		display.setCurrent(form);
		}
	}
	
	public void commandAction(Command c, Displayable d) {
	 	// 返回上级界面
        if (c == back) {
            new Thread(new Runnable() {
                public void run() {
                	System.out.println("page "+PageID+" search_word:"+video_info.getSearchKeyword());
                	video_info.setPageNum(video_info.getPageNum()-1);
                	new GetVideoInfoPage(ml, video_info);
                }
            }).start();
        }
        // 退出app
        if(c==exit){
        	ml.exitApp();
        }
        if(c==showUserFace){
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
    }
}
