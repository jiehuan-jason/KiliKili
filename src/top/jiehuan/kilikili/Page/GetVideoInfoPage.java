package top.jiehuan.kilikili.Page;

import java.io.UnsupportedEncodingException;
import java.util.Vector;

import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.StringItem;

import top.jiehuan.kilikili.PageInfo;
import top.jiehuan.kilikili.Exception.PageInfoEmptyException;
import top.jiehuan.kilikili.util.FindString;
import top.jiehuan.kilikili.util.URLget;

public class GetVideoInfoPage extends Page implements CommandListener{
	
	public static final short PageID = 1;
	
	Form form;
	StringItem title;
	StringItem up_name=null;
	StringItem time;
	StringItem part_title;
	String desc;
	StringItem info;
	StringItem ln;
	String pic;
	String mid;
	String ctime;
	
	public String bvid;
	Image image;
	Command download;
	Command view_cover;
	Command author_info;
	String video_url;
	String cid;
	

	public GetVideoInfoPage(Vector page_info_list){
		//初始化需要用到的变量 
		super(page_info_list);
		form=new Form(lang_res.getValue("videoDisplay"));
		
		try{
			video_info = page_info.getVideoInfo();
		}catch(PageInfoEmptyException e){
			displayErrorAlert("Page is Empty:"+e.getMessage());
		}
		
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
                    	PageInfo newpage = new PageInfo(DownloadPage.PageID,ml);
                    	newpage.setVideoInfo(video_info);
                    	page_info_list.addElement(newpage);
                    	for(int i=0;i<page_info_list.size();i++){
                    		PageInfo info = (PageInfo)(page_info_list.elementAt(i));
                    		System.out.println("page num in"+i+" is:"+info.pageID);
                    	}
						new DownloadPage(page_info_list);
                    	
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
							displayErrorAlert(e.getMessage());
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
							displayErrorAlert(e.getMessage());
						}
                    	
                    }
	            }).start();
	        }if (c == author_info) {
	            new Thread(new Runnable() {
	                public void run() {
	                	PageInfo newpage = new PageInfo(UserInfoPage.PageID,ml);
	                	System.out.println(bvid);
	                	try {
							newpage.setVideoInfo(bvid);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							displayErrorAlert(e.getMessage());
						}
	                	page_info_list.addElement(newpage);
	                	new UserInfoPage(page_info_list);
	                }
	            }).start();
	        }
	    }
	 
		protected void initPageVars(){
			
			try {
				this.bvid=page_info.getBVID();
				
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
			try {
				video_url=URLget.BackVideoLink(bvid, cid);
			} catch(Exception e){
				displayErrorAlert("GetVideoInfoPage initPageVars error"+e.getMessage());
			}
		}
		protected void initDisplayVars(){
			System.out.println("start initDisplayVars");
			title=new StringItem(null, video_info.getTitle());
			if(video_info.getVideoParts() != 1){
				part_title = new StringItem(null, "P"+video_info.getPart()+" "+lang_res.getValue("part_title")+video_info.getPartTitle());
			}
			up_name=new StringItem(null,"\n"+lang_res.getValue("author")+video_info.getUserName());
			info = new StringItem(null,"\n"+lang_res.getValue("view")+video_info.getView()+lang_res.getValue("ci")+"  "+lang_res.getValue("reply")+video_info.getReply()+lang_res.getValue("ci")+"  "+lang_res.getValue("coin")+video_info.getCoin()+lang_res.getValue("ge")+"  "+lang_res.getValue("share")+video_info.getShare()+lang_res.getValue("ci")+"  "+lang_res.getValue("like")+video_info.getLike()+lang_res.getValue("ci")+"  "+lang_res.getValue("favorite")+video_info.getFavorite()+lang_res.getValue("ci"));
			time = new StringItem(null,"\n"+lang_res.getValue("public_time")+":"+video_info.getFormatPubTime());
			download=new Command(lang_res.getValue("download"),Command.ITEM,1);
			initBackAndExitCommand();
			view_cover=new Command(lang_res.getValue("cover"),Command.ITEM,2);
			author_info=new Command(lang_res.getValue("authorInfo"),Command.ITEM,2);

		}
		protected void display(){
			System.out.println("start display");
			form.append(title);
			if(video_info.getVideoParts() != 1){
				form.append(part_title);
			}
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
}