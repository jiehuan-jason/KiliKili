package top.jiehuan.kilikili.Page;


import java.io.IOException;
import java.util.Vector;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;

import top.jiehuan.kilikili.MainMIDlet;
import top.jiehuan.kilikili.util.GetLangRes;

public class AboutPage implements CommandListener{
	
	public static short PageID = 6;
	// 定义所需要的变量
	private MainMIDlet ml;
	
	GetLangRes lang_res;
	Display display;
	Form form;
	Command back;
	Command exit;
	StringItem author;
	StringItem text;
	
	public static String version = "0.3.2";
	
	public AboutPage(MainMIDlet midlet,Vector page_list_info){
		//初始化变量和界面
		
		ml=midlet;
		
		loadMessages();
		initDisplayVars();
		display();
	}
	 public void commandAction(Command c, Displayable d) {
		 	// 返回主界面
	        if (c == back) {
	            new Thread(new Runnable() {
	                public void run() {
	                	new MainPage(ml);
	                }
	            }).start();
	        }
	        // 退出app
	        if(c==exit){
	        	ml.exitApp();
	        }
	    }
	 private void initDisplayVars(){
		    display = Display.getDisplay(ml);
		    form=new Form(lang_res.getValue("about"));
			author = new StringItem("","软件作者：jiehuan\n" +
					"电子邮箱：jiehuan233@outlook.com\n" +
					"dospy.wang:@jiehuan\n" +
					"github:@jiehuan-jason\n" +
					"网盘:jiehuan233.ysepan.com\n");
			text = new StringItem("","软件版本：V0.3beta1\n" +
					"更新日期：2024.11.24\n" +
					"更新内容：添加了转码视频获取\n" +
					"修复了一些bug");
			back=new Command(lang_res.getValue("back"),Command.BACK,1);
			exit=new Command(lang_res.getValue("exit"),Command.EXIT,0);
	 }
	 private void display(){
		form.addCommand(back);
		form.addCommand(exit);
		form.append(author);
		form.append(text);
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

}
