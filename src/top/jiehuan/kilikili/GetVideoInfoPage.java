package top.jiehuan.kilikili;

import java.io.UnsupportedEncodingException;

import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.StringItem;


public class GetVideoInfoPage implements CommandListener{

	Display display;
	Form form;
	StringItem string;
	StringItem up_name=null;
	StringItem view;
	StringItem reply;
	StringItem coin;
	StringItem share;
	StringItem like;
	String desc;
	StringItem info;
	StringItem ln;
	String pic;
	
	public String bvid;
	Image image;
	Command back;
	Command exit;
	Command download;
	Command view_cover;
	private MainMIDlet ml;
	String video_url;
	String cid;

	public GetVideoInfoPage(MainMIDlet midlet,String bvid){
		//初始化需要用到的变量 输入的bvid要求前面带上BV两个字母
		this.bvid=bvid;
		ml=midlet;
		display = Display.getDisplay(midlet);
		
		//获取视频信息
		String[] s_info= URLget.sendGetRequest("http://localhost:3000/view?bvid="+bvid);
		String title = null;
		System.out.println("status:"+s_info[0]);
		
		//若返回代码为错误代码，则显示未找到视频
		if(s_info[0].equals("error")){
			form=new Form("未找到该视频");
			back=new Command("Back",Command.BACK,1);
			exit=new Command("Exit",Command.EXIT,0);
			form.addCommand(back);
			form.addCommand(exit);
			form.setCommandListener(this);
			Alert alert = new Alert("Error", s_info[1], null, AlertType.ERROR);
            alert.setTimeout(Alert.FOREVER); // 设置为永远显示，直到用户操作
            display.setCurrent(alert, form);
            ml.display.setCurrent(ml.form);
            
		}else{
			// 初始化视频信息界面
			title=FindString.findValue(s_info[1],"title");
			string=new StringItem(null, title);
			up_name=new StringItem(null,"\nup主："+FindString.findValue(s_info[1], "name"));
			desc = "\n简介:"+FindString.findValueInt(s_info[1],"desc").substring(1, FindString.findValueInt(s_info[1],"desc").length() - 1);
			info = new StringItem(null,"\n看"+FindString.findValueInt(s_info[1],"view")+"次  "+"回"+FindString.findValueInt(s_info[1],"reply")+"条  "+"币"+FindString.findValueInt(s_info[1],"coin")+"个  "+"转"+FindString.findValueInt(s_info[1],"share")+"次  "+"赞"+FindString.findValueInt(s_info[1],"like")+"次");
			cid=FindString.findValueInt(s_info[1], "cid");
			pic=FindString.findValue(s_info[1], "pic");
			System.out.println(cid);
			video_url=URLget.BackVideoLink(bvid, cid);
			System.out.println("Get Already");
			download=new Command("Download",Command.ITEM,1);
			back=new Command("Back",Command.BACK,1);
			exit=new Command("Exit",Command.EXIT,0);
			view_cover=new Command("View the Cover",Command.ITEM,2);
			form=new Form("视频界面");
			form.append(string);
			form.append(up_name);
			if(!desc.equals("\n简介:")){
				String[] items=FindString.Display_Desc(desc);
				for(int i=0;i<items.length;i++){
					form.append(new StringItem(null,items[i]));
				}
				form.append(new StringItem(null,""));
			}
			
			form.append(info);
			form.addCommand(back);
			form.addCommand(exit);
			form.addCommand(download);
			form.addCommand(view_cover);
			form.setCommandListener(this);
			display.setCurrent(form);
		}
		
	}
	 public void commandAction(Command c, Displayable d) {
		 //返回主界面
	        if (c == back) {
	            new Thread(new Runnable() {
	                public void run() {
	                	ml.display.setCurrent(ml.form);
	                }
	            }).start();
	        }
	        //退出app
	        if(c==exit){
	        	ml.exitApp();
	        }
	        //下载视频
	        if(c==download){
	        	new Thread(new Runnable() {
                    public void run() {
                    	try {
                    		System.out.println("video_url is:"+video_url);
							ml.platformRequest(video_url);
                    	} catch (ConnectionNotFoundException e) {
							e.printStackTrace();
						}
                    	
                    }
	            }).start();
	        }
	        // 显示视频封面
	        if(c==view_cover){
	        	new Thread(new Runnable() {
                    public void run() {
                    	try {
                    		System.out.println("cover_url is:"+pic);
							ml.platformRequest(new String(pic.getBytes("UTF-8"),"UTF-8"));
						} catch (ConnectionNotFoundException e) {
							e.printStackTrace();
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
                    	
                    }
	            }).start();
	        }
	    }
	 
	 
	 

}