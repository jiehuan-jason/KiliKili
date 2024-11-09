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

import top.jiehuan.kilikili.Exception.ErrorVideoStatusException;
import top.jiehuan.kilikili.Exception.WebReturnErrorCodeException;

public class UserInfoPage implements CommandListener {
	
	public static String PageID = "4";
	
	private MainMIDlet ml;
	GetLangRes lang_res;
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
	
	private String bvid;
	private String face_url;
	private String text;
	private String name;
	private String sign;
	private String fans;
	private String attentions;
	private String level;
	
	VideoInfo video_info;
	
	public UserInfoPage(MainMIDlet ml,VideoInfo video_info){
		this.ml=ml;
		this.bvid=video_info.getBVID();
		this.video_info = video_info;
		
		this.video_info.setPageNum(MainPage.addPageNum(PageID,video_info));
		loadMessages();
		
		try{
			initPageVars();
			initDisplayVars();
			display();
			
		}catch(Exception e){
			form=new Form("Error");
			back=new Command(lang_res.getValue("back"),Command.BACK,1);
			exit=new Command(lang_res.getValue("exit"),Command.EXIT,0);
			form.append(new StringItem("","获取错误"));
			form.addCommand(back);
			form.addCommand(exit);
			form.setCommandListener(this);
			Alert alert = new Alert("Error", "获取错误", null, AlertType.ERROR);
            alert.setTimeout(Alert.FOREVER); // 设置为永远显示，直到用户操作
            display.setCurrent(alert, form);
            
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
		
		display = Display.getDisplay(ml);
		form=new Form(name+lang_res.getValue("user_info"));
		back=new Command(lang_res.getValue("back"),Command.BACK,1);
		exit=new Command(lang_res.getValue("exit"),Command.EXIT,0);
		showUserFace = new Command(lang_res.getValue("show_user_face"),Command.OK,1);
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
		form.setCommandListener(this);
		display.setCurrent(form);
	}
}
