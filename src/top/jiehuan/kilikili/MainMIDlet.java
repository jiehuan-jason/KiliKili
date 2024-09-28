package top.jiehuan.kilikili;

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import javax.microedition.lcdui.*;

public class MainMIDlet extends MIDlet implements CommandListener{
	
	public Display display;
	public Form form;
	StringItem string;
	TextField tf;
	Command go;
	Command exit;
	Command about;
	Command rcmd;
	Command search;
	
	private String goString;
	private String exitString;
	private String main_pageString;
	private String inputString;
	private String invalid_bvidStirng;
	private String aboutString;
	private String rcmd_listString;
	private String searchString;
	
	public String lang;

	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
		// TODO Auto-generated method stub
		
	}

	protected void pauseApp() {
		// TODO Auto-generated method stub

	}

	
	/*
	 * startApp()
	 * 初始化程序，初始化控件和命令，显示首页
	 * 
	 * */
	protected void startApp() throws MIDletStateChangeException {
		lang = System.getProperty("microedition.locale");

		loadMessages();
		
		System.out.println("Start init the display moudle");
		
		display = Display.getDisplay(this);
		go = new Command(goString,Command.OK,0);
		exit = new Command(exitString,Command.EXIT,1);
		about = new Command(aboutString,Command.OK,1);
		rcmd = new Command(rcmd_listString,Command.OK,1);
		search = new Command(searchString,Command.OK,1);
		form = new Form(main_pageString);
		tf = new TextField(inputString,"",10,TextField.ANY);
		
		System.out.println("Finish init the display moudle");
		
		form.append(tf);
		form.addCommand(go);
		form.addCommand(exit);
		form.addCommand(search);
		form.addCommand(rcmd);
		form.addCommand(about);
		form.setCommandListener(this);
		display.setCurrent(form);
	}
	/*
	 * commandAction()
	 * 命令处理
	 * */
	public void commandAction(Command c, Displayable d) {
        if (c == go) //前往视频信息页面
        {
        	if(tf.getString().length()==10){
        		new Thread(new Runnable() {
                    public void run() {
                    	String bvid = tf.getString();
                        new GetVideoInfoPage(MainMIDlet.this, "BV"+bvid);
                    }
                }).start();
        	}else{
        		//处理输入错误
        		Alert alert = new Alert("Error", invalid_bvidStirng, null, AlertType.ERROR);
                alert.setTimeout(Alert.FOREVER); // 设置为永远显示，直到用户操作
                display.setCurrent(alert, form);
        	}
        }else if(c==exit){
        	exitApp();//退出app
        }else if(c==about){
        	new Thread(new Runnable() {
                public void run() {
                    new AboutPage(MainMIDlet.this); //打开关于界面
                }
            }).start();
        }else if(c==rcmd){
        	new Thread(new Runnable() {
                public void run() {
                    new RecommendPage(MainMIDlet.this); //打开推荐界面
                }
            }).start();
        }else if(c==search){
        	new Thread(new Runnable() {
                public void run() {
                	System.out.println("search button");
                	System.out.println("keyword:"+tf.getString());
                    new SearchPage(MainMIDlet.this,tf.getString()); //打开搜索界面
                }
            }).start();
        }
    }
	void exitApp() {
        try {
			destroyApp(false);
		} catch (MIDletStateChangeException e) {
			e.printStackTrace();
		} // 销毁应用
        notifyDestroyed(); // 通知 MIDP 退出
    }
	private void loadMessages() {
        // 根据系统语言加载相应的资源文件
        
        if (lang.equals("zh-CN")) {
        	goString="前往";
        	exitString="退出";
        	main_pageString="首页";
        	inputString="输入bvid：";
        	invalid_bvidStirng="无效的bvid号";
        	aboutString="关于";
        	rcmd_listString="推荐列表";
        	searchString="搜索";
        } else {
        	goString="Go";
        	exitString="Exit";
        	main_pageString="Main Page";
        	inputString="input bvid: ";
        	invalid_bvidStirng="invalid bvid";
        	aboutString="About";
        	rcmd_listString="Recommend List";
        	searchString="Search";
        }
    }
	

}