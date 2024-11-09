package top.jiehuan.kilikili;

import java.io.IOException;
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

import top.jiehuan.kilikili.Exception.ErrorVideoStatusException;
import top.jiehuan.kilikili.Exception.WebReturnErrorCodeException;

public class GetVideoInfoPage implements CommandListener{
	
	public static String PageID = "1";
	
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
	
	VideoInfo video_info;

	public GetVideoInfoPage(MainMIDlet midlet,VideoInfo video_info){
		//video_info.setPageNum(video_info.getPageNum()+1);
		this.video_info = video_info;
		//初始化需要用到的变量 输入的bvid要求前面带上BV两个字母
		this.bvid=video_info.getBVID();
		ml=midlet;
		display = Display.getDisplay(midlet);
		
		loadMessages();
		
		this.video_info.setPageNum(MainPage.addPageNum(PageID,video_info));
		//video_info = new VideoInfo(bvid);
		boolean status = video_info.getStatus();
		
		//若返回代码为错误代码，则显示未找到视频
		if(!status){
			displayErrorAlert(video_info.getVideoContent());
		}else{
			// 初始化视频信息界面
			System.out.println("init video info form");
			try{
				initPageVars();
				initDisplayVars();
				display();
			}catch(Exception e){
				displayErrorAlert("获取错误");
			}
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
	        try {
				lang_res = new GetLangRes(System.getProperty("microedition.locale"));
				//System.out.println(lang_res.getLangFileContent());
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
		private void goLastPage(){
			System.out.println("call goLastPage.Page now is:"+video_info.getPageNum());
			
			 String page = (String) video_info.getPageList()[video_info.getPageNum()-1];
			 if(page.equals(MainPage.PageID)){
				 new Thread(new Runnable() {
		                public void run() {
		                	//MainMIDlet.pagelist=new String[100];
		                	//MainMIDlet.pagelist[0]="0";
		                	//MainMIDlet.page_list_num=0;
		                	new MainPage(ml);
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
		private void initPageVars() throws WebReturnErrorCodeException, IOException, ErrorVideoStatusException{
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