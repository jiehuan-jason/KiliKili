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
	
	String findErrorString;
	String backString;
	String exitString;
	String likeString;
	String viewString;
	String replyString;
	String coinString;
	String shareString;
	String introductionString;
	String authorString;
	String videoDisplayString;
	String downloadString;
	String coverString;
	String authorInfoString;
	String ci;
	String ge;

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
	String mid;
	
	public String bvid;
	Image image;
	Command back;
	Command exit;
	Command download;
	Command view_cover;
	Command author_info;
	private MainMIDlet ml;
	String video_url;
	String cid;

	public GetVideoInfoPage(MainMIDlet midlet,String bvid){
		//初始化需要用到的变量 输入的bvid要求前面带上BV两个字母
		this.bvid=bvid;
		ml=midlet;
		display = Display.getDisplay(midlet);
		
		loadMessages();
		
		//获取视频信息
		String[] s_info= URLget.sendGetRequest(bvid);
		String title = null;
		System.out.println("status:"+s_info[0]);
		
		//若返回代码为错误代码，则显示未找到视频
		if(s_info[0].equals("error")){
			form=new Form(findErrorString);
			back=new Command(backString,Command.BACK,1);
			exit=new Command(exitString,Command.EXIT,0);
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
			up_name=new StringItem(null,"\n"+authorString+FindString.findValue(s_info[1], "name"));
			desc = "\n"+introductionString+FindString.findValueInt(s_info[1],"desc").substring(1, FindString.findValueInt(s_info[1],"desc").length() - 1);
			info = new StringItem(null,"\n"+viewString+FindString.findValueInt(s_info[1],"view")+ci+"  "+replyString+FindString.findValueInt(s_info[1],"reply")+ci+"  "+coinString+FindString.findValueInt(s_info[1],"coin")+ge+"  "+shareString+FindString.findValueInt(s_info[1],"share")+ci+"  "+likeString+FindString.findValueInt(s_info[1],"like")+ci);
			cid=FindString.findValueInt(s_info[1], "cid");
			pic=FindString.findValue(s_info[1], "pic");
			mid=FindString.findValueInt(s_info[1], "mid");
			System.out.println(cid);
			video_url=URLget.BackVideoLink(bvid, cid);
			System.out.println("Get Already");
			download=new Command(downloadString,Command.ITEM,1);
			back=new Command(backString,Command.BACK,1);
			exit=new Command(exitString,Command.EXIT,0);
			view_cover=new Command(coverString,Command.ITEM,2);
			author_info=new Command(authorInfoString,Command.ITEM,2);
			form=new Form(videoDisplayString);
			form.append(string);
			form.append(up_name);
			if(!desc.equals("\n"+introductionString)){
				String[] items=FindString.Display_Desc(desc);
				for(int i=0;i<items.length;i++){
					form.append(new StringItem(null,items[i]));
				}
				form.append(new StringItem(null,""));
			}
			
			form.append(info);
			form.addCommand(back);
			form.addCommand(author_info);
			form.addCommand(download);
			form.addCommand(view_cover);
			form.addCommand(exit);
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
	        }if (c == author_info) {
	            new Thread(new Runnable() {
	                public void run() {
	                	new UserInfoPage(ml,mid,bvid);
	                }
	            }).start();
	        }
	    }
	 
	 private void loadMessages() {
	        // 根据系统语言加载相应的资源文件
	        
	        if (System.getProperty("microedition.locale").equals("zh-CN")) {
	        	exitString="退出";
	        	findErrorString="找不到该BVID对应的视频";
	        	backString="返回";
	        	likeString="赞";
	        	viewString="看";
	        	replyString="回";
	        	coinString="币";
	        	shareString="转";
	        	introductionString="简介:";
	        	authorString="作者:";
	        	videoDisplayString="视频界面";
	        	downloadString="下载视频";
	        	coverString="显示封面";
	        	authorInfoString="作者空间";
	        	ci="次";
	        	ge="个";
	        } else {
	        	exitString="Exit";
	        	findErrorString="No this video";
	        	backString="Back";
	        	likeString="Likes";
	        	viewString="Views";
	        	replyString="Replys";
	        	coinString="Coins";
	        	shareString="Shares";
	        	introductionString="Introduction:";
	        	authorString="Author:";
	        	videoDisplayString="Video Screen";
	        	downloadString="Download";
	        	coverString="View the cover";
	        	authorInfoString="Author Space";
	        	ci="";
	        	ge="";
	        }
	    }
	 

}