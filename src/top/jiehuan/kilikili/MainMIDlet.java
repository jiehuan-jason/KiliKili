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
		
		display = Display.getDisplay(this);
		go = new Command("Go",Command.OK,1);
		exit = new Command("Exit",Command.EXIT,0);
		form = new Form("首页");
		tf = new TextField("输入bvid:","",20,TextField.ANY);
		form.append(tf);
		form.addCommand(go);
		form.addCommand(exit);
		form.setCommandListener(this);
		display.setCurrent(form);
		
	}
	
	public void commandAction(Command c, Displayable d) {
        if (c == go) {
        	
           
            new Thread(new Runnable() {
                public void run() {
                	String text = tf.getString();
                    new GetVideoInfoPage(MainMIDlet.this, text);
                }
            }).start();
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

}
