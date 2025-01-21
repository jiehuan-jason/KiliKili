package top.jiehuan.kilikili.Page;

import java.util.Vector;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.List;

import top.jiehuan.kilikili.PageInfo;
import top.jiehuan.kilikili.VideoInfo;
import top.jiehuan.kilikili.Exception.PageInfoEmptyException;
import top.jiehuan.kilikili.util.*;

public class SearchPage extends Page implements CommandListener{
	
	public static final short PageID = 3;
	
	List search_list;
	Command go;
	Command view_cover;
	Command last_page;
	Command next_page;
	Form form;
	String[] list_bvid;
	
	static int maxVideosNum = 15;
	
	private String keyword;
	
	private int page_num = 1;
	
	public SearchPage(Vector page_info_list){
		//初始化变量和界面
		super(page_info_list);
		
		search_list=new List(lang_res.getValue("search_list"),List.IMPLICIT);
		
		try {
			keyword = URLget.urlEncode(page_info.getSearchKeyword());
			page_num = page_info.getSearchPage();
		} catch (PageInfoEmptyException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			displayErrorAlert(e1.getMessage());
		}
		try{
			initPageVars();
			initDisplayVars();
			display();

		}catch(Exception e){
			e.printStackTrace();
			displayErrorAlert(e.getMessage());
		}
	}
	 public void commandAction(Command c, Displayable d) {
		 	// 返回主界面
	        if (c == back) {
	        	backMainPage();
	        }
	        // 退出app
	        else if(c==exit){
	        	ml.exitApp();
	        }else if(c==view_cover){
	        	new Thread(new Runnable() {
	                public void run() {
	                	try {
	                		VideoInfo cover_info= new VideoInfo(list_bvid[search_list.getSelectedIndex()]);
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
	                	goToVideoListPage();
	                }
	            }).start();
	        }else if(c == last_page){
	        	page_num--;
	        	page_info.setSearchInfo(keyword, page_num);
	        	try {
					initPageVars();
					display();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					displayErrorAlert(e.getMessage());
				} 
	        }else if(c == next_page){
	        	page_num++;
	        	page_info.setSearchInfo(keyword, page_num);
	        	try {
					initPageVars();
					display();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					displayErrorAlert(e.getMessage());
				} 
	        }
	        else if (d == search_list) {
	            // 检查是否是通过选择列表项触发的 OK 键
	            int selectedIndex = search_list.getSelectedIndex();
	            if (selectedIndex != -1) {
	            	goToVideoListPage();
	            }else{
	            	displayErrorAlert("未选择！");
	            }
	        }
	    }
	 protected void initPageVars(){
		
		String web="";
		if(page_info.getIsContentSet())
			try {
				web = page_info.getContent();
			} catch (PageInfoEmptyException e1) {
				//TODO 肯定不为空
			}
		else {
			try {
				web = URLget.BackWeb(URLget.SEARCH_URL+keyword+"&page="+page_num);
			} catch (Exception e) {
				displayErrorAlert("SearchPage initPageVars Error:"+e.getMessage());
			} 
		}
		
		String[] list_str=FindString.FindTitle(web);
	    list_bvid=FindString.FindBVID(web);
		
		for(int i=0;i<maxVideosNum;i++){
			if(!((list_str[i].equals(null))||(list_bvid[i].equals(null)))){//在列表内添加搜索到的视频的标题
			System.out.println("str:"+list_str[i]);
			System.out.println("bvid:"+list_bvid[i]);
			search_list.append(list_str[i], null);}
			else{
				break;
			}
		}

	 }
	 protected void initDisplayVars(){
		initBackAndExitCommand();
		go=new Command(lang_res.getValue("go"),Command.OK,1);
		last_page=new Command(lang_res.getValue("last_page"),Command.OK,2);
		next_page=new Command(lang_res.getValue("next_page"),Command.OK,2);
		view_cover=new Command(lang_res.getValue("view_cover"),Command.ITEM,2);
	 }
	 protected void display(){
		search_list.addCommand(back);
		search_list.addCommand(go);
		search_list.addCommand(view_cover);
		if(!(page_num==1))
			search_list.addCommand(last_page);
		if(page_num<=20)
			search_list.addCommand(next_page);
		search_list.addCommand(exit);
		
		search_list.setCommandListener(this);
		display.setCurrent(search_list);

	 }
	 private void goToVideoListPage(){
		String bvid = list_bvid[search_list.getSelectedIndex()];
     	System.out.println(bvid);
     	PageInfo newpage = new PageInfo(PartVideoListPage.PageID,ml);
     	try {
			newpage.setVideoInfo(bvid);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			displayErrorAlert(e.getMessage());
		}
     	page_info_list.addElement(newpage);
     	System.out.println("SearchPage call GetVideoInfoPage");
        new PartVideoListPage(page_info_list);
	 }

}
