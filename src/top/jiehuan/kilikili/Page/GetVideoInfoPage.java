package top.jiehuan.kilikili.Page;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

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

import top.jiehuan.kilikili.MainMIDlet;
import top.jiehuan.kilikili.PageInfo;
import top.jiehuan.kilikili.VideoInfo;
import top.jiehuan.kilikili.Exception.ErrorVideoStatusException;
import top.jiehuan.kilikili.Exception.PageInfoEmptyException;
import top.jiehuan.kilikili.Exception.WebReturnErrorCodeException;
import top.jiehuan.kilikili.util.FindString;
import top.jiehuan.kilikili.util.GetLangRes;
import top.jiehuan.kilikili.util.URLget;

public class GetVideoInfoPage implements CommandListener{
	
	public static final short PageID = 1;
	
	GetLangRes lang_res;
	
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
	
	Vector page_info_list;
	PageInfo page_info;
	VideoInfo video_info;

	public GetVideoInfoPage(MainMIDlet midlet,Vector page_info_list){
		//初始化需要用到的变量 
		System.out.println("GetVideoInfoPage init");
		ml=midlet;
		display = Display.getDisplay(midlet);
		this.page_info_list = page_info_list;
		page_info = (PageInfo) page_info_list.lastElement();
		try {
			video_info = page_info.getVideoInfo();
		} catch (PageInfoEmptyException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		loadMessages();
		
		// 初始化视频信息界面
		System.out.println("init video info form");
		try{
			initPageVars();
			initDisplayVars();
			display();
		}catch(Exception e){
			displayErrorAlert("获取错误");
		}
		
		//video_info = new VideoInfo(bvid);
		boolean status = video_info.getStatus();
		
		System.out.println("GetVideoInfoPage:status = "+status);
		//若返回代码为错误代码，则显示未找到视频
		if(!status){
			System.out.println("GetVideoInfoPage:Back code error");
			displayErrorAlert(video_info.getVideoContent());
		}else{
			
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
                    	PageInfo newpage = new PageInfo(DownloadPage.PageID);
                    	page_info_list.addElement(newpage);
						new DownloadPage(ml,page_info_list);
                    	
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
	                	PageInfo newpage = new PageInfo(UserInfoPage.PageID);
	                	System.out.println(bvid);
	                	newpage.setVideoInfo(bvid);
	                	page_info_list.addElement(newpage);
	                	new UserInfoPage(ml,page_info_list);
	                }
	            }).start();
	        }
	    }
	 
	 private void loadMessages() {
	        // 根据系统语言加载相应的资源文件
	        try {
				lang_res = new GetLangRes(System.getProperty("microedition.locale"));
				//System.out.println(lang_res.getLangFileContent());
				System.out.println("GetVideoInfoPage:Get lang_res OK");
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
		private void goLastPage(){
			
			page_info_list.removeElementAt(page_info_list.size()-1);
			PageInfo last_page = (PageInfo) page_info_list.lastElement();
			
			System.out.println("call goLastPage.Page now is:"+last_page.pageID);
			
			 short page = last_page.pageID;
			 if(page==RecommendPage.PageID){
				 new Thread(new Runnable() {
		                public void run() {
		                	new RecommendPage(ml,page_info_list);
		                }
		            }).start();
			 }else if(page==SearchPage.PageID){
				 new Thread(new Runnable() {
		                public void run() {
		                	new SearchPage(ml,page_info_list);
		                }
		            }).start();
			 }else if(page==UserVideoListPage.PageID){
				 new Thread(new Runnable() {
		                public void run() {
		                	new UserVideoListPage(ml,page_info_list);
		                }
		            }).start();
			 }else{
				 new Thread(new Runnable() {
		                public void run() {
		                	//MainMIDlet.pagelist=new String[100];
		                	//MainMIDlet.pagelist[0]="0";
		                	//MainMIDlet.page_list_num=0;
		                	new MainPage(ml);
		                }
		            }).start();

			 }
		 }
		private void initPageVars() throws WebReturnErrorCodeException, IOException, ErrorVideoStatusException{
			
			try {
				this.bvid=page_info.getBVID();
				video_info = page_info.getVideoInfo();
				
			} catch (PageInfoEmptyException e1) {
				// TODO Auto-generated catch block
				displayErrorAlert(video_info.getVideoContent());
			}
			boolean status = video_info.getStatus();
			System.out.println("GetVideoInfoPage:status = "+status);
			//若返回代码为错误代码，则显示未找到视频
			if(!status){
				System.out.println("GetVideoInfoPage:Back code error");
				displayErrorAlert(video_info.getVideoContent());
			}
			
			System.out.println("start initPageVars");
			desc = "\n"+lang_res.getValue("introduction")+video_info.getDescription();
			cid = Long.toString(video_info.getCID());
			pic = video_info.getCoverURL();
			mid = Long.toString(video_info.getUserMID());
			video_url=URLget.BackVideoLink(bvid, cid);
		}
		private void initDisplayVars(){
			System.out.println("start initDisplayVars");
			form=new Form(lang_res.getValue("videoDisplay"));
			title=new StringItem(null, video_info.getTitle());
			up_name=new StringItem(null,"\n"+lang_res.getValue("author")+video_info.getUserName());
			info = new StringItem(null,"\n"+lang_res.getValue("view")+video_info.getView()+lang_res.getValue("ci")+"  "+lang_res.getValue("reply")+video_info.getReply()+lang_res.getValue("ci")+"  "+lang_res.getValue("coin")+video_info.getCoin()+lang_res.getValue("ge")+"  "+lang_res.getValue("share")+video_info.getShare()+lang_res.getValue("ci")+"  "+lang_res.getValue("like")+video_info.getLike()+lang_res.getValue("ci")+"  "+lang_res.getValue("favorite")+video_info.getFavorite()+lang_res.getValue("ci"));
			time = new StringItem(null,"\n"+lang_res.getValue("public_time")+":"+video_info.getFormatPubTime());
			download=new Command(lang_res.getValue("download"),Command.ITEM,1);
			back=new Command(lang_res.getValue("back"),Command.BACK,1);
			exit=new Command(lang_res.getValue("exit"),Command.EXIT,0);
			view_cover=new Command(lang_res.getValue("cover"),Command.ITEM,2);
			author_info=new Command(lang_res.getValue("authorInfo"),Command.ITEM,2);

		}
		private void display(){
			System.out.println("start display");
			form.append(title);
			form.append(up_name);
			form.append(time);
			if(!desc.equals("\n"+lang_res.getValue("introduction"))){
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
		private void displayErrorAlert(String error){
			form=new Form(lang_res.getValue("findError"));
			back=new Command(lang_res.getValue("back"),Command.BACK,1);
			exit=new Command(lang_res.getValue("exit"),Command.EXIT,0);
			form.addCommand(back);
			form.addCommand(exit);
			form.setCommandListener(this);
			Alert alert = new Alert("Error", error, null, AlertType.ERROR);
            alert.setTimeout(Alert.FOREVER); // 设置为永远显示，直到用户操作
            display.setCurrent(alert, form);
            //ml.display.setCurrent(ml.form);
		}
}