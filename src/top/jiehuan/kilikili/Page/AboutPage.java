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
import top.jiehuan.kilikili.PageInfo;
import top.jiehuan.kilikili.util.GetLangRes;

public class AboutPage implements CommandListener{
	
	public static final short PageID = 6;
	// 定义所需要的变量
	private MainMIDlet ml;
	
	GetLangRes lang_res;
	Display display;
	Form form;
	Command back;
	Command exit;
	StringItem author;
	StringItem text;
	StringItem thanks_list;
	PageInfo page_info;
	
	public static String version = "0.3.3";
	
	public AboutPage(Vector page_list_info){
		//初始化变量和界面
		page_info = (PageInfo) page_list_info.lastElement();
		ml=page_info.getMainMIDletObject();
		
		loadMessages();
		initDisplayVars();
		display();
	}
	 public void commandAction(Command c, Displayable d) {
		 	// 返回主界面
	        if (c == back) {
	            page_info.backMainPage();
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
			text = new StringItem("","软件版本：V0.3beta2\n" +
					"更新日期：2024.12.7\n" +
					"更新内容：添加了分p视频获取\n" +
					"转码下载可以完全在软件内完成\n" +
					"修复了一些bug\n");
			thanks_list = new StringItem("","感谢列表（截至更新时，排名不分先后）：" +
					"爱发电用户_898f2\n" +
					"爱发电用户_wnJK\n" +
					"爱发电用户_7ebbe\n" +
					"爱发电用户_kMRv\n" +
					"欢迎用智能手机/PC访问 https://afdian.com/a/jiehuan233 赞助本项目");
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
