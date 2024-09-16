package top.jiehuan.kilikili;


import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;


import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.io.HttpsConnection;
import javax.microedition.lcdui.*;

public class MainMIDlet extends MIDlet implements CommandListener{
	
	public Display display;
	public Form form;
	StringItem string;
	TextField tf;
	Command go;
	Command exit;
	
	
	private String goString;
	private String exitString;
	private String main_pageString;
	private String inputString;
	private String invalid_bvidStirng;
	
	public String lang;
	/*public MainMIDlet(){
        display = Display.getDisplay(this);
        form = new Form("JSON Response");
        exitCommand = new Command("Exit", Command.EXIT, 1);
        form.addCommand(exitCommand);
        form.setCommandListener((CommandListener) this);
    }*/

	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
		// TODO Auto-generated method stub
		
	}

	protected void pauseApp() {
		// TODO Auto-generated method stub

	}

	protected void startApp() throws MIDletStateChangeException {
		lang = System.getProperty("microedition.locale");

		loadMessages();
		
		display = Display.getDisplay(this);
		go = new Command(goString,Command.OK,1);
		exit = new Command(exitString,Command.EXIT,0);
		form = new Form(main_pageString);
		tf = new TextField(inputString,"",20,TextField.ANY);
		form.append(tf);
		form.addCommand(go);
		form.addCommand(exit);
		form.setCommandListener(this);
		display.setCurrent(form);
	}
	
	public void commandAction(Command c, Displayable d) {
        if (c == go) {
        	
        	if(tf.getString().length()==10){
        		new Thread(new Runnable() {
                    public void run() {
                    	String bvid = tf.getString();
                        new GetVideoInfoPage(MainMIDlet.this, bvid);
                    }
                }).start();
        	}else{
        		Alert alert = new Alert("Error", invalid_bvidStirng, null, AlertType.ERROR);
                alert.setTimeout(Alert.FOREVER); // 设置为永远显示，直到用户操作
                display.setCurrent(alert, form);
        	}
           
            //GetVideoInfoPage secondMIDlet = new GetVideoInfoPage(text);
            //display.setCurrent(secondMIDlet.getForm());
            //notifyPaused();
        }else if(c==exit){
        	exitApp();
        }
    }
	void exitApp() {
        try {
			destroyApp(false);
		} catch (MIDletStateChangeException e) {
			// TODO Auto-generated catch block
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
        } else {
        	goString="go";
        	exitString="exit";
        	main_pageString="Main Page";
        	inputString="input bvid: ";
        	invalid_bvidStirng="invalid bvid";
        }
    }

}