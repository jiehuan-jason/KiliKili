package top.jiehuan.kilikili.Page;

import java.io.IOException;
import java.util.Vector;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.StringItem;
import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;

import top.jiehuan.kilikili.Exception.PageInfoEmptyException;
import top.jiehuan.kilikili.Exception.WebReturnErrorCodeException;
import top.jiehuan.kilikili.Model.PageInfo;
import top.jiehuan.kilikili.Model.WebModel;
import top.jiehuan.kilikili.util.CookiesUtils;
import top.jiehuan.kilikili.util.FindString;
import top.jiehuan.kilikili.util.URLget;
import top.jiehuan.kilikili.util.VideoUtils;

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
	String my_mid;
	
	public String bvid;
	String aid;
	Image image;
	Command download;
	Command view_cover;
	Command author_info;
	Command like; //点赞
	Command coin;
	Command favorite; //收藏
	Command reply;
	Command tags;
	String video_url;
	String cid;
	String errorMessage = "";
	
	String[] favFoldersID;
	String[] favFoldersName;
	boolean[] isFav;
	int favFoldersNum;
	
	
	
	boolean is_login = false;
	

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
			displayErrorAlert("Back code error"+video_info.getErrorMessage());
			
		}else{
			
		}
		
	}
	
	public short getPageID() {
        return PageID;
 }
	
	 public void commandAction(Command c, Displayable d) {
		 VideoUtils utils = new VideoUtils(video_info);
		 //返回主界面
	        if (c == back) {
	        	goLastPage();
	        }
	        //退出app
	        else if(c==exit){
	        	ml.exitApp();
	        }
	        //下载视频
	        else if(c==download){
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
	        else if(c==view_cover){
	        	new Thread(new Runnable() {
                    public void run() {
                    	try {
                    		System.out.println("cover_url is:"+pic);
							ml.platformRequest(new String(pic.getBytes("UTF-8"),"UTF-8"));
						} catch (Exception e) {
							e.printStackTrace();
							displayErrorAlert(e.getMessage());
						}
                    	
                    }
	            }).start();
	        }else if (c == author_info) {
	            new Thread(new Runnable() {
	                public void run() {
	                	PageInfo newpage = new PageInfo(UserInfoPage.PageID,ml);
	                	System.out.println(bvid);
	                	try {
							newpage.setVideoInfo(bvid);
						} catch (Exception e) {
							displayErrorAlert(e.getMessage());
						}
	                	page_info_list.addElement(newpage);
	                	new UserInfoPage(page_info_list);
	                }
	            }).start();
	        }else if (c==like){
	        	
	        	try{
	        		boolean status = utils.pressLike();
	        		video_info.setLikeStatus(status);
	        		page_info.setVideoInfo(video_info);
					page_info_list.removeElement(page_info_list.lastElement());
					page_info_list.addElement(page_info);
					Thread.sleep(500);
					refresh();
					if(status)
						displayInfoAlert("点赞成功！", form);
					else
						displayInfoAlert("取消成功！", form);
	        	}catch(Exception e){
	        		displayErrorAlert(e.getMessage());
	        	}
	        }else if (c==coin){
	        	pressCoin();
	        }else if (c==favorite){
	        	try{
	        		
	        		utils.pressFavorite(page_info_list);
	        		
	        	}catch(Exception e){
	        		displayErrorAlert(e.getMessage());
	        	}
	        }else if (c==reply){
	        	try{
	        		PageInfo newPage = new PageInfo(ReplyListPage.PageID,ml);
	        		newPage.setVideoInfo(video_info);
	        		newPage.setPage(1);
	        		page_info_list.addElement(newPage);
	        		new ReplyListPage(page_info_list);
	        	}catch(Exception e){
	        		displayErrorAlert(e.getMessage());
	        	}
	        }else if (c==tags){
	        	new Thread(new Runnable() {
	                public void run() {
	        	try{
	        		PageInfo newPage = new PageInfo(SimpleListPage.PageID,ml);
	        		newPage.setVideoInfo(video_info);
	        		newPage.setType(3);
	        		page_info_list.addElement(newPage);
	        		new SimpleListPage(page_info_list);
	        	}catch(Exception e){
	        		displayErrorAlert(e.getMessage());
	        	}
	          }
	         }).start();
	        }
	    }
	 

	 	
	 	private void pressCoin(){
	 		chooseCoinCounts();
	 	}
	 	
	 	private void chooseCoinCounts(){
	 		
	 		Alert alert = new Alert("投币数量", "请使用下方命令选择投币数量", null, AlertType.INFO);
	        alert.setTimeout(Alert.FOREVER); // 设置为永远显示，直到用户操作
	        final Command closeCommand = new Command("关闭", Command.EXIT, 0);
	        final Command oneCoinCommand = new Command("一个币", Command.OK, 1);
	        final Command twoCoinCommand = new Command("两个币", Command.OK, 1);
	        alert.addCommand(closeCommand);
	        alert.addCommand(oneCoinCommand);
	        alert.addCommand(twoCoinCommand);

	        alert.setCommandListener(new CommandListener() {
	            public void commandAction(Command c, Displayable d) {
	            	VideoUtils utils = new VideoUtils(video_info);
	            	try{
	            	if (c == oneCoinCommand) {
	            		utils.postCoinRequest(1);
	                }else if (c == twoCoinCommand) {
	                	utils.postCoinRequest(2);
	                } else if (c == closeCommand) {
	                    // 关闭按钮被按下，返回到之前的界面
	                	display.setCurrent(form);
	                }
	            	page_info_list.removeElement(page_info_list.lastElement());
	 				page_info_list.addElement(page_info);
	    			refresh();
	    			displayInfoAlert("投币成功！", form);
	            	}catch(Exception e){
	            		displayErrorAlert("chooseCoinCounts error:"+e.getMessage());
	            	}
	            }
	        });
	        display.setCurrent(alert, form);
	 	}
	 	
	 	
	 	
	 	
	 
	 	
	 	private void refresh(){
	 		form=new Form(lang_res.getValue("videoDisplay"));
	 		try {
				video_info = page_info.getVideoInfo();
			} catch (PageInfoEmptyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	 		video_info.initUserDataInVideo();
	 		initPageVars();
			initDisplayVars();
			display();
	 	}
	 
		protected void initPageVars(){
			
			try {
				this.bvid=page_info.getBVID();
				this.aid = video_info.getAID();
			} catch (PageInfoEmptyException e1) {
				displayErrorAlert("PIEE:"+video_info.getVideoContent());
			}
			boolean status = video_info.getStatus();
			System.out.println("GetVideoInfoPage:status = "+status);
			//若返回代码为错误代码，则显示未找到视频
			if(!status){
				System.out.println("GetVideoInfoPage:Back code error");
				displayErrorAlert("GVIF BCE:"+video_info.getErrorMessage());
			}
			
			
			System.out.println("GetVideoInfoPage:start initPageVars");
			desc = "\n"+lang_res.getValue("introduction")+video_info.getDescription();
			cid = Long.toString(video_info.getCID());
			pic = video_info.getCoverURL();
			mid = Long.toString(video_info.getUserMID());
			try {
				video_url=URLget.BackVideoLink(bvid, cid);
				video_info.postHeartbeat();
			} catch(Exception e){
				displayErrorAlert("GetVideoInfoPage initPageVars error"+e.getMessage());
			}
			
			isFav = new boolean[30];
			favFoldersNum = 0;
			try{
				is_login = new CookiesUtils("isLogin").isTokenStored();
				if(is_login){
					WebModel web = URLget.BackWebWithMoreInfo(URLget.GET_PERSONAL_INFO_URL);
					my_mid = FindString.findValueInt(web.content, "mid");
					web = URLget.BackWebWithMoreInfo(URLget.GET_USER_ALL_FAV_FOLDERS_URL+"?up_mid="+my_mid+"&type=2&rid="+aid);
					favFoldersID = FindString.extractContentsInt(web.content, "\"id\"");
					String[] favStatus = FindString.extractContentsInt(web.content, "\"fav_state\"");
					favFoldersName = FindString.extractContents(web.content, "\"title\"");
					int i=0;
					while(favStatus[i]!=null){
						if(favStatus[i].equals("0")) isFav[i] = false;
						else isFav[i] = true;
						System.out.println(favFoldersID[i]);
						System.out.println(favFoldersName[i]);
						//favFoldersNameList.addElement(favFolders[i]);
						i++;
						favFoldersNum++;
					}                                                       
				}
			}catch(Exception e){
				displayErrorAlert(e.getMessage());
			}
			
			
		}
		protected void initDisplayVars(){
			System.out.println("GetVideoInfoPage:start initDisplayVars");
			title=new StringItem(null, video_info.getTitle());
			if(video_info.getVideoParts() != 1){
				part_title = new StringItem(null, "P"+video_info.getPart()+" "+lang_res.getValue("part_title")+video_info.getPartTitle());
			}
			up_name=new StringItem(null,"\n"+lang_res.getValue("author")+video_info.getUserName());
			info = new StringItem(null,"\n"+lang_res.getValue("view")+video_info.getView()+lang_res.getValue("ci")+"  "+lang_res.getValue("reply")+video_info.getReply()+lang_res.getValue("ci")+"  "+lang_res.getValue("coin")+video_info.getCoin()+lang_res.getValue("ge")+"  "+lang_res.getValue("share")+video_info.getShare()+lang_res.getValue("ci")+"  "+lang_res.getValue("like")+video_info.getLike()+lang_res.getValue("ci")+"  "+lang_res.getValue("favorite")+video_info.getFavorite()+lang_res.getValue("ci")+"\n"+errorMessage);
			time = new StringItem(null,"\n"+lang_res.getValue("public_time")+":"+video_info.getFormatPubTime());
			download=new Command(lang_res.getValue("download"),Command.ITEM,1);
			initBackAndExitCommand();
			view_cover=new Command(lang_res.getValue("cover"),Command.ITEM,2);
			author_info=new Command(lang_res.getValue("authorInfo"),Command.ITEM,2);
			tags = new Command("Tags",Command.ITEM,1);
			if(is_login){
				if(video_info.isLike)
					like = new Command(lang_res.getValue("cancel")+lang_res.getValue("like"),Command.OK,3);
				else
					like = new Command(lang_res.getValue("like"),Command.OK,3);
				favorite = new Command(lang_res.getValue("favorite"),Command.OK,3);
				coin = new Command(lang_res.getValue("coin"),Command.OK,3);
				reply = new Command(lang_res.getValue("reply_list"), Command.OK,3);
			}

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
			form.addCommand(tags);
			form.addCommand(author_info);
			form.addCommand(download);
			form.addCommand(view_cover);
			form.addCommand(exit);
			if(is_login){
				form.addCommand(like);
				//form.addCommand(coin); //报-401风控 暂时移除
				form.addCommand(favorite);
				form.addCommand(reply);
			}
			form.setCommandListener(this);
			display.setCurrent(form);

		}
}