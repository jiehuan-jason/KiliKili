package top.jiehuan.kilikili.util;

import java.io.IOException;
import java.util.Vector;

import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;

import top.jiehuan.kilikili.Exception.WebReturnErrorCodeException;
import top.jiehuan.kilikili.Model.PageInfo;
import top.jiehuan.kilikili.Model.VideoInfo;
import top.jiehuan.kilikili.Model.WebModel;
import top.jiehuan.kilikili.Page.FavFolderListPage;

public class VideoUtils {
	public VideoInfo video;
	
	public VideoUtils(VideoInfo video){
		this.video = video;
	}
	
	public static String avidToBvid(String avid) throws WebReturnErrorCodeException, IOException{
		WebModel web = URLget.BackWebWithMoreInfo(URLget.GET_INFO_URL+"aid="+avid);
		return FindString.findValue(web.content, "bvid");
		//return web.content;
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
	public void pressFavorite(Vector pageInfoList){
		PageInfo page = (PageInfo) (pageInfoList.lastElement());
		PageInfo favListPage = new PageInfo(FavFolderListPage.PageID,page.getMainMIDletObject());
		favListPage.setVideoInfo(video);
		favListPage.setType(2);
		pageInfoList.addElement(favListPage);
		for(int i=0;i<pageInfoList.size();i++){
			PageInfo info = (PageInfo)(pageInfoList.elementAt(i));
			System.out.println("page num in"+i+" is:"+info.pageID);
		}
		new FavFolderListPage(pageInfoList);
	}
	
	//status = true 点赞
	//status = false 取消点赞
	/*private void postFavRequest(String favID, boolean status){
		
		
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
	}*/
	
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
 
	public boolean pressLike() throws InvalidRecordIDException, RecordStoreFullException, RecordStoreNotFoundException, RecordStoreException, IOException, WebReturnErrorCodeException{
 	
		
		if(!video.isLike){
			return postLikeRequestAndRefresh(1);
 			
		}else{
			return postLikeRequestAndRefresh(2);
		}
	}
 
 
 //mode = 1 点赞
 //mode = 2 取消点赞
	private boolean postLikeRequestAndRefresh(int mode) throws InvalidRecordIDException, RecordStoreFullException, RecordStoreNotFoundException, RecordStoreException, WebReturnErrorCodeException, IOException{
	 
		String cookiesString = new CookiesUtils().loadToken();
		String csrf = FindString.findValueInCookies(cookiesString, "bili_jct");
		WebModel web = URLget.BackWebAndUserCookiesPost(URLget.LIKE_DYNAMIC_URL+"?csrf="+csrf,"{\"dyn_id_str\":\""+video.getDynamicID()+"\",\"up\":"+mode+",\"spmid\":\"333.1365.0.0\"}",2);
		int bili_code = URLget.getAPIBackCode(web.content);
		if(bili_code!=0){
			throw new WebReturnErrorCodeException(bili_code);
			//displayErrorAlertCanCancel(FindString.findValue(web.content, "message"), form);
			//displayErrorAlertCanCancel(web.content,form);
		}
			
		else{
			if(mode == 1){
				video.setLikeStatus(true);
				return true;
			}
			else{
				video.setLikeStatus(false);
				return false;
			}
			
		}
	}
	
	public void postCoinRequest(int num) throws WebReturnErrorCodeException, InvalidRecordIDException, IOException, RecordStoreException{
 		if(num>2||num<=0)
 			return;
 		
 		
			String cookiesString = new CookiesUtils().loadToken();
    		String csrf = FindString.findValueInCookies(cookiesString, "bili_jct");
    		
	 		WebModel web = URLget.BackWebAndUserCookiesPost(URLget.COIN_URL,"bvid="+video.getBVID()+"&multiply="+num+"&csrf="+csrf);
    		int bili_code = URLget.getAPIBackCode(web.content);
    		if(bili_code!=0)
    			throw new WebReturnErrorCodeException(bili_code, web.content);
    		else{
    			video.setCoinStatus(true);
 				
    		}
 	}
}
