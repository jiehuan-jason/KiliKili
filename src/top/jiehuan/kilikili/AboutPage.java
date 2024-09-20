package top.jiehuan.kilikili;


import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;

public class AboutPage implements CommandListener{
	// 定义所需要的变量
	private MainMIDlet ml;
	Display display;
	Form form;
	Command back;
	Command exit;
	StringItem author;
	StringItem text;
	
	public AboutPage(MainMIDlet midlet){
		//初始化变量和界面
		ml=midlet;
		display = Display.getDisplay(midlet);
		author = new StringItem("","软件作者：jiehuan\n" +
				"电子邮箱：jiehuan233@outlook.com\n" +
				"dospy.wang:@jiehuan\n" +
				"github:@jiehuan-jason\n" +
				"网盘:jiehuan233.ysepan.com\n");
		text = new StringItem("","软件版本：V0.2alpha\n" +
				"更新日期：2024.9.21\n");
		form=new Form("关于");
		back=new Command("Back",Command.BACK,1);
		exit=new Command("Exit",Command.EXIT,0);
		form.addCommand(back);
		form.addCommand(exit);
		form.append(author);
		form.append(text);
		form.setCommandListener(this);
		display.setCurrent(form);
	}
	 public void commandAction(Command c, Displayable d) {
		 	// 返回主界面
	        if (c == back) {
	            new Thread(new Runnable() {
	                public void run() {
	                	ml.display.setCurrent(ml.form);
	                }
	            }).start();
	        }
	        // 退出app
	        if(c==exit){
	        	ml.exitApp();
	        }
	    }

}
