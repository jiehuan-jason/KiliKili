package top.jiehuan.kilikili.Page;


import java.util.Vector;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.ImageItem;
import javax.microedition.lcdui.StringItem;

import top.jiehuan.kilikili.util.CookiesUtils;

import com.google.zxing.*;


public class AboutPage extends Page implements CommandListener {
	
	public static final short PageID = 6;
	// 定义所需要的变量
	
	Form form;
	StringItem author;
	StringItem text;
	StringItem thanks_list;
	Image qrcode;
	ImageItem imageItem;
	Command get_cookies;
	
	public static String version = "1.0.2";
	
	public AboutPage(Vector page_list_info){
		//初始化变量和界面
		super(page_list_info);
		form=new Form(lang_res.getValue("about"));
		initDisplayVars();
		display();
	}
	 public void commandAction(Command c, Displayable d) {
		 	// 返回主界面
	        if (c == back) {
	            super.backMainPage();
	        }
	        // 退出app
	        if(c==exit){
	        	ml.exitApp();
	        }if(c == get_cookies){
	        	String cookies;
				try {
					cookies = new CookiesUtils().loadToken();
					displayInfoAlert(cookies, form);
				} catch (Exception e) {
					displayErrorAlert(e.getClass().getName()+e.getMessage());
				} 
	        	
	        }
	    }
	 public short getPageID() {
	        return PageID;
	 }
	 protected void initDisplayVars(){
		    
			author = new StringItem("","软件作者: jiehuan\n" +
					"电子邮箱: jiehuan233@outlook.com\n" +
					"dospy.wang: @jiehuan\n" +
					"github: @jiehuan-jason\n" +
					"网盘: jiehuan233.ysepan.com\n"
					+ "个人主页: www.kinsler.top\n");
			text = new StringItem("","软件版本：V1.0beta1\n" +
					"更新日期：2025.6.6\n" +
					"更新内容：\n" +
					"修复了部分bug\n");
			thanks_list = new StringItem("","捐赠感谢列表（截至更新时，排名不分先后）：\n" +
					"爱发电用户_898f2\n" +
					"爱发电用户_wnJK\n" +
					"爱发电用户_7ebbe\n" +
					"爱发电用户_kMRv\n" +
					"jjbvfggggjj\n" +
					"皓哥\n" +
					"欢迎用智能手机/PC访问 https://afdian.com/a/jiehuan233 赞助本项目\n" +
					"扫描二维码访问项目dospy论坛地址\n");
			initBackAndExitCommand();
			
			qrcode = TextToQRcodeImage.encode("https://www.dospy.wang/thread-22464-1-1.html");
			imageItem = new ImageItem(null, qrcode, ImageItem.LAYOUT_CENTER, "QRCode");
			get_cookies = new Command("Cookies", Command.ITEM, 2);
	 }
	 protected void display(){
		
		form.append(author);
		form.append(text);
		form.append(thanks_list);
		form.append(imageItem);
		form.addCommand(back);
		form.addCommand(get_cookies);
		form.addCommand(exit);
		form.setCommandListener(this);
		display.setCurrent(form);
	 }
	protected void initPageVars() {
		TextToQRcodeImage.encode("test");
	}
	
}
