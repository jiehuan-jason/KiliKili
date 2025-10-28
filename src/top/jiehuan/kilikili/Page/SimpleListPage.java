package top.jiehuan.kilikili.Page;

import java.io.IOException;
import java.util.Vector;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

import top.jiehuan.kilikili.Exception.PageInfoEmptyException;
import top.jiehuan.kilikili.Model.FavFolderInfo;
import top.jiehuan.kilikili.Model.VideoInfo;
import top.jiehuan.kilikili.util.*;

public class SimpleListPage extends Page implements CommandListener{
	
	public static final short PageID = 3;
	
	private List list;
	private Command go;
	private Command view_cover;
	private Command last_page;
	private Command next_page;
	private Command refresh;
	private String[] bvidList;
	private String[] avidList;
	private boolean[] isVideoInvalid;
	
	static int maxVideosNum = 15;
	private short video_counts = 0;
	private FavFolderInfo favFolderInfo;
	private boolean nextPageStatus = true;
	
	private String keyword;
	
	private int page_num = 1;
	private String listName;

	private int type = 0; // 0 = RCMD; 1 = SEARCH; 2 = FAV
	
	public SimpleListPage(Vector page_info_list){
		//初始化变量和界面
		super(page_info_list);
		
		//get page type
		if(page_info.getIsPageSet())
				page_num = page_info.getPage();
		type = page_info.getType();
		
		//get page's name and get necessary info
		if(type == 0)
			listName = lang_res.getValue("rcmd_list");
		else if(type == 1){
			listName = lang_res.getValue("search_list");
			try {
			keyword = URLget.urlEncode(page_info.getSearchKeyword());
			
		} catch (PageInfoEmptyException e1) {
			e1.printStackTrace();
			displayErrorAlert("1"+e1.getMessage());
		}	
		}else if(type == 2){
			listName = lang_res.getValue("fav_list");
		}

		//init the list
		list = new List(listName,List.IMPLICIT);

		//init
		try{
			initDisplayVars();
			initPageVars();
			display();
		}catch(Exception e){
			displayErrorAlert(e.getMessage());
		}
	
	}
	
	public short getPageID() {
        return PageID;
 	}
	
	 public void commandAction(Command c, Displayable d) {
		 	// 返回主界面
	        if (c == back) {
	        	new Thread(new Runnable() {
                	public void run() {
                		page_info_list.removeElementAt(page_info_list.size()-1);
                		back(page_info_list);
                	}
            	}).start();
	        }
	        // 退出app
	        else if(c==exit){
	        	ml.exitApp();
	        }else if(c==view_cover){
	        	new Thread(new Runnable() {
	                public void run() {
	                	try {
	                		VideoInfo cover_info= new VideoInfo(bvidList[list.getSelectedIndex()]);
	                		System.out.println("cover_url is:"+cover_info.getCoverURL());
	                		//勿忘字符编码转换
							ml.platformRequest(new String(cover_info.getCoverURL().getBytes("UTF-8"),"UTF-8"));
						} catch (Exception e) {
							e.printStackTrace();
							displayErrorAlert(e.getMessage());
						} 
	                	
	                }
	            }).start();
	        }else if(c==refresh){
        		list = null;
        		list=new List(lang_res.getValue("rcmd_list"),List.IMPLICIT);
        		page_info.emptyContent();
        		new Thread(new Runnable() {
                	public void run() {
                		initPageVars();
                		display();
                	}
            	}).start();
        	}else if(c==go){
	        	new Thread(new Runnable() {
	                public void run() {
	                	//跳转内容在此method处理
	                	goToVideoListPage();
	                }
	            }).start();
	        }else if(c == last_page){
	        	page_num--;
	        	System.out.println("now page is "+page_num);
	        	page_info.setPage(page_num);
	        	if(type == 1)
	        		page_info.setKeyword(keyword);
	        	refreshPage();
	        }else if(c == next_page){
	        	page_num++;
	        	System.out.println("now page is "+page_num);
	        	page_info.setPage(page_num);
	        	if(type == 1)
	        		page_info.setKeyword(keyword);
	        	refreshPage();
	        }
	        else if (d == list) {
	            // 检查是否是通过选择列表项触发的 OK 键
	            int selectedIndex = list.getSelectedIndex();
	            if (selectedIndex != -1) {
	            	goToVideoListPage();
	            }else{
	            	displayErrorAlert("未选择！");
	            }
	        }
	    }
	 private void refreshPage(){
	 //清空page_info和list的信息确保刷新完成
		 page_info.emptyContent();
		 list=null;

		 list = new List(listName,List.IMPLICIT);

		//重新初始化
		 new Thread(new Runnable() {
             public void run() {
            	 try {
         			initPageVars();
         			display();
         		 } catch (Exception e) {
         			e.printStackTrace();
         			displayErrorAlert(e.getMessage());
         		 } 
             }
         }).start();
	 }

	 protected void initPageVars(){
		avidList = new String[maxVideosNum];
		isVideoInvalid = new boolean[maxVideosNum];

		for(int i=0;i<maxVideosNum;i++)
			isVideoInvalid[i] = false;
		
		try{
			if(type == 2)
				favFolderInfo = page_info.getFavFolderInfo();
		}catch(Exception e){
			displayErrorAlert("SimpleListPage initPageVars Error:"+e.getMessage());
		}

		String web="";
		if(page_info.getIsContentSet())
			try {
				web = page_info.getContent();
			} catch (PageInfoEmptyException e1) {
				//TODO 肯定不为空
			}
		else {
			//获取网页内容
			try {
				if(type == 0)
					web = URLget.BackWeb(URLget.RCMD_URL+"?fresh_idx="+page_num);
				else if(type == 1)
					web = URLget.BackWebWithMoreInfo(URLget.SEARCH_URL+"?search_type=video&keyword="+keyword+"&page="+page_num).content;
				else if(type == 2)
					web = URLget.BackWebWithMoreInfo(URLget.GET_FAV_LIST_URL+"?media_id="+favFolderInfo.getID()+"&ps="+maxVideosNum+"&pn="+page_num).content;
			} catch (Exception e) {
				displayErrorAlert("SimpleListPage initPageVars Error:"+e.getMessage()+web);
			} 
		}
		System.out.println(web);
		page_info.setContent(web);

		//解析
		String[] titles = new String[1];
		String[] typeList = new String[1];
		String[] bvidListAll = new String[1];
		String[] avidList = new String[1];
		bvidList = new String[maxVideosNum];
		//list.append("test",null);
		//list.append(web, null);
		try{
			web = URLget.decodeUnicode(web);
			bvidListAll=FindString.extractContents(web,"\"bvid\"");
			if(type == 0){
				titles=FindString.extractContents(web,"\"title\"");
				bvidList = bvidListAll;
			}else if(type == 1){
				titles=FindString.FindTitleAndDeleteHtmlCode(web);
				typeList=FindString.extractContents(web,"\"type\"");
			}else if(type == 2){
				titles=FindString.extractContents(web,"\"title\"");
				avidList=FindString.extractContentsInt(web,"\"id\""); //第一个ID是收藏夹的id，所以使用时要+1
			}
			
		}catch (Exception e) {
			displayErrorAlert("SimpleListPage initPageVars Part 2 Error:"+e.getMessage()+web);
		} 
		
		//String[] titles=FindString.FindTitle(web);
	    //bvidList=FindString.FindBVID(web);
	    //String[] typeList=FindString.FindVideoType(web);

	    //添加到list
		try{
			int j=0;
			for(int i=0;i<maxVideosNum&&i<titles.length;i++){
				if(!(titles[i] == null||bvidListAll[i] == null)){
		
				System.out.println("str:"+titles[i]);
				System.out.println("bvid:"+bvidListAll[i]);
				if(type == 0){
					list.append(titles[i], null);
				}
				else if(type == 1){
					if(typeList[i].equals("video")){
						list.append(titles[i], null);
						bvidList[j]= bvidListAll[i];
						j++;
					}
				}
				else if(type == 2){
					list.append(titles[i+1], null);
					this.avidList[i] = avidList[i+1];
					video_counts++;
					if(titles[i+1].equals("已失效视频"))
						isVideoInvalid[i] = true;
				}
		
			}
		}
		}catch (Exception e) {
			//displayErrorAlert("SimpleListPage initPageVars Part 3 Error:"+e.getMessage()+web);
			//TODO 这里报空指针错误 但是不妨碍功能 就没有处理 之后看看到底哪里有问题
		} 

		// set the next page status. Only Fav.
		if(type == 2){
			if(video_counts >= Integer.parseInt(favFolderInfo.getMediaCount()))
				nextPageStatus = false;
			
			String content = "";
			try{
				content = URLget.BackWebWithMoreInfo(URLget.GET_FAV_LIST_URL+"?media_id="+favFolderInfo.getID()+"&ps="+maxVideosNum+"&pn="+page_num).content;
				if(FindString.findValueBool(content, "medias").equals("null"))
					nextPageStatus = false;
			}catch(Exception e){
				nextPageStatus = false;
				displayErrorAlert("SimpleListPage initPageVars Error:"+e.getMessage());
			}

			
		}

	 }
	 protected void initDisplayVars(){
		initBackAndExitCommand();
		go=new Command(lang_res.getValue("go"),Command.OK,1);
		last_page=new Command(lang_res.getValue("last_page"),Command.OK,2);
		next_page=new Command(lang_res.getValue("next_page"),Command.OK,2);
		refresh = new Command(lang_res.getValue("refresh"),Command.ITEM,1);
		view_cover=new Command(lang_res.getValue("view_cover"),Command.ITEM,2);
	 }
	 protected void display(){
		list.addCommand(back);
		list.addCommand(go);
		list.setSelectCommand(go);
		if(type == 0){
			try {
				if(new CookiesUtils("isLogin").isTokenStored())
					list.addCommand(refresh);
			} catch (Exception e) {
				displayErrorAlert(e.getMessage());
			}
		}
		
		if(!(page_num==1))
			list.addCommand(last_page);
		if(type == 2)
			if(nextPageStatus)
				list.addCommand(next_page);
		else
			if(page_num<=20)
				list.addCommand(next_page);
		
		list.addCommand(view_cover);
		list.addCommand(exit);
		
		list.setCommandListener(this);
		display.setCurrent(list);

	 }
	 private void goToVideoListPage(){
	 	String bvid = "";
	 	if(type == 2){
	 	//判断是否失效 若非则转换avid为bvid
	 		if(isVideoInvalid[list.getSelectedIndex()]){
				displayErrorAlert("视频已失效！");
				return;
			}
	 		try{
	 			bvid = VideoUtils.avidToBvid(avidList[list.getSelectedIndex()]);
	 		}catch(Exception e){
	 			displayErrorAlert("SimpleListPage avidToBvid Error:"+e.getMessage());
	 		}
		}else
			bvid = bvidList[list.getSelectedIndex()];

     	System.out.println(bvid);
     	
     	super.goToVideoListPage(bvid);
     	
	 }

}
