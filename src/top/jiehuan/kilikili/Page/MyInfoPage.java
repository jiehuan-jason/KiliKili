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
	Command token_status;
	Command token_refresh;
	StringItem tips;
	StringItem personal_info;
	
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
				//displayInfoAlert("你已经登录！cookies:"+token_utils.loadToken());
				//String webpage = URLget.BackWeb(URLget.GET_PERSONAL_INFO_URL);
				//displayInfoAlert(webpage);
				String personal_info_content = URLget.BackWeb(URLget.GET_PERSONAL_INFO_URL);
				String name = FindString.findValue(personal_info_content, "uname");
				String sign = FindString.findValue(personal_info_content, "sign");
				form = new Form(name);
				personal_info = new StringItem("",name+"\n"+sign);
			}catch(Exception e){
				e.printStackTrace();
				displayErrorAlert(e.getMessage());
			}
			
		}else{
			try {
				info = getQRCodeURLandKey();
				qrcode = TextToQRcodeImage.encode(info[0]);
				imageItem = new ImageItem("Login QRCode", qrcode, ImageItem.LAYOUT_CENTER, "Login QRCode");
				tips = new StringItem("","\n扫码后请点击刷新命令");
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
		token_status = new Command("Token Status", Command.ITEM, 2);
		token_refresh = new Command("Refresh Token", Command.ITEM, 2);
	}

	protected void display() {
		if(!token_utils.isTokenStored()){
			form.append(tips);
			form.append(imageItem);
			form.addCommand(refresh);
		}
		else {
			form.append(personal_info);
			form.addCommand(delete);
			//form.addCommand(token_status);
			//form.addCommand(token_refresh);
		}
		form.addCommand(back);
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
					CookiesUtils utils = new CookiesUtils();
					utils.updateToken(content.cookies);
					new CookiesUtils("refresh_token").updateToken(FindString.findValue(content.content, "refresh_token"));
					cookies = getBUVIDAndAddToCookies(content.cookies);
					utils.updateToken(cookies);
					initPageVars();
					initDisplayVars();
					display();
				}
			} catch (Exception e) {
				e.printStackTrace();
				displayErrorAlert(e.getMessage());
			} 
        }else if(c==delete){
        	try{
        		new CookiesUtils().deleteToken();
        		backMainPage();
        	}catch(Exception e){
        		//TODO
        	}
        }else if(c==token_status){
        	display_token_status();
        }else if(c==token_refresh){
        	refresh_token();
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
	
	private void display_token_status(){
		try{
			String content = URLget.BackWeb(URLget.GET_CSRF_REFRESH_TOKEN_STATUTS_URL);
			displayInfoAlert(content);
		}catch(Exception e){
			displayErrorAlert(e.getMessage());
		}
	}
	
	public static void refresh_token(){
		try{
			CookiesUtils cookies_utils = new CookiesUtils();
			if(cookies_utils.isTokenStored()){
				String cookies = cookies_utils.loadToken();
				CookiesUtils refresh_util = new CookiesUtils("refresh_token");
				String refresh_token = refresh_util.loadToken();
				String csrf = FindString.findValueInCookies(cookies, "bili_jct");
				String content = URLget.BackWeb(URLget.GET_CSRF_REFRESH_TOKEN_STATUTS_URL);
				//if(FindString.findValueBool(content, "refresh").equals("true")){
					
				//}
				long time = System.currentTimeMillis();
				content = URLget.BackWeb(URLget.GET_CorrespondPath_TIMESTAMP_URL+"?t="+String.valueOf(time));
				String hash = FindString.findValue(content, "hash");
				WebModel web = URLget.BackWebWithMoreInfo(URLget.GET_REFRESH_CSRF_URL+hash);
				String refresh_csrf = FindString.findValueInHTMLLabel(web.content);
				web = URLget.BackWebAndCookiesPost(URLget.REFRESH_COOKIES_URL, "csrf="+csrf+"&refresh_csrf="+refresh_csrf+"&source=main_web&refresh_token="+refresh_token);
				//refresh_token = FindString.findValue(web.content, "refresh_token");
				//refresh_token保持旧值 后面post将会用到
				refresh_util.updateToken(FindString.findValue(web.content, "refresh_token"));
				cookies_utils.updateToken(web.cookies);
				csrf = FindString.findValueInCookies(web.cookies, "bili_jct");
				web = URLget.BackWebAndCookiesPost(URLget.REFRESH_COOKIES_COMFIRM_URL,"csrf="+csrf+"&refresh_token="+refresh_token);
			}	
		}catch(Exception e){
			
		}
	}
	
	private String getBUVIDAndAddToCookies(String cookies) throws WebReturnErrorCodeException, IOException{
		WebModel web = URLget.BackWebWithMoreInfo(URLget.GET_BUVID3_URL);
		//displayInfoAlert(web.content);
		String buvid3 = FindString.findValue(web.content, "b_3");
		//String buvid4 = FindString.findValue(web.content, "b_4");
		return "buvid3="+buvid3+"; "+cookies;
	}
}
