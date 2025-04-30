package top.jiehuan.kilikili.Page;

import java.io.IOException;
import java.util.Vector;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.ImageItem;
import javax.microedition.lcdui.StringItem;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;

import com.google.zxing.TextToQRcodeImage;

import top.jiehuan.kilikili.Exception.ErrorVideoStatusException;
import top.jiehuan.kilikili.Exception.WebReturnErrorCodeException;
import top.jiehuan.kilikili.Model.PageInfo;
import top.jiehuan.kilikili.Model.WebModel;
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
	Command fav_folder_list;
	StringItem tips;
	StringItem personal_info;
	
	String[] info;
	String cookies;
	//String enable_content = "";
	
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
		delete = new Command(lang_res.getValue("delete_token"), Command.ITEM, 2);
		//token_status = new Command("Token Status", Command.ITEM, 2);
		//token_refresh = new Command("Refresh Token", Command.ITEM, 2);
		fav_folder_list = new Command(lang_res.getValue("fav_folder_list"),Command.ITEM,1);
	}

	protected void display() {
		if(!token_utils.isTokenStored()){
			form.append(tips);
			form.append(imageItem);
			form.addCommand(refresh);
		}
		else {
			form.append(personal_info);
			form.addCommand(fav_folder_list);
			form.addCommand(delete);
			//form.append(new StringItem("","\n"+enable_content));
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
				WebModel content = URLget.BackWebWithMoreInfo(URLget.GET_QRCODE_LOGIN_STATUS+"qrcode_key="+info[1]);
				String[] code = FindString.extractContentsInt(content.content,"\"code\"");
				System.out.println("code:"+code[1]);
				if(code[1].equals("0")){
					displayInfoAlert("Login Successfully!Cookies:"+content.cookies, form);
					CookiesUtils utils = new CookiesUtils();
					utils.updateToken(content.cookies);
					cookies=content.cookies;
					//new CookiesUtils("refresh_token").updateToken(FindString.findValue(content.content, "refresh_token"));
					getBUVIDAndEnable();
					//enable_content = enable_content+"\n"+content.content+"\n"+payload;
					//utils.updateToken(cookies);
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
        	//refresh_token();
        }else if(c==fav_folder_list){
        	new Thread(new Runnable() {
                public void run() {
                	page_info_list.addElement(new PageInfo(FavFolderListPage.PageID,ml));
                    new FavFolderListPage(page_info_list); 
                }
            }).start();
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
			displayInfoAlert(content, form);
		}catch(Exception e){
			displayErrorAlert(e.getMessage());
		}
	}
	
	public short getPageID() {
        return PageID;
 }
	
	/*public static void refresh_token(){
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
				web = URLget.BackWebAndUserCookiesPost(URLget.REFRESH_COOKIES_URL, "csrf="+csrf+"&refresh_csrf="+refresh_csrf+"&source=main_web&refresh_token="+refresh_token);
				//refresh_token = FindString.findValue(web.content, "refresh_token");
				//refresh_token保持旧值 后面post将会用到
				refresh_util.updateToken(FindString.findValue(web.content, "refresh_token"));
				cookies_utils.updateToken(web.cookies);
				csrf = FindString.findValueInCookies(web.cookies, "bili_jct");
				web = URLget.BackWebAndUserCookiesPost(URLget.REFRESH_COOKIES_COMFIRM_URL,"csrf="+csrf+"&refresh_token="+refresh_token);
			}	
		}catch(Exception e){
			
		}
	}*/
	
	private void getBUVIDAndEnable() throws RecordStoreFullException, RecordStoreNotFoundException, RecordStoreException, WebReturnErrorCodeException, IOException{
		CookiesUtils utils = new CookiesUtils();
		String[] buvid = getBUVID();
		cookies = cookies+" buvid3="+buvid[0]+"; "+"buvid4="+buvid[1]+"; ";
		utils.updateToken(cookies);
		WebModel content = URLget.BackWebWithMoreInfo(URLget.GET_BUVID_FP_URL);
		String buvid_fp = FindString.findValue(content.content, "buvid_fp");
		//enable_content = content.content;
		cookies = cookies+"buvid_fp="+buvid_fp+"; ";
		utils.updateToken(cookies);
		content = URLget.BackWebWithMoreInfo(URLget.GET_UUID_URL);
		String uuid = FindString.findValue(content.content, "uuid");
		cookies = cookies+"_uuid="+uuid+"; ";
		utils.updateToken(cookies);
		long timestamp = System.currentTimeMillis();
		String payload = "{\"3064\":1,\"5062\":\""+timestamp+"\",\"03bf\":\"https%3A%2F%2Fwww.bilibili.com%2F\",\"39c8\":\"333.1007.fp.risk\",\"34f1\":\"\",\"d402\":\"\",\"654a\":\"\",\"6e7c\":\"839x959\",\"3c43\":{\"2673\":1,\"5766\":24,\"6527\":0,\"7003\":1,\"807e\":1,\"b8ce\":\"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36\",\"641c\":0,\"07a4\":\"zh-CN\",\"1c57\":8,\"0bd0\":16,\"748e\":[2560,1080],\"d61f\":[2467,1091],\"fc9d\":480,\"6aa9\":\"Asia/Hong_Kong\",\"75b8\":1,\"3b21\":1,\"8a1c\":0,\"d52f\":\"not available\",\"adca\":\"Win32\",\"80c9\":[[\"PDF Viewer\",\"Portable Document Format\",[[\"application/pdf\",\"pdf\"],[\"text/pdf\",\"pdf\"]]],[\"Chrome PDF Viewer\",\"Portable Document Format\",[[\"application/pdf\",\"pdf\"],[\"text/pdf\",\"pdf\"]]],[\"Chromium PDF Viewer\",\"Portable Document Format\",[[\"application/pdf\",\"pdf\"],[\"text/pdf\",\"pdf\"]]],[\"Microsoft Edge PDF Viewer\",\"Portable Document Format\",[[\"application/pdf\",\"pdf\"],[\"text/pdf\",\"pdf\"]]],[\"WebKit built-in PDF\",\"Portable Document Format\",[[\"application/pdf\",\"pdf\"],[\"text/pdf\",\"pdf\"]]]],\"13ab\":\"mTUAAAAASUVORK5CYII=\",\"bfe9\":\"aTot0S1jJ7Ws0JC6QkvAL/A4H1PbV+/QA3AAAAAElFTkSuQmCC\",\"a3c1\":[],\"6bc5\":\"Google Inc. (NVIDIA) #X3fQVPgERx~ANGLE (NVIDIA, NVIDIA GeForce RTX 3060 Laptop GPU (0x00002560) Direct3D11 vs_5_0 ps_5_0, D3D11) #X3fQVPgERx\",\"ed31\":0,\"72bd\":0,\"097b\":0,\"52cd\":[0,0,0],\"a658\":[\"Arial\",\"Arial Black\",\"Arial Narrow\",\"Book Antiqua\",\"Bookman Old Style\",\"Calibri\",\"Cambria\",\"Cambria Math\",\"Century\",\"Century Gothic\",\"Century Schoolbook\",\"Comic Sans MS\",\"Consolas\",\"Courier\",\"Courier New\",\"Georgia\",\"Helvetica\",\"Helvetica Neue\",\"Impact\",\"Lucida Bright\",\"Lucida Calligraphy\",\"Lucida Console\",\"Lucida Fax\",\"Lucida Handwriting\",\"Lucida Sans\",\"Lucida Sans Typewriter\",\"Lucida Sans Unicode\",\"Microsoft Sans Serif\",\"Monotype Corsiva\",\"MS Gothic\",\"MS PGothic\",\"MS Reference Sans Serif\",\"MS Sans Serif\",\"MS Serif\",\"Palatino Linotype\",\"Segoe Print\",\"Segoe Script\",\"Segoe UI\",\"Segoe UI Light\",\"Segoe UI Semibold\",\"Segoe UI Symbol\",\"Tahoma\",\"Times\",\"Times New Roman\",\"Trebuchet MS\",\"Verdana\",\"Wingdings\",\"Wingdings 2\",\"Wingdings 3\"],\"d02f\":\"124.04347527516074\"},\"8b94\":\"\",\"df35\":\""+uuid+"\",\"07a4\":\"zh-CN\",\"5f45\":null,\"db46\":0}";
		payload = FindString.replace(payload, "\"", "\\\"");
		payload = "{\"payload\":\""+payload+"\"}";
		content = URLget.BackWebAndUserCookiesPost(URLget.EXCLIMBWUZHI_URL, payload, cookies, 2);
	}
	
	private String[] getBUVID() throws WebReturnErrorCodeException, IOException{
		WebModel web = URLget.BackWebWithMoreInfo(URLget.GET_BUVID3_URL);
		//displayInfoAlert(web.content);
		String[] buvid = new String[2];
		buvid[0] = FindString.findValue(web.content, "b_3"); //buvid3
		buvid[1] = FindString.findValue(web.content, "b_4"); //buvid4
		return buvid;
	}
	

}
