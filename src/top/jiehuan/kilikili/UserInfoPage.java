package top.jiehuan.kilikili;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;

public class UserInfoPage implements CommandListener {
	private MainMIDlet ml;
	Display display;
	Command back;
	Command exit;
	Form form;
	StringItem nameitem;
	StringItem signitem;
	StringItem fansitem;
	StringItem attentionsitem;
	StringItem levelitem;
	
	String bvid;
	
	public UserInfoPage(MainMIDlet ml,String mid,String bvid){
		this.ml=ml;
		this.bvid=bvid;
		
		String text = URLget.BackWeb(URLget.USER_INFO_URL+mid);
		
		String name = FindString.findValue(text, "name");
		String sign = FindString.findValue(text, "sign");
		String fans = FindString.findValueInt(text, "fans");
		String attentions = FindString.findValueInt(text, "attention");
		String level = FindString.findValueInt(text, "current_level");
		
		nameitem=new StringItem("",name+"  ");
		signitem=new StringItem("","简介:"+sign+"\n");
		fansitem=new StringItem("","粉丝:"+fans+"\n");
		attentionsitem=new StringItem("","关注数:"+attentions+"\n");
		levelitem=new StringItem("","LV"+level+"\n");
		
		display = Display.getDisplay(ml);
		form=new Form(name+"的个人信息");
		back=new Command("Back",Command.BACK,1);
		exit=new Command("Exit",Command.EXIT,0);
		form.append(nameitem);
		form.append(levelitem);
		form.append(signitem);
		form.append(fansitem);
		form.append(attentionsitem);
		form.addCommand(back);
		form.addCommand(exit);
		form.setCommandListener(this);
		display.setCurrent(form);
	}
	
	public void commandAction(Command c, Displayable d) {
	 	// 返回主界面
        if (c == back) {
            new Thread(new Runnable() {
                public void run() {
                	new GetVideoInfoPage(ml, "BV"+bvid);
                }
            }).start();
        }
        // 退出app
        if(c==exit){
        	ml.exitApp();
        }
    }
}
