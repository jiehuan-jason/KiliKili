package top.jiehuan.kilikili.Page;

import java.io.IOException;
import java.util.Vector;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

import top.jiehuan.kilikili.Exception.ErrorVideoStatusException;
import top.jiehuan.kilikili.Exception.WebReturnErrorCodeException;
import top.jiehuan.kilikili.Model.FavFolderInfo;
import top.jiehuan.kilikili.Model.PageInfo;
import top.jiehuan.kilikili.Model.VideoInfo;
import top.jiehuan.kilikili.Model.WebModel;
import top.jiehuan.kilikili.util.*;

public class FavListPage extends Page implements CommandListener {
	public static final short PageID = 12;
	public static final int maxVideosNum = 15;
	
	List video_list;
	Command view_cover;
	Command go;
	Command last_page;
	Command next_page;
	
	private short page_num;
	private short video_counts;
	private String[] titles;
	private String[] avids;
	private boolean[] isVideoInvalid;
	private FavFolderInfo favFolderInfo;
	private boolean nextPageStatus;
	
	public FavListPage(Vector page_info_list){
		super(page_info_list);
		video_list=new List(lang_res.getValue("fav_list"),List.IMPLICIT);
		
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
        }else if(c==view_cover){
        	new Thread(new Runnable() {
                public void run() {
                	try {
                		VideoInfo cover_info= new VideoInfo(VideoInfo.avidToBvid(avids[video_list.getSelectedIndex()+1]));
                		System.out.println("cover_url is:"+cover_info.getCoverURL());
						ml.platformRequest(new String(cover_info.getCoverURL().getBytes("UTF-8"),"UTF-8"));
					} catch (Exception e) {
						e.printStackTrace();
						displayErrorAlert(e.getMessage());
					}
                	
                }
            }).start();
        }else if(c==go){
        	new Thread(new Runnable() {
                public void run() {
                	initInfoPage();
                }
            }).start();
        }else if(c == last_page){
        	page_num--;
        	page_info.setPage(page_num);
        	try {
        		refreshPage(page_num);
				display();
			} catch (Exception e) {
				e.printStackTrace();
				displayErrorAlert(e.getMessage());
			} 
        }else if(c == next_page){
        	page_num++;
        	page_info.setPage(page_num);
        	try {
				refreshPage(page_num);
				display();
			} catch (Exception e) {
				e.printStackTrace();
				displayErrorAlert(e.getMessage());
			} 
        }else if (d == video_list) {
            // 检查是否是通过选择列表项触发的 OK 键
            int selectedIndex = video_list.getSelectedIndex();
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
		if(isVideoInvalid[video_list.getSelectedIndex()]){
			displayErrorAlert("视频已失效！");
			return;
		}
		
		String avid = avids[video_list.getSelectedIndex()];
    	try {
			goToVideoListPage(avid);
		} catch (Exception e) {
			e.printStackTrace();
			displayErrorAlert(avid+" "+e.getClass().toString()+" "+e.getMessage());
		}
	}
	
	private void goToVideoListPage(String avid){
     	PageInfo newpage = new PageInfo(PartVideoListPage.PageID,ml);
     	String bvid = "";
     	try {
     		bvid = VideoInfo.avidToBvid(avid);
     		//displayErrorAlert(avid+" "+bvid);
			newpage.setVideoInfo(bvid);
		} catch (Exception e) {
			e.printStackTrace();
			displayErrorAlert(avid+bvid+" "+e.getClass().toString()+" "+e.getMessage());
			
		}
     	page_info_list.addElement(newpage);
     	System.out.println("FavListPage call GetVideoInfoPage");
        new PartVideoListPage(page_info_list);
	 }
	
	private void refreshPage(short page) throws WebReturnErrorCodeException, IOException, ErrorVideoStatusException{
		video_list=new List(lang_res.getValue("fav_list"),List.IMPLICIT);
		
		String video_data;
		video_data = getWeb(page);
		
		//page_info.setContent(video_data);
		System.out.println("Video_data is:"+video_data);
		titles=FindString.extractContents(video_data,"\"title\"");
		String[] avids=FindString.extractContentsInt(video_data,"\"id\""); //第一个ID是收藏夹的id，所以使用时要+1
		
		
		for(int i=1;i<=maxVideosNum;i++){	//在列表内添加用户视频的标题
			if(titles[i]!=null&&avids[i]!=null){
				video_list.append(titles[i], null);
				this.avids[i-1] = avids[i];
				video_counts++;
				if(titles[i].equals("已失效视频"))
					isVideoInvalid[i-1] = true;
			}else break;
		}
		//displayErrorAlertCanCancel(video_data, video_list);
		if(video_counts >= Integer.parseInt(favFolderInfo.getMediaCount()))
			nextPageStatus = false;
		String content = getWeb((short) (page_num+1));
		if(FindString.findValueBool(content, "medias").equals("null"))
			nextPageStatus = false;
			
			
	}
	
	protected void initPageVars(){
		page_num = 1;
		avids = new String[maxVideosNum];
		isVideoInvalid = new boolean[maxVideosNum];
		nextPageStatus = true;
		video_counts = 0;
		
		for(int i=0;i<maxVideosNum;i++)
			isVideoInvalid[i] = false;
		
		try {
			favFolderInfo = page_info.getFavFolderInfo();
			refreshPage(page_num);
		} catch (Exception e) {
			displayErrorAlert("FavListPage initPageVars Error:"+e.getMessage());
		} 
	}
	
	private String getWeb(short page) throws WebReturnErrorCodeException, IOException, ErrorVideoStatusException{
		WebModel web = URLget.BackWebWithMoreInfo(URLget.GET_FAV_LIST_URL+"?media_id="+favFolderInfo.getID()+"&ps="+maxVideosNum+"&pn="+page);
		return web.content;
	}
	
	protected void initDisplayVars(){
		System.out.println("start to initDisplayVars");
		view_cover=new Command(lang_res.getValue("view_cover"),Command.ITEM,2);
		initBackAndExitCommand();
		go = new Command(lang_res.getValue("go"),Command.OK,1);
		last_page=new Command(lang_res.getValue("last_page"),Command.OK,2);
		next_page=new Command(lang_res.getValue("next_page"),Command.OK,2);
	}
	protected void display(){
		System.out.println("start to display");
		video_list.addCommand(back);
		video_list.addCommand(go);
		checkVideoPage();
		video_list.setSelectCommand(go);
		video_list.addCommand(view_cover);
		video_list.addCommand(exit);
		video_list.setCommandListener(this);
		display.setCurrent(video_list);
	}
	
	private void checkVideoPage(){
		if(!(page_num==1))
			video_list.addCommand(last_page);
		if(nextPageStatus){
			video_list.addCommand(next_page);
		}
		
	}
	
}
