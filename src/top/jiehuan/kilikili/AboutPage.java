package top.jiehuan.kilikili;

import java.io.UnsupportedEncodingException;

import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;

public class AboutPage implements CommandListener{
	private MainMIDlet ml;
	Display display;
	Form form;
	Command back;
	Command exit;
	StringItem author;
	StringItem text;
	public AboutPage(MainMIDlet midlet){
		ml=midlet;
		display = Display.getDisplay(midlet);
		author = new StringItem("","软件作者：jiehuan\n" +
				"电子邮箱：jiehuan233@outlook.com\n" +
				"dospy.wang:@jiehuan\n" +
				"github:@jiehuan-jason\n" +
				"网盘:jiehuan233.ysepan.com\n");
		text = new StringItem("","软件版本：V0.1beta2\n" +
				"更新日期：2024.9.16\n" +
				"祝各位中秋佳节快乐！\n");
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
	        if (c == back) {
	        	
	           
	            new Thread(new Runnable() {
	                public void run() {
	                	ml.display.setCurrent(ml.form);
	                }
	            }).start();
	            //GetVideoInfoPage secondMIDlet = new GetVideoInfoPage(text);
	            //display.setCurrent(secondMIDlet.getForm());
	            //notifyPaused();
	        }
	        if(c==exit){
	        	ml.exitApp();
	        }
	    }

}
