package top.jiehuan.kilikili.Page;

import java.io.IOException;
import java.util.Vector;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;
import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;

import top.jiehuan.kilikili.Exception.ErrorVideoStatusException;
import top.jiehuan.kilikili.Exception.PageInfoEmptyException;
import top.jiehuan.kilikili.Exception.WebReturnErrorCodeException;
import top.jiehuan.kilikili.Model.FavFolderInfo;
import top.jiehuan.kilikili.Model.PageInfo;
import top.jiehuan.kilikili.Model.VideoInfo;
import top.jiehuan.kilikili.Model.WebModel;
import top.jiehuan.kilikili.util.CookiesUtils;
import top.jiehuan.kilikili.util.FindString;
import top.jiehuan.kilikili.util.URLget;

public class FavFolderListPage extends Page implements CommandListener{
	public static final short PageID = 11;
	public static int maxFoldersNum = 30;
	
	List folders_list;
	Command go;
	
	private short page_num;
	private short folder_counts;
	private String[] ids;
	private String[] titles;
	private String[] favStatus;
	//private Vector folders; //Class FavFolderInfo
	private String mid;
	private int type;
	//type = 1 Mine
	//type = 2 Video
	
	public FavFolderListPage(Vector page_info_list){
		super(page_info_list);
		type = page_info.getType();
		
		folders_list=new List(lang_res.getValue("fav_folder_list"),List.IMPLICIT);
		
			initPageVars();
			initDisplayVars();
			display();
	}
	public short getPageID() {
        return PageID;
 }
	
	public void commandAction(Command c, Displayable d) {
        if (c == back) {
            new Thread(new Runnable() {
                public void run() {
                	page_info_list.removeElementAt(page_info_list.size()-1);
                	back(page_info_list);
                }
            }).start();
        }
        else if(c==exit){
        	ml.exitApp();
        }else if(c==go){
        	new Thread(new Runnable() {
                public void run() {
                	if(type==1)
                		initInfoPage();
                	else if(type==2)
                		postFav();
                }
            }).start();
        }else if (d == folders_list) {
            // 检查是否是通过选择列表项触发的 OK 键
            int selectedIndex = folders_list.getSelectedIndex();
            if (selectedIndex != -1) {
            	new Thread(new Runnable() {
                    public void run() {
                    	if(type==1)
                    		initInfoPage();
                    	else if(type==2)
                    		postFav();
                    }
                }).start();
            }else{
            	displayErrorAlert("未选择！");
            }
        }
    }
	
	private void initInfoPage(){
		String id = ids[folders_list.getSelectedIndex()];
    	PageInfo newpage = new PageInfo(FavListPage.PageID,ml);
    	try {
			newpage.setFavFolderInfo(new FavFolderInfo(id));;
		} catch (Exception e) {
			e.printStackTrace();
			displayErrorAlert("FFL initInfo Error:"+e.getMessage());
		}
    	page_info_list.addElement(newpage);
        new FavListPage(page_info_list);
	}
	
	private void postFav(){
	try{
		String cookiesString = new CookiesUtils().loadToken();
		String csrf = FindString.findValueInCookies(cookiesString, "bili_jct");
		String data;
		if(favStatus[folders_list.getSelectedIndex()].equals("0"))
			data = "rid="+video_info.getAID()+"&type=2&platform=web&del_media_ids=&add_media_ids="+ids[folders_list.getSelectedIndex()]+"&csrf="+csrf;
		else
			data = "rid="+video_info.getAID()+"&type=2&platform=web&add_media_ids=&del_media_ids="+ids[folders_list.getSelectedIndex()]+"&csrf="+csrf;
		WebModel web = URLget.BackWebAndUserCookiesPost(URLget.FAVORITE_URL, data);
		int bili_code = URLget.getAPIBackCode(web.content);
		if(bili_code!=0)
			displayErrorAlertCanCancel(FindString.findValue(web.content, "message"), folders_list);
		else{
			displayInfoAlert("收藏成功！", folders_list);
			page_info_list.removeElementAt(page_info_list.size()-1);
        	back(page_info_list);
	}
	}catch(Exception e){
			e.printStackTrace();
			displayErrorAlert("FFL postFav Error:"+e.getMessage());
	}
	}
	
	private void refreshPage() throws WebReturnErrorCodeException, IOException, ErrorVideoStatusException, PageInfoEmptyException{
		System.out.println("start to get user fav folders data");
		String folder_data = "";
		
		
		if(type == 1)
			folder_data = getWeb();
		else if(type == 2){
			video_info = page_info.getVideoInfo();
			String aid = video_info.getAID();
			folder_data = getWeb(aid);
			favStatus = FindString.extractContentsInt(folder_data, "\"fav_state\"");
		}
		
		//page_info.setContent(video_data);
		System.out.println("folder_data is:"+folder_data);
		ids = FindString.extractContentsInt(folder_data, "\"id\"");
		titles = FindString.extractContents(folder_data, "\"title\"");
		//for(int i=0;i<ids.length;i++)
			//folders.addElement(new FavFolderInfo(ids[i]));
		
		folder_counts = 0;
		for(int i=0;i<maxFoldersNum;i++){	//在列表内添加收藏夹的标题
			if(ids[i]!=null&&titles[i]!=null){
				System.out.println(titles[i]);
				if(type == 2){
					if(favStatus[i].equals("1"))
						titles[i] = titles[i]+" 取消收藏";
				}
				folders_list.append(titles[i], null);
				folder_counts++;
			}else break;
		}
	}
	
	protected void initPageVars(){
		page_num = 1;
		
		try {
			WebModel web = URLget.BackWebWithMoreInfo(URLget.GET_PERSONAL_INFO_URL);
			mid = FindString.findValueInt(web.content, "mid");
			refreshPage();
		} catch (Exception e) {
			displayErrorAlert("initPageVars Error:"+e.getClass().getName()+" "+e.getMessage());
		} 
	}
	
	private String getWeb() throws WebReturnErrorCodeException, IOException, ErrorVideoStatusException{
		WebModel web = URLget.BackWebWithMoreInfo(URLget.GET_USER_ALL_FAV_FOLDERS_URL+"?up_mid="+mid+"&type=2");
		return web.content;
	}
	
	private String getWeb(String aid) throws WebReturnErrorCodeException, IOException, ErrorVideoStatusException{
		WebModel web = URLget.BackWebWithMoreInfo(URLget.GET_USER_ALL_FAV_FOLDERS_URL+"?up_mid="+mid+"&type=2&rid="+aid);
		return web.content;
	}
	
	protected void initDisplayVars(){
		System.out.println("start to initDisplayVars");
		initBackAndExitCommand();
		go = new Command(lang_res.getValue("go"),Command.OK,1);
	}
	protected void display(){
		System.out.println("start to display");
		folders_list.addCommand(back);
		folders_list.addCommand(go);
		folders_list.setSelectCommand(go);
		folders_list.addCommand(exit);
		folders_list.setCommandListener(this);
		display.setCurrent(folders_list);
	}
}
