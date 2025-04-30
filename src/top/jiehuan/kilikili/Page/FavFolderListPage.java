package top.jiehuan.kilikili.Page;

import java.io.IOException;
import java.util.Vector;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

import top.jiehuan.kilikili.Exception.ErrorVideoStatusException;
import top.jiehuan.kilikili.Exception.PageInfoEmptyException;
import top.jiehuan.kilikili.Exception.WebReturnErrorCodeException;
import top.jiehuan.kilikili.Model.FavFolderInfo;
import top.jiehuan.kilikili.Model.PageInfo;
import top.jiehuan.kilikili.Model.VideoInfo;
import top.jiehuan.kilikili.Model.WebModel;
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
	//private Vector folders; //Class FavFolderInfo
	private String mid;
	
	public FavFolderListPage(Vector page_info_list){
		super(page_info_list);
		
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
                	initInfoPage();
                }
            }).start();
        }else if (d == folders_list) {
            // 检查是否是通过选择列表项触发的 OK 键
            int selectedIndex = folders_list.getSelectedIndex();
            if (selectedIndex != -1) {
            	new Thread(new Runnable() {
                    public void run() {
                    	initInfoPage();
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
			// TODO Auto-generated catch block
			e.printStackTrace();
			displayErrorAlert("FFL initInfo Error:"+e.getMessage());
		}
    	page_info_list.addElement(newpage);
        new FavListPage(page_info_list);
	}
	
	private void refreshPage() throws WebReturnErrorCodeException, IOException, ErrorVideoStatusException{
		System.out.println("start to get user fav folders data");
		String folder_data;
		folder_data = getWeb();
		
		//page_info.setContent(video_data);
		System.out.println("folder_data is:"+folder_data);
		ids = FindString.extractContentsInt(folder_data, "\"id\"");
		titles = FindString.extractContents(folder_data, "\"title\"");
		//for(int i=0;i<ids.length;i++)
			//folders.addElement(new FavFolderInfo(ids[i]));
		
		folder_counts = 0;
		for(int i=0;i<maxFoldersNum;i++){	//在列表内添加用户视频的标题
			if(ids[i]!=null&&titles[i]!=null){
				System.out.println(titles[i]);
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
