package top.jiehuan.kilikili.Page;

import java.io.IOException;
import java.util.Vector;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.ImageItem;
import javax.microedition.lcdui.StringItem;

import com.google.zxing.TextToQRcodeImage;

import top.jiehuan.kilikili.WebModel;
import top.jiehuan.kilikili.Exception.ErrorVideoStatusException;
import top.jiehuan.kilikili.Exception.WebReturnErrorCodeException;
import top.jiehuan.kilikili.util.CookiesUtils;
import top.jiehuan.kilikili.util.FindString;
import top.jiehuan.kilikili.util.URLget;

public class MyInfoPage extends Page {
	public static final short PageID = 10;
	
	Form form;
	String name;
	Image qrcode;
	ImageItem imageItem;
	Command refresh;
	Command delete;
	StringItem tips;
	
	String[] info;
	String cookies;
	
	CookiesUtils token_utils;

	public MyInfoPage(Vector page_info_list) {
		super(page_info_list);
		form = new Form(lang_res.getValue("login"));
		initPageVars();
		initDisplayVars();
		display();
	}

	protected void initPageVars() {
		try {
			token_utils = new CookiesUtils();
		} catch (Exception e) {
			e.printStackTrace();
			displayErrorAlert(e.getMessage());
		} 
		if(token_utils.isTokenStored()){
			try{
				displayInfoAlert("你已经登录！");
				String webpage = URLget.BackWeb(URLget.GET_PERSONAL_INFO_URL);
				displayInfoAlert(webpage);
			}catch(Exception e){
				e.printStackTrace();
				displayErrorAlert(e.getMessage());
			}
			
		}else{
			try {
				info = getQRCodeURLandKey();
				qrcode = TextToQRcodeImage.encode(info[0]);
				imageItem = new ImageItem("Login QRCode", qrcode, ImageItem.LAYOUT_CENTER, "Login QRCode");
			} catch (Exception e) {
				e.printStackTrace();
				displayErrorAlert(e.getMessage());
			} 
		}
	}

	protected void initDisplayVars() {
		initBackAndExitCommand();
		refresh = new Command(lang_res.getValue("refresh"),Command.ITEM,1);
		delete = new Command(lang_res.getValue("delete_token"), Command.ITEM, 1);
	}

	protected void display() {
		form.append(imageItem);
		form.addCommand(back);
		if(!token_utils.isTokenStored())
			form.addCommand(refresh);
		else {
			form.addCommand(delete);
		}
		form.addCommand(exit);
		form.setCommandListener(this);
		display.setCurrent(form);
	}

	public void commandAction(Command c, Displayable d) {
		if (c == back) {
            super.backMainPage();
        }
        // 退出app
		else if(c==exit){
        	ml.exitApp();
        }else if(c==refresh){
        	try {
				WebModel content = URLget.BackWebAndCookies(URLget.GET_QRCODE_LOGIN_STATUS+"qrcode_key="+info[1]);
				String[] code = FindString.extractContentsInt(content.content,"\"code\"");
				System.out.println("code:"+code[1]);
				if(code[1].equals("0")){
					displayInfoAlert("Login Successfully!Cookies:"+content.cookies);
					new CookiesUtils().updateToken(content.cookies);
					String webpage = URLget.BackWeb(URLget.GET_PERSONAL_INFO_URL);
					displayInfoAlert(webpage);
				}
			} catch (Exception e) {
				e.printStackTrace();
				displayErrorAlert(e.getMessage());
			} 
        }else if(c==delete){
        	try{
        		new CookiesUtils().deleteToken();
        	}catch(Exception e){
        		//TODO
        	}
        }
	}
	
	//string[0] = url
	//string[1] = key
	private String[] getQRCodeURLandKey() throws WebReturnErrorCodeException, IOException, ErrorVideoStatusException{
		String content = URLget.BackWeb(URLget.GET_QRCODE_URL);
		String[] result = new String[2];
		result[0] = FindString.replace(FindString.findValue(content,"url"),"\\u0026","&");
		result[1] = FindString.findValue(content,"qrcode_key");
		System.out.println("url:"+result[0]);
		System.out.println("key:"+result[1]);
		return result;
	}
}
