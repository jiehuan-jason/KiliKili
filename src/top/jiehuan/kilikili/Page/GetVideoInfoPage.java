package top.jiehuan.kilikili.Page;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

import javax.microedition.io.ConnectionNotFoundException;
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

import top.jiehuan.kilikili.Exception.ErrorVideoStatusException;
import top.jiehuan.kilikili.Exception.PageInfoEmptyException;
import top.jiehuan.kilikili.Exception.WebReturnErrorCodeException;
import top.jiehuan.kilikili.Model.PageInfo;
import top.jiehuan.kilikili.Model.WebModel;
import top.jiehuan.kilikili.util.CookiesUtils;
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
	String video_url;
	String cid;
	String errorMessage = "";
	
	String[] favFoldersID;
	String[] favFoldersName;
    private Vector favFoldersCommandList = new Vector();
    private Vector favFoldersNameList = new Vector();
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
						} catch (Exception e) {
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
							displayErrorAlert(e.getMessage());
						}
	                	page_info_list.addElement(newpage);
	                	new UserInfoPage(page_info_list);
	                }
	            }).start();
	        }if (c==like){
	        	try{
	        		pressLike();
	        	}catch(Exception e){
	        		displayErrorAlert(e.getMessage());
	        	}
	        }if (c==coin){
	        	pressCoin();
	        }if (c==favorite){
	        	try{
	        		pressFavorite();
	        	}catch(Exception e){
	        		displayErrorAlert(e.getMessage());
	        	}
	        }if (c==reply){
	        	try{
	        		PageInfo newPage = new PageInfo(ReplyListPage.PageID,ml);
	        		newPage.setVideoInfo(video_info);
	        		newPage.setPage(1);
	        		page_info_list.addElement(newPage);
	        		new ReplyListPage(page_info_list);
	        	}catch(Exception e){
	        		displayErrorAlert(e.getMessage());
	        	}
	        }
	    }
	 
	 	/*
	 	 * TODO:
	 	 * 目前存在的已知问题：成功后不显示弹窗，不会刷新界面
	 	 * 待观察的问题：有的时候Cookies疑似会失效，code为-403
	 	 * 投币问题同上
	 	 * 
	 	 * 4.28 此为旧版，由于接口问题无法解决，已经换用动态的点赞接口
	 	 * */
	 	/*private void pressLike() throws InvalidRecordIDException, RecordStoreFullException, RecordStoreNotFoundException, RecordStoreException, IOException, WebReturnErrorCodeException{
	 		String cookiesString = new CookiesUtils().loadToken();
    		String csrf = FindString.findValueInCookies(cookiesString, "bili_jct");
    		
    		if(video_info.isLike){
    			WebModel web = URLget.BackWebAndUserCookiesPost(URLget.LIKE_URL,"aid="+aid+"&like=2&from_spmid=333.1007.tianma.1-1-1.click&spmid=333.788.0.0&source=web_normal&csrf="+csrf);
        		int bili_code = URLget.getAPIBackCode(web.content);
        		if(bili_code!=1)
        			//displayErrorAlertCanCancel(FindString.findValue(web.content, "message"), form);
        			displayErrorAlertCanCancel(web.content+"aid="+aid+"&like=2&from_spmid=333.1007.tianma.1-1-1.click&spmid=333.788.0.0&source=web_normal&csrf="+csrf,form);
        		else{
        			displayInfoAlert("取消点赞成功！");
        			refresh();
        		}
        			
    		}else{
    			WebModel web = URLget.BackWebAndUserCookiesPost(URLget.LIKE_URL,"aid="+aid+"&like=1&from_spmid=333.1007.tianma.1-1-1.click&spmid=333.788.0.0&source=web_normal&csrf="+csrf);
        		int bili_code = URLget.getAPIBackCode(web.content);
        		if(bili_code!=0)
        			//displayErrorAlertCanCancel(FindString.findValue(web.content, "message"), form);
        			displayErrorAlertCanCancel(web.content+"aid="+aid+"&like=1&from_spmid=333.1007.tianma.1-1-1.click&spmid=333.788.0.0&source=web_normal&csrf="+csrf,form);
        		else{
        			displayInfoAlert("点赞成功！");
        			refresh();
        		}
    		}
    		
	 	}*/
	 
	 private void pressLike() throws InvalidRecordIDException, RecordStoreFullException, RecordStoreNotFoundException, RecordStoreException, IOException, WebReturnErrorCodeException{
	 	
 		
 		if(!video_info.isLike){
 			postLikeRequestAndRefresh(1);
     			
 		}else{
 			postLikeRequestAndRefresh(2);
 		}
	 }
	 
	 
	 //mode = 1 点赞
	 //mode = 2 取消点赞
	 private void postLikeRequestAndRefresh(int mode) throws InvalidRecordIDException, RecordStoreFullException, RecordStoreNotFoundException, RecordStoreException, WebReturnErrorCodeException, IOException{
		if(mode!=1&&mode!=2){
			displayErrorAlert("postLikeRequestAndRefresh函数参数错误");
			return;
		}
		 
		String cookiesString = new CookiesUtils().loadToken();
	 	String csrf = FindString.findValueInCookies(cookiesString, "bili_jct");
	 	WebModel web = URLget.BackWebAndUserCookiesPost(URLget.LIKE_DYNAMIC_URL+"?csrf="+csrf,"{\"dyn_id_str\":\""+video_info.getDynamicID()+"\",\"up\":"+mode+",\"spmid\":\"333.1365.0.0\"}",2);
 		int bili_code = URLget.getAPIBackCode(web.content);
 		if(bili_code!=0)
 			//displayErrorAlertCanCancel(FindString.findValue(web.content, "message"), form);
 			displayErrorAlertCanCancel(web.content,form);
 		else{
 			
 			if(mode == 1){
 				video_info.setLikeStatus(true);
 				page_info.setVideoInfo(video_info);
 				page_info_list.removeElement(page_info_list.lastElement());
 				page_info_list.addElement(page_info);
 				refresh();
 				displayInfoAlert("点赞成功！", form);
 			}
 			else{
 				video_info.setLikeStatus(false);
 				page_info.setVideoInfo(video_info);
 				page_info_list.removeElement(page_info_list.lastElement());
 				page_info_list.addElement(page_info);
 				refresh();
 				displayInfoAlert("取消成功！", form);
 			}
 			
 		}
	 }
	 	
	 	private void pressCoin(){
	 		choossCoinCounts();
	 	}
	 	
	 	private void choossCoinCounts(){
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
	            	
	            	if (c == oneCoinCommand) {
	            		postCoinRequest(1);
	                }else if (c == twoCoinCommand) {
	                	postCoinRequest(2);
	                } else if (c == closeCommand) {
	                    // 关闭按钮被按下，返回到之前的界面
	                	display.setCurrent(form);
	                }
	            }
	        });
	        display.setCurrent(alert, form);
	 	}
	 	
	 	private void postCoinRequest(int num){
	 		if(num>2||num<=0)
	 			return;
	 		
	 		
    		try{
    			String cookiesString = new CookiesUtils().loadToken();
        		String csrf = FindString.findValueInCookies(cookiesString, "bili_jct");
        		
    	 		WebModel web = URLget.BackWebAndUserCookiesPost(URLget.COIN_URL,"bvid="+bvid+"&multiply="+num+"&csrf="+csrf);
        		int bili_code = URLget.getAPIBackCode(web.content);
        		if(bili_code!=0)
        			displayErrorAlertCanCancel(FindString.findValue(web.content, "message"), form);
        		else{
        			video_info.setCoinStatus(true);
     				page_info_list.removeElement(page_info_list.lastElement());
     				page_info_list.addElement(page_info);
        			refresh();
        			displayInfoAlert("投币成功！", form);
        		}
    	 	}catch(Exception e){
    	 		displayErrorAlert(e.getMessage());
    	 	}
	 	}
	 	
	 	/*private void pressFavorite() throws InvalidRecordIDException, RecordStoreFullException, RecordStoreNotFoundException, RecordStoreException, IOException, WebReturnErrorCodeException{
	 		Alert alert = new Alert("收藏", "请使用下方命令选择收藏夹", null, AlertType.INFO);
	        alert.setTimeout(Alert.FOREVER); // 设置为永远显示，直到用户操作
	        final Command closeCommand = new Command("关闭", Command.EXIT, 0);
	        alert.addCommand(closeCommand);
	        favFoldersCommandList.addElement(closeCommand);
	        favFoldersNameList.addElement("关闭");
	        
	        for (int i = 0; i < favFoldersNum; i++) {
	            Command cmd = new Command(favFoldersName[i], Command.ITEM, 1);
	            alert.addCommand(cmd);
	            favFoldersCommandList.addElement(cmd);
	            favFoldersNameList.addElement(favFoldersName[i]);
	        }

	        alert.setCommandListener(new CommandListener() {
	            public void commandAction(Command c, Displayable d) {
	            	display.setCurrent(form);
	            	displayErrorAlertCanCancel(c.getLabel(), form);
	            	
	                for (int i = 0; i < favFoldersCommandList.size(); i++) {
	                    if (c == (Command) favFoldersCommandList.elementAt(i)) {
	                        String selectedName = (String) favFoldersNameList.elementAt(i);

	                        if (selectedName.equals("关闭")) {
	                        	display.setCurrent(form);
	                        } else {
	                            System.out.println("用户选择了: " + selectedName);
		                        displayErrorAlertCanCancel(selectedName+" "+favFoldersID[i-1], form);
	                            if(isFav[i-1])
	                            	postFavRequest(favFoldersID[i-1], false);
	                            else
	                            	postFavRequest(favFoldersID[i-1], true);
	                        }
	                        break;
	                    }
	                }
	            }
	        });
	        display.setCurrent(alert, form);			
	 	}*/
	 	private void pressFavorite(){
	 		PageInfo favListPage = new PageInfo(FavFolderListPage.PageID,ml);
	 		favListPage.setVideoInfo(video_info);
	 		favListPage.setType(2);
        	page_info_list.addElement(favListPage);
        	for(int i=0;i<page_info_list.size();i++){
        		PageInfo info = (PageInfo)(page_info_list.elementAt(i));
        		System.out.println("page num in"+i+" is:"+info.pageID);
        	}
			new FavFolderListPage(page_info_list);
	 	}
	 	
	 	//status = true 点赞
	 	//status = false 取消点赞
	 	private void postFavRequest(String favID, boolean status){
	 		
	 		
    		try{
    			String cookiesString = new CookiesUtils().loadToken();
        		String csrf = FindString.findValueInCookies(cookiesString, "bili_jct");
        		WebModel web = new WebModel();
        		if(status)
    	 			web = URLget.BackWebAndUserCookiesPost(URLget.FAVORITE_URL,"rid="+aid+"&type=2&add_media_ids="+favID+"&csrf="+csrf);
        		else
        			web = URLget.BackWebAndUserCookiesPost(URLget.FAVORITE_URL,"rid="+aid+"&type=2&del_media_ids="+favID+"&csrf="+csrf);
        		
        		int bili_code = URLget.getAPIBackCode(web.content);
        		displayErrorAlertCanCancel(web.content, form);
        		if(bili_code!=0){
        			displayErrorAlertCanCancel(FindString.findValue(web.content, "message"), form);
        			errorMessage = "rid="+aid+"&type=2&add_media_ids="+favID+"&csrf="+csrf;
        			errorMessage = errorMessage+web.content;
        			refresh();
        		}
        		else{
        			displayErrorAlertCanCancel("rid="+aid+"&type=2&add_media_ids="+favID+"&csrf="+csrf+web.content, form);
        			displayInfoAlert("收藏/取消成功！", form);
        			refresh();
        		}
    	 	}catch(Exception e){
    	 		displayErrorAlert(e.getMessage());
    	 	}
	 	}
	 
	 	
	 	private void refresh(){
	 		form=new Form(lang_res.getValue("videoDisplay"));
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
			} catch(Exception e){
				displayErrorAlert("GetVideoInfoPage initPageVars error"+e.getMessage());
			}
			
			isFav = new boolean[30];
			favFoldersNum = 0;
			try{
				is_login = new CookiesUtils().isTokenStored();
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
			form.addCommand(author_info);
			form.addCommand(download);
			form.addCommand(view_cover);
			form.addCommand(exit);
			if(is_login){
				form.addCommand(like);
				form.addCommand(coin);
				form.addCommand(favorite);
				form.addCommand(reply);
			}
			form.setCommandListener(this);
			display.setCurrent(form);

		}
}