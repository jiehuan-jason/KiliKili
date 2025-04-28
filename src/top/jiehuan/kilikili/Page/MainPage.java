package top.jiehuan.kilikili.Page;

import java.util.Vector;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;
import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordStoreException;

import top.jiehuan.kilikili.MainMIDlet;
import top.jiehuan.kilikili.Model.PageInfo;
import top.jiehuan.kilikili.util.CookieDateParser;
import top.jiehuan.kilikili.util.CookiesUtils;
import top.jiehuan.kilikili.util.FindString;

public class MainPage extends Page implements CommandListener{
	public static final short PageID = 0;
	
	Form form;
	StringItem tips;
	TextField tf;
	Command go;
	Command about;
	Command rcmd;
	Command search;
	Command back;
	Command mine;
	
	public MainPage(MainMIDlet ml){
		super(ml);
		
		try{
			getTheCookiesExpiresAndCompare();
		}catch(Exception e){
			displayErrorAlert(e.getMessage());
		}
		initPageVars();
		initDisplayVars();
		display();
	}
	
	public void commandAction(Command c, Displayable d) {
        if (c == go) //前往视频信息页面
        {
        	if(tf.getString().length()==12&&tf.getString().startsWith("BV")){
        		new Thread(new Runnable() {
                    public void run() {
                    	String bvid = tf.getString();
                    	PageInfo newpage = new PageInfo(PartVideoListPage.PageID,ml);
                    	try {
							newpage.setVideoInfo(bvid);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							displayErrorAlert(e.getMessage());
						}
                    	page_info_list.addElement(newpage);
                        new PartVideoListPage(page_info_list);
                    }
                }).start();
        	}else if(tf.getString().length()==10){
        		new Thread(new Runnable() {
                    public void run() {
                    	String bvid = "BV"+tf.getString();
                    	PageInfo newpage = new PageInfo(PartVideoListPage.PageID,ml);
                    	try {
							newpage.setVideoInfo(bvid);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							displayErrorAlert(e.getMessage());
						}
                    	page_info_list.addElement(newpage);
                        new PartVideoListPage(page_info_list);
                    }
                }).start();
        	}else{
        		//处理输入错误
        		Alert alert = new Alert("Error", lang_res.getValue("invalid_bvid"), null, AlertType.ERROR);
                alert.setTimeout(Alert.FOREVER); // 设置为永远显示，直到用户操作
                display.setCurrent(alert, form);
        	}
        }else if(c==exit){
        	ml.exitApp();//退出app
        }else if(c==about){
        	new Thread(new Runnable() {
                public void run() {
                	page_info_list.addElement(new PageInfo(AboutPage.PageID,ml));
                    new AboutPage(page_info_list); //打开关于界面
                }
            }).start();
        }else if(c==rcmd){
        	new Thread(new Runnable() {
                public void run() {
                	page_info_list.addElement(new PageInfo(RecommendPage.PageID,ml));
                    new RecommendPage(page_info_list); //打开推荐界面
                }
            }).start();
        }else if(c==search){
        	if(tf.getString().equals(""))
        		displayErrorAlert("输入不能为空");
        	else
        	new Thread(new Runnable() {
                public void run() {
                	System.out.println("search button");
                	System.out.println("keyword:"+tf.getString());
                	PageInfo newpage = new PageInfo(SearchPage.PageID,ml);
                	newpage.setSearchInfo(tf.getString(), 1);
                	page_info_list.addElement(newpage);
                	System.out.println("go to SearchPage");
                    new SearchPage(page_info_list); //打开搜索界面
                }
            }).start();
        }else if(c==back){
        	display.setCurrent(form);
        }else if(c==mine){
        	new Thread(new Runnable() {
                public void run() {
                	page_info_list.addElement(new PageInfo(MyInfoPage.PageID,ml));
                    new MyInfoPage(page_info_list); //打开我的界面
                }
            }).start();
        }
    }
	
	protected void initPageVars(){
		page_info = new PageInfo(PageID,ml);
		page_info_list = new Vector();
		page_info_list.addElement(page_info);
	}
	
	protected void initDisplayVars(){
		System.out.println("Start init the display moudle");
		
		display = Display.getDisplay(ml);
		go = new Command(lang_res.getValue("go"),Command.OK,0);
		exit = new Command(lang_res.getValue("exit"),Command.EXIT,1);
		about = new Command(lang_res.getValue("about"),Command.OK,1);
		rcmd = new Command(lang_res.getValue("rcmd_list"),Command.OK,1);
		search = new Command(lang_res.getValue("search"),Command.OK,1);
		mine = new Command(lang_res.getValue("mine"),Command.OK,1);
		form = new Form(lang_res.getValue("main_page"));
		tf = new TextField(lang_res.getValue("input"),"",20,TextField.ANY);
		tips = new StringItem("","\n"+lang_res.getValue("tips"));
		
		System.out.println("Finish init the display moudle");
	}
	
	protected void display(){
		form.append(tf);
		form.append(tips);
		form.addCommand(go);
		form.addCommand(exit);
		form.addCommand(search);
		form.addCommand(rcmd);
		form.addCommand(mine);
		form.addCommand(about);
		form.setCommandListener(this);
		display.setCurrent(form);
	}
	
	private void getTheCookiesExpiresAndCompare() throws InvalidRecordIDException, RecordStoreException{
		CookiesUtils util = new CookiesUtils();
		String cookies = util.loadToken();
		String expires = FindString.findValueInCookies(cookies, "Expires");
		long expireTime = CookieDateParser.parseCookieDate(expires);
		//displayErrorAlert(String.valueOf(expireTime));
		if(expireTime < System.currentTimeMillis() / 1000 - 3600){
			util.deleteToken();
			displayErrorAlert("你的登录已过期，请重新登录！");
		}
	}
}
