package top.jiehuan.kilikili.Page;

import java.io.IOException;
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

import top.jiehuan.kilikili.MainMIDlet;
import top.jiehuan.kilikili.PageInfo;
import top.jiehuan.kilikili.util.GetLangRes;

public class MainPage implements CommandListener{
	public static short PageID = 0;
	
	GetLangRes lang_res;
	Display display;
	Form form;
	StringItem tips;
	TextField tf;
	Command go;
	Command exit;
	Command about;
	Command rcmd;
	Command search;
	
	public String lang;
	
	private MainMIDlet m;
	private PageInfo page_info;
	private Vector page_info_list;
	
	public MainPage(MainMIDlet m){
		this.m=m;
		lang = System.getProperty("microedition.locale");
		
		loadMessages();
		
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
                    	PageInfo newpage = new PageInfo(GetVideoInfoPage.PageID);
                    	newpage.setVideoInfo(bvid);
                    	page_info_list.addElement(newpage);
                        new GetVideoInfoPage(m, page_info_list);
                    }
                }).start();
        	}else if(tf.getString().length()==10){
        		new Thread(new Runnable() {
                    public void run() {
                    	String bvid = "BV"+tf.getString();
                    	PageInfo newpage = new PageInfo(GetVideoInfoPage.PageID);
                    	newpage.setVideoInfo(bvid);
                    	page_info_list.addElement(newpage);
                        new GetVideoInfoPage(m, page_info_list);
                    }
                }).start();
        	}else{
        		//处理输入错误
        		Alert alert = new Alert("Error", lang_res.getValue("invalid_bvid"), null, AlertType.ERROR);
                alert.setTimeout(Alert.FOREVER); // 设置为永远显示，直到用户操作
                display.setCurrent(alert, form);
        	}
        }else if(c==exit){
        	m.exitApp();//退出app
        }else if(c==about){
        	new Thread(new Runnable() {
                public void run() {
                	page_info_list.addElement(new PageInfo(AboutPage.PageID));
                    new AboutPage(m,page_info_list); //打开关于界面
                }
            }).start();
        }else if(c==rcmd){
        	new Thread(new Runnable() {
                public void run() {
                	page_info_list.addElement(new PageInfo(RecommendPage.PageID));
                    new RecommendPage(m,page_info_list); //打开推荐界面
                }
            }).start();
        }else if(c==search){
        	new Thread(new Runnable() {
                public void run() {
                	System.out.println("search button");
                	/*String search_text;
					try {
						search_text = new String( tf.getString().getBytes( "utf8" ), "utf8" );
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						search_text = tf.getString();
					}*/
                    /*Alert alert = new Alert("Keyword", tf.getString(), null, AlertType.INFO);
                    alert.setTimeout(Alert.FOREVER); // 设置为永远显示，直到用户操作
                    display.setCurrent(alert); // 显示 Alert*/
                	System.out.println("keyword:"+tf.getString());
                	PageInfo newpage = new PageInfo(SearchPage.PageID);
                	newpage.setSearchInfo(tf.getString(), 1);
                	page_info_list.addElement(newpage);
                	System.out.println("go to SearchPage");
                    new SearchPage(m,page_info_list); //打开搜索界面
                }
            }).start();
        }
    }
	private void loadMessages() {
        // 根据系统语言加载相应的资源文件
        try {
			lang_res = new GetLangRes(lang);
			//System.out.println(lang_res.getLangFileContent());
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
	private void initPageVars(){
		lang = System.getProperty("microedition.locale");
		page_info = new PageInfo(PageID);
		page_info_list = new Vector();
		page_info_list.addElement(page_info);
		page_info = new PageInfo(PageID);
		page_info_list = new Vector();
		page_info_list.addElement(page_info);
	}
	
	private void initDisplayVars(){
		System.out.println("Start init the display moudle");
		
		display = Display.getDisplay(m);
		go = new Command(lang_res.getValue("go"),Command.OK,0);
		exit = new Command(lang_res.getValue("exit"),Command.EXIT,1);
		about = new Command(lang_res.getValue("about"),Command.OK,1);
		rcmd = new Command(lang_res.getValue("rcmd_list"),Command.OK,1);
		search = new Command(lang_res.getValue("search"),Command.OK,1);
		form = new Form(lang_res.getValue("main_page"));
		tf = new TextField(lang_res.getValue("input"),"",20,TextField.ANY);
		tips = new StringItem("","\n"+lang_res.getValue("tips"));
		
		System.out.println("Finish init the display moudle");
	}
	
	private void display(){
		form.append(tf);
		form.append(tips);
		form.addCommand(go);
		form.addCommand(exit);
		form.addCommand(search);
		form.addCommand(rcmd);
		form.addCommand(about);
		form.setCommandListener(this);
		display.setCurrent(form);
	}
}
