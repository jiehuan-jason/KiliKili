package top.jiehuan.kilikili.Page;


import java.util.Vector;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.ImageItem;
import javax.microedition.lcdui.StringItem;

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
	
	public static String version = "1.0.1";
	
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
	        }
	    }
	 protected void initDisplayVars(){
		    
			author = new StringItem("","软件作者：jiehuan\n" +
					"电子邮箱：jiehuan233@outlook.com\n" +
					"dospy.wang:@jiehuan\n" +
					"github:@jiehuan-jason\n" +
					"网盘:jiehuan233.ysepan.com\n");
			text = new StringItem("","软件版本：V1.0alpha\n" +
					"更新日期：2025.1.28\n" +
					"更新内容：更新了登录测试\n" +
					"注意：仅为测试！！没有其他功能\n");
			thanks_list = new StringItem("","感谢列表（截至更新时，排名不分先后）：\n" +
					"爱发电用户_898f2\n" +
					"爱发电用户_wnJK\n" +
					"爱发电用户_7ebbe\n" +
					"爱发电用户_kMRv\n" +
					"欢迎用智能手机/PC访问 https://afdian.com/a/jiehuan233 赞助本项目\n" +
					"扫描二维码访问项目dospy论坛地址\n");
			initBackAndExitCommand();
			
			qrcode = TextToQRcodeImage.encode("https://www.dospy.wang/thread-22464-1-1.html");
			imageItem = new ImageItem(null, qrcode, ImageItem.LAYOUT_CENTER, "QRCode");
			
	 }
	 protected void display(){
		form.addCommand(back);
		form.addCommand(exit);
		form.append(author);
		form.append(text);
		form.append(thanks_list);
		form.append(imageItem);
		form.setCommandListener(this);
		display.setCurrent(form);
	 }
	protected void initPageVars() {
		TextToQRcodeImage.encode("test");
	}
	
}
