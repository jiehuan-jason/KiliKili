package top.jiehuan.kilikili;

import java.io.IOException;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.ImageItem;
import javax.microedition.lcdui.StringItem;
import javax.microedition.midlet.MIDlet;


public class GetVideoInfoPage implements CommandListener{

	public Display display;
	public Form form;
	public StringItem string;
	public StringItem up_name=null;
	StringItem view;
	StringItem reply;
	StringItem coin;
	StringItem share;
	StringItem like;
	public String bvid;
	Image image;
	Command back;
	Command exit;
	private MainMIDlet ml;
    /*public MainMIDlet(){
        display = Display.getDisplay(this);
        form = new Form("JSON Response");
        exitCommand = new Command("Exit", Command.EXIT, 1);
        form.addCommand(exitCommand);
        form.setCommandListener((CommandListener) this);
    }*/
	public GetVideoInfoPage(MainMIDlet midlet,String bvid){
		this.bvid=bvid;
		ml=midlet;
		display = Display.getDisplay(midlet);
		URLget video_info = new URLget("http://192.168.1.2:3000/view?bvid="+bvid);
		String[] s_info= video_info.sendGetRequest();
		String title = null;
		if(s_info[0].equals("error")){
			title="error";
			string=new StringItem(null, s_info[1]);
			up_name=new StringItem(null,"null");
		}else{
			//title=get_Title(s_info[1]);
			title=URLget.findValue(s_info[1],"title");
			string=new StringItem(null, title);
			up_name=new StringItem(null,"up主："+URLget.findValue(s_info[1], "name"));
			view = new StringItem(null,"看"+URLget.findValueInt(s_info[1],"view")+"次");
			reply = new StringItem(null,"回"+URLget.findValueInt(s_info[1],"reply")+"条");
			coin = new StringItem(null,"币"+URLget.findValueInt(s_info[1],"coin")+"个");
			share = new StringItem(null,"转"+URLget.findValueInt(s_info[1],"share")+"次");
			like = new StringItem(null,"赞"+URLget.findValueInt(s_info[1],"like")+"次");
		}
		
		back=new Command("Back",Command.BACK,1);
		exit=new Command("Exit",Command.EXIT,0);
		//loadImage(URLget.findValue(s_info[1], "name"));
		form=new Form("视频界面");
		form.append(string);
		form.append(up_name);
		form.append(view);
		form.append(like);
		form.append(coin);
		form.append(share);
		form.append(reply);
		form.addCommand(back);
		form.addCommand(exit);
		form.setCommandListener(this);
		//loadImage(URLget.findValue(s_info[1], "pic"));
		display.setCurrent(form);
	}
	 /*private void loadImage(final String url) {
	        new Thread(new Runnable() {
	            public void run() {
	                try {
	                    image = Image.createImage(url);
	                    // 创建 ImageItem 并添加到表单
	                    ImageItem imageItem = new ImageItem(null, image, ImageItem.LAYOUT_CENTER, null);
	                    form.append(imageItem);
	                    display.setCurrent(form); // 显示表单
	                } catch (IOException e) {
	                	System.out.println(e.getMessage());
	                    e.printStackTrace();
	                }
	            }
	        }).start();
	    }*/
	
	/*private String get_Title(String j_info){
		try {
			JSONObject data=new JSONObject(new JSONObject(j_info).getString("data"));
			return data.getString("title");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			try {
				if(new JSONObject(j_info).getString("data").toString().equals("-400")){
					return "找不到对应的视频";
				}
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return "get_Title error: "+e1.getMessage()+'\n'+j_info;
			}
			return "get_Title error: "+e.getMessage()+'\n'+j_info;
		}
	}*/
	 public void commandAction(Command c, Displayable d) {
	        if (c == back) {
	        	
	           
	            new Thread(new Runnable() {
	                public void run() {
	                	ml.display.setCurrent(ml.form);
	                }
	            }).start();
	            //GetVideoInfoPage secondMIDlet = new GetVideoInfoPage(text);
	            //display.setCurrent(secondMIDlet.getForm());
	            //notifyPaused();
	        }
	        if(c==exit){
	        	ml.exitApp();
	        }
	    }
	 
}
