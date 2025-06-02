package top.jiehuan.kilikili.Page;

import java.util.Vector;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.StringItem;

import top.jiehuan.kilikili.Exception.PageInfoEmptyException;
import top.jiehuan.kilikili.Model.PageInfo;
import top.jiehuan.kilikili.util.FindString;
import top.jiehuan.kilikili.util.URLget;

public class UserInfoPage extends Page implements CommandListener {
	
	public static final short PageID = 4;
	
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
	private String mid;
	
	private int type;
	
	
	public UserInfoPage(Vector page_info_list){
		super(page_info_list);
		this.page_info_list = page_info_list;
		form=new Form(lang_res.getValue("user_info"));
		type = page_info.getType();
		
		
		if(type == 0){
			try{
				video_info = page_info.getVideoInfo();
			}catch(PageInfoEmptyException e){
				displayErrorAlert("Page is Empty:"+e.getMessage());
			}
		}else{
			try{
				mid = page_info.getMID();
			}catch(PageInfoEmptyException e){
				displayErrorAlert("Page is Empty:"+e.getMessage());
			}
		}
		
		initPageVars();
		initDisplayVars();
		display();
	}
	
	
	
	public short getPageID() {
        return PageID;
 }
	
	public void commandAction(Command c, Displayable d) {
	 	// 返回上级界面
        if (c == back) {
        	goLastPage();
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
                	} catch (Exception e) {
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
	protected void initPageVars(){
		
		try {
			if(type == 0){
				System.out.println(video_info.getUserMID());
				text = URLget.BackWeb(URLget.USER_INFO_URL+video_info.getUserMID());
			}else{
				System.out.println(mid);
				text = URLget.BackWeb(URLget.USER_INFO_URL+mid);
			}
			
			name = FindString.findValue(text, "name");
			sign = FindString.findValue(text, "sign");
			fans = FindString.findValueInt(text, "fans");
			attentions = FindString.findValueInt(text, "attention");
			level = FindString.findValueInt(text, "current_level");
			face_url = FindString.findValue(text, "face");
		} catch (Exception e) {
			displayErrorAlert("UserInfoPage initPageVars Error:"+e.getMessage());
		}
		
	}
	
	protected void initDisplayVars(){
		form=null;
		form=new Form(name+lang_res.getValue("user_info"));
		nameitem=new StringItem("",name+"  ");
		signitem=new StringItem("","\n"+lang_res.getValue("introduction"));
		
		fansitem=new StringItem("",lang_res.getValue("fans")+fans+"\n");
		attentionsitem=new StringItem("",lang_res.getValue("attentions")+attentions+"\n");
	
		System.out.println("/level_img/lv"+level+".png");
		try {
			level_img = Image.createImage("/level_img/lv"+level+".png");
		} catch (Exception e) {
			displayErrorAlert("UserInfoPage init level_img error:"+e.getMessage());
		}
		showUserFace = new Command(lang_res.getValue("show_user_face"),Command.OK,1);
		getUserVideo = new Command(lang_res.getValue("get_user_video"),Command.OK,1);
		initBackAndExitCommand();
	}
	
	protected void display(){
		form.append(nameitem);
		//form.append(levelitem);
		form.append(level_img);
		form.append(signitem);
		String[] items=FindString.Display_Desc(sign);
		for(int i=0;i<items.length;i++){
			form.append(new StringItem(null,items[i]));
		}
		form.append(fansitem);
		form.append(attentionsitem);
		form.addCommand(back);
		form.addCommand(showUserFace);
		form.addCommand(getUserVideo);
		form.addCommand(exit);
		form.setCommandListener(this);
		display.setCurrent(form);
	}
}
