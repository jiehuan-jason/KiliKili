package top.jiehuan.kilikili;


import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;




import top.jiehuan.kilikili.Page.MainPage;


public class MainMIDlet extends MIDlet{
	
	protected void destroyApp(boolean arg0) {
		
	}

	protected void pauseApp() {

	}

	protected void startApp(){
		/*Vector page_info_list = new Vector();
		PageInfo newpage = new PageInfo(MainPage.PageID,this);
		newpage.setVideoInfo(TEST_BVID);
		page_info_list.addElement(newpage);*/
		//canvas = new DebugCanvas();
		//display = Display.getDisplay(this);
        //display.setCurrent(canvas);
        //init();
        
		new MainPage(this);
	}
	public void exitApp() {
        try {
			destroyApp(false);
		} catch (Exception e) {
			e.printStackTrace();
		} // 销毁应用
        notifyDestroyed(); // 通知 MIDP 退出
    }
	
	/*private void init(){
		try{
			CookiesUtils cookies_utils = new CookiesUtils();
			if(cookies_utils.isTokenStored()){
				String cookies = cookies_utils.loadToken();
				CookiesUtils refresh_util = new CookiesUtils("refresh_token");
				String refresh_token = refresh_util.loadToken();
				String csrf = FindString.findValueInCookies(cookies, "bili_jct");
				String content = URLget.BackWeb(URLget.GET_CSRF_REFRESH_TOKEN_STATUTS_URL);
				
				canvas.log(refresh_token+"\n");
				canvas.log(cookies+"\n");
				canvas.log(csrf+'\n');
				canvas.log(content+"\n");
				//if(FindString.findValueBool(content, "refresh").equals("true")){
					
				//}
				canvas.log("1"+"\n");
				long time = System.currentTimeMillis();
				canvas.log("1"+"\n");
				String content = URLget.BackWeb(URLget.GET_CorrespondPath_TIMESTAMP_URL+"?t="+String.valueOf(time));
				canvas.log("1"+"\n");
				String hash = FindString.findValue(content, "hash");
				canvas.log(hash);
				WebModel web = URLget.BackWebWithMoreInfo(URLget.GET_REFRESH_CSRF_URL+hash);
				String refresh_csrf = FindString.findValueInHTMLLabel(web.content);
				canvas.log(web.content);
				canvas.log(refresh_csrf);
				web = URLget.BackWebAndCookiesPost(URLget.REFRESH_COOKIES_URL, "csrf="+csrf+"&refresh_csrf="+refresh_csrf+"&source=main_web&refresh_token="+refresh_token);
				//refresh_token = FindString.findValue(web.content, "refresh_token");
				//refresh_token保持旧值 后面post将会用到
				canvas.log(web.content);
				canvas.log(FindString.findValue(web.content, "refresh_token"));
				refresh_util.updateToken(FindString.findValue(web.content, "refresh_token"));
				cookies_utils.updateToken(web.cookies);
				csrf = FindString.findValueInCookies(web.cookies, "bili_jct");
				canvas.log(csrf);
				web = URLget.BackWebAndCookiesPost(URLget.REFRESH_COOKIES_COMFIRM_URL,"csrf="+csrf+"&refresh_token="+refresh_token);
			}	
		}catch(Exception e){
			
		}
	}*/
	

}
class MyCanvas extends Canvas {

    public MyCanvas() {
    }

    protected void paint(Graphics g) {
        // 清空背景为白色
        g.setColor(255, 255, 255);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(0, 0, 0);
        g.drawString("KiliKili is Loading......", getWidth() / 2, getHeight() / 2, Graphics.HCENTER | Graphics.BASELINE);
    }
}