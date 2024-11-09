package top.jiehuan.kilikili;

import java.io.IOException;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;

public class MainPage implements CommandListener{
	public static String pagelist[] = new String[100];
	public static int page_list_num;
	static String PageID = "0";
	
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
	
	public MainPage(MainMIDlet m){
		this.m=m;
		lang = System.getProperty("microedition.locale");

		loadMessages();
		
		page_list_num = 0;
		pagelist[0]=PageID;
		//page_list_num++;
		
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
	
	public void commandAction(Command c, Displayable d) {
        if (c == go) //前往视频信息页面
        {
        	if(tf.getString().length()==12&&tf.getString().startsWith("BV")){
        		new Thread(new Runnable() {
                    public void run() {
                    	String bvid = tf.getString();
                        new GetVideoInfoPage(m, new VideoInfo(bvid,0,pagelist));
                    }
                }).start();
        	}else if(tf.getString().length()==10){
        		new Thread(new Runnable() {
                    public void run() {
                    	String bvid = tf.getString();
                        new GetVideoInfoPage(m, new VideoInfo("BV"+bvid,0,pagelist));
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
                    new AboutPage(m,new VideoInfo(0,pagelist)); //打开关于界面
                }
            }).start();
        }else if(c==rcmd){
        	new Thread(new Runnable() {
                public void run() {
                    new RecommendPage(m,new VideoInfo(0,pagelist)); //打开推荐界面
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
                    new SearchPage(m,new VideoInfo(0,pagelist,tf.getString())); //打开搜索界面
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
	static public int addPageNum(String PageID,VideoInfo video_info)
	{
		int index = video_info.getPageNum();
		String[] pagelist = video_info.getPageList();
		index+=1;
		System.out.println("call addPage Num.Page is "+PageID+" list num is "+index);
		//System.out.println(pagelist);
		if(!pagelist[index-1].equals(PageID)){
			pagelist[index]=PageID;
			
		}else{
			index--;
		}
		return index;
	}
}
