package top.jiehuan.kilikili;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;

public class UserInfoPage implements CommandListener {
	private MainMIDlet ml;
	Display display;
	Command back;
	Command exit;
	Form form;
	
	String bvid;
	
	public UserInfoPage(MainMIDlet ml,String mid,String bvid){
		this.ml=ml;
		this.bvid=bvid;
		
		String text = URLget.BackWeb(URLget.USER_INFO_URL+mid);
		
		String name = FindString.findValue("name", text);
		String sign = FindString.findValue("sign", text);
		String fans = FindString.findValueInt("fans", text);
		String attentions = FindString.findValueInt("attentions", text);
		String level = FindString.findValueInt("current_level", text);
		
		display = Display.getDisplay(ml);
		form=new Form(name+"的个人信息");
		back=new Command("Back",Command.BACK,1);
		exit=new Command("Exit",Command.EXIT,0);
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
