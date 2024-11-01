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
	
	public static String PageID = "1";
	
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
	String timeString;
	String favoriteString;
	String ci;
	String ge;

	Display display;
	Form form;
	StringItem title;
	StringItem up_name=null;
	StringItem view;
	StringItem reply;
	StringItem coin;
	StringItem share;
	StringItem like;
	StringItem time;
	String desc;
	StringItem info;
	StringItem ln;
	String pic;
	String mid;
	String ctime;
	
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
	
	VideoInfo video_info;

	public GetVideoInfoPage(MainMIDlet midlet,VideoInfo video_info){
		//video_info.setPageNum(video_info.getPageNum()+1);
		this.video_info = video_info;
		//初始化需要用到的变量 输入的bvid要求前面带上BV两个字母
		this.bvid=video_info.getBVID();
		ml=midlet;
		display = Display.getDisplay(midlet);
		
		loadMessages();
		
		this.video_info.setPageNum(MainMIDlet.addPageNum(PageID,video_info));
		//video_info = new VideoInfo(bvid);
		boolean status = video_info.getStatus();
		
		//若返回代码为错误代码，则显示未找到视频
		if(!status){
			form=new Form(findErrorString);
			back=new Command(backString,Command.BACK,1);
			exit=new Command(exitString,Command.EXIT,0);
			form.addCommand(back);
			form.addCommand(exit);
			form.setCommandListener(this);
			Alert alert = new Alert("Error", video_info.getVideoContent(), null, AlertType.ERROR);
            alert.setTimeout(Alert.FOREVER); // 设置为永远显示，直到用户操作
            display.setCurrent(alert, form);
            ml.display.setCurrent(ml.form);
            
		}else{
			// 初始化视频信息界面
			System.out.println("init video info form");
			
			
			//title=FindString.findValue(s_info[1],"title");
			title=new StringItem(null, video_info.getTitle());
			up_name=new StringItem(null,"\n"+authorString+video_info.getUserName());
			info = new StringItem(null,"\n"+viewString+video_info.getView()+ci+"  "+replyString+video_info.getReply()+ci+"  "+coinString+video_info.getCoin()+ge+"  "+shareString+video_info.getShare()+ci+"  "+likeString+video_info.getLike()+ci+"  "+favoriteString+video_info.getFavorite()+ci);
			time = new StringItem(null,"\n"+timeString+":"+video_info.getFormatPubTime());
			System.out.println("finish init string item");
			desc = "\n"+introductionString+video_info.getDescription();
			cid = Long.toString(video_info.getCID());
			pic = video_info.getCoverURL();
			mid = Long.toString(video_info.getUserMID());
			System.out.println("finish findValue");
			video_url=URLget.BackVideoLink(bvid, cid);
			System.out.println("Get Already");
			
			
			download=new Command(downloadString,Command.ITEM,1);
			back=new Command(backString,Command.BACK,1);
			exit=new Command(exitString,Command.EXIT,0);
			view_cover=new Command(coverString,Command.ITEM,2);
			author_info=new Command(authorInfoString,Command.ITEM,2);
			
			
			form=new Form(videoDisplayString);
			
			
			form.append(title);
			form.append(up_name);
			form.append(time);
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
	        	goLastPage();
	        }
	        //退出app
	        if(c==exit){
	        	ml.exitApp();
	        }
	        //下载视频
	        if(c==download){
	        	new Thread(new Runnable() {
                    public void run() {
                    	System.out.println("video_url is:"+video_url);
						new DownloadPage(ml,video_info);
                    	
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
	                	System.out.println("page "+PageID+" search_word:"+video_info.getSearchKeyword());
	                	new UserInfoPage(ml,video_info);
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
	        	timeString="发布时间";
	        	favoriteString="收藏";
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
	        	timeString="Time";
	        	favoriteString="Favorites";
	        	ci="";
	        	ge="";
	        }
	    }
		private void goLastPage(){
			System.out.println("call goLastPage.Page now is:"+video_info.getPageNum());
			
			 String page = (String) video_info.getPageList()[video_info.getPageNum()-1];
			 if(page.equals(MainMIDlet.PageID)){
				 new Thread(new Runnable() {
		                public void run() {
		                	//MainMIDlet.pagelist=new String[100];
		                	//MainMIDlet.pagelist[0]="0";
		                	//MainMIDlet.page_list_num=0;
		                	ml.display.setCurrent(ml.form);
		                }
		            }).start();
			 }else if(page.equals(RecommendPage.PageID)){
				 new Thread(new Runnable() {
		                public void run() {
		                	new RecommendPage(ml,video_info);
		                }
		            }).start();
			 }else if(page.equals(SearchPage.PageID)){
				 new Thread(new Runnable() {
		                public void run() {
		                	new SearchPage(ml,video_info);
		                }
		            }).start();
			 }
		 }

}