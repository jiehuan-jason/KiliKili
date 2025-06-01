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
import top.jiehuan.kilikili.Model.PageInfo;
import top.jiehuan.kilikili.Model.VideoInfo;
import top.jiehuan.kilikili.util.*;

public class UserVideoListPage extends Page implements CommandListener {
	public static final short PageID = 8;
	public static int maxVideosNum = 10;
	
	List video_list;
	Command view_cover;
	Command go;
	Command last_page;
	Command next_page;
	
	private short page_num;
	private Vector last_aid;
	private short video_counts;
	private String[] aids;
	private String[] titles;
	private String[] bvids;
	
	public UserVideoListPage(Vector page_info_list){
		super(page_info_list);
		try{
			video_info = page_info.getVideoInfo();
		}catch(PageInfoEmptyException e){
			displayErrorAlert("Page is Empty:"+e.getMessage());
		}
		video_list=new List(video_info.getUserName()+lang_res.getValue("video_list"),List.IMPLICIT);
		
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
                	new UserInfoPage(page_info_list);
                }
            }).start();
        }
        else if(c==exit){
        	ml.exitApp();
        }else if(c==view_cover){
        	new Thread(new Runnable() {
                public void run() {
                	try {
                		VideoInfo cover_info= new VideoInfo(bvids[video_list.getSelectedIndex()]);
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
        	last_aid.removeElementAt(last_aid.size()-1);
        	last_aid.removeElementAt(last_aid.size()-1);
        	try {
        		refreshPage(last_aid.lastElement().toString());
				display();
			} catch (Exception e) {
				e.printStackTrace();
				displayErrorAlert(e.getMessage());
			} 
        }else if(c == next_page){
        	page_num++;
        	page_info.setPage(page_num);
        	try {
				refreshPage(last_aid.lastElement().toString());
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
		String bvid = bvids[video_list.getSelectedIndex()];
    	PageInfo newpage = new PageInfo(GetVideoInfoPage.PageID,ml);
    	try {
			newpage.setVideoInfo(bvid);
		} catch (Exception e) {
			e.printStackTrace();
			displayErrorAlert(e.getMessage());
		}
    	page_info_list.addElement(newpage);
        new GetVideoInfoPage(page_info_list);
	}
	
	private void refreshPage(String aid) throws WebReturnErrorCodeException, IOException, ErrorVideoStatusException{
		video_list=new List(video_info.getUserName()+lang_res.getValue("video_list"),List.IMPLICIT);
		
		System.out.println("start to get user video data");
		String video_data;
		video_data = getWeb(aid);
		
		//page_info.setContent(video_data);
		System.out.println("Video_data is:"+video_data);
		titles=FindString.extractContents(video_data,"\"title\"");
		bvids=FindString.extractContents(video_data,"\"bvid\"");
		aids=FindString.extractContents(video_data,"\"param\"");
		
		String laid = "";
		video_counts = 0;
		for(int i=0;i<maxVideosNum;i++){	//在列表内添加用户视频的标题
			if(titles[i]!=null){
				System.out.println(titles[i]);
				System.out.println(bvids[i]);
				video_list.append(titles[i], null);
				laid = aids[i];
				video_counts++;
			}else break;
		}
		last_aid.addElement(laid);
	}
	
	protected void initPageVars(){
		page_num = 1;
		last_aid = new Vector();
		
		try {
			refreshPage("");
		} catch (Exception e) {
			displayErrorAlert("UserVideoListPage initPageVars Error:"+e.getMessage());
		} 
	}
	
	private String getWeb(String aid) throws WebReturnErrorCodeException, IOException, ErrorVideoStatusException{
		return URLget.BackWeb(URLget.GET_USER_VIDEOS_URL+"mid="+video_info.getUserMID()+"&aid="+aid);
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
		if(video_counts == maxVideosNum){
			video_list.addCommand(next_page);
		}
		
	}
	
}
