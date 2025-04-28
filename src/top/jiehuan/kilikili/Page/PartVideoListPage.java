package top.jiehuan.kilikili.Page;

import java.util.Vector;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

import top.jiehuan.kilikili.Exception.PageInfoEmptyException;
import top.jiehuan.kilikili.Model.PageInfo;
import top.jiehuan.kilikili.Model.PartInfo;
import top.jiehuan.kilikili.Model.VideoInfo;
import top.jiehuan.kilikili.util.URLget;

public class PartVideoListPage extends Page implements CommandListener{
	public static final short PageID = 9;
	private static final short PARTS_IN_PAGE = 20;
	
	List videos_list;
	Command go;
	Command last_page;
	Command next_page;
	
	private long cid_list[];
	private String part_title_list[];
	
	private int page_pn;
	private int parts;
	private int pages;
	
	public PartVideoListPage(Vector page_info_list){
		super(page_info_list);
		
		try{
			video_info = page_info.getVideoInfo();
		}catch(PageInfoEmptyException e){
			displayErrorAlert("Page is Empty:"+e.getMessage());
		}
		
		videos_list = new List(video_info.getTitle(),List.IMPLICIT);
		
		
		
		initPageVars();
		initDisplayVars();
		display();
	}
	
	public void commandAction(Command c, Displayable d) {
		 	// 返回主界面
	        if (c == back) {
	        	backMainPage();
	        	//TODO 处理返回逻辑
	        }
	        // 退出app
	        else if(c==exit){
	        	ml.exitApp();
	        }else if(c==go){
	        	new Thread(new Runnable() {
	                public void run() {
	                	goInfoPage();
	                }
	            }).start();
	        }else if(c == last_page){
	        	page_pn--;
	        	page_info.setPage(page_pn);
	        	refresh();
	        }else if(c == next_page){
	        	page_pn++;
	        	page_info.setPage(page_pn);
	        	refresh();
	        }
	        else if (d == videos_list) {
	            // 检查是否是通过选择列表项触发的 OK 键
	            int selectedIndex = videos_list.getSelectedIndex();
	            if (selectedIndex != -1) {
	            	new Thread(new Runnable() {
	                    public void run() {
	                    	goInfoPage();
	                    }
	            	}).start();
	            }else{
	            	displayErrorAlert("未选择！");
	            }
	        }
	    }
	private void refresh(){
		page_info.emptyContent();
		videos_list = null;
		videos_list = new List(video_info.getTitle(),List.IMPLICIT);
		new Thread(new Runnable() {
            public void run() {
            	try {
        			initPageVars();
        			initDisplayVars();
        			display();
        		} catch (Exception e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        			displayErrorAlert(e.getMessage());
        		} 
            }
        }).start();
	}
	private void goInfoPage(){
		long cid = cid_list[videos_list.getSelectedIndex()];
    	int parts_base = (page_pn-1)*PARTS_IN_PAGE;
    	System.out.println(cid);
    	PageInfo newpage = new PageInfo(GetVideoInfoPage.PageID,ml);
    	VideoInfo newvideo = video_info;
    	try {
			newvideo.setBVID(video_info.getBVID(), videos_list.getSelectedIndex()+parts_base+1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			displayErrorAlert(e.getMessage());
		}
    	newpage.setVideoInfo(newvideo);
    	page_info_list.addElement(newpage);
    	System.out.println("PartVideoPage call GetVideoInfoPage");
        new GetVideoInfoPage(page_info_list);
	}
	
	//TODO 重构此函数！！！！
	 protected void initPageVars(){
		 try {
				parts = video_info.getVideoParts();
				if(parts == 1){
					new Thread(new Runnable() {
		                public void run() {
		                	PageInfo newpage = new PageInfo(GetVideoInfoPage.PageID,ml);
		                	newpage.setVideoInfo(video_info);
		                	page_info_list.removeElement(page_info_list.lastElement());
		                	page_info_list.addElement(newpage);
		                	for(int i=0;i<page_info_list.size();i++){
	                    		PageInfo info = (PageInfo)(page_info_list.elementAt(i));
	                    		System.out.println("page num in"+i+" is:"+info.pageID);
	                    	}
							new GetVideoInfoPage(page_info_list);
		                }
		            }).start();
				}
				pages = (parts - (parts%PARTS_IN_PAGE))/PARTS_IN_PAGE + 1;
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println(e.getMessage());
				displayErrorAlert(e.getMessage());
			}
		 if(page_info.getIsPageSet())
			 page_pn = page_info.getPage();
		 else{
			 page_pn = 1;
			 page_info.setPage(page_pn);
		 }
		 
		 try{
			 cid_list = new long[PARTS_IN_PAGE];
			 part_title_list = new String[PARTS_IN_PAGE];
			 int base = (page_pn-1)*PARTS_IN_PAGE;
			 String content = URLget.BackWeb(URLget.GET_VIDEOS_PAGE_LIST_URL+"bvid="+video_info.getBVID());
			 System.out.println(content);
			 PartInfo part;
			 System.gc();
			 System.out.println(" before for");
			 for(int i=1;i<=PARTS_IN_PAGE && i+base<=parts;i++){
				 System.out.println("for i="+i);
				 part = null;
				 part = new PartInfo(video_info.getBVID(),i+base,content);
				 part_title_list[i-1] = part.getPartTitle();
				 cid_list[i-1]=part.getCID();
			 }
		 }catch(Exception e){
			 System.out.println(e.getMessage());
			 displayErrorAlert(e.getMessage());
		 }
		 
	 }
	 
	 protected void initDisplayVars(){
		 System.out.println("initDisplayVars starts");
		 for(int i=1;i<=PARTS_IN_PAGE;i++){
			 System.out.println("for i="+i+" ptitle="+part_title_list[i-1]);
			 if(part_title_list[i-1]!=null)
				 videos_list.append(part_title_list[i-1], null);
			 else break;
		 }
		 back=new Command(lang_res.getValue("back"),Command.BACK,1);
		 exit=new Command(lang_res.getValue("exit"),Command.EXIT,0);
		 go=new Command(lang_res.getValue("go"),Command.OK,1);
	     last_page=new Command(lang_res.getValue("last_page"),Command.OK,2);
		 next_page=new Command(lang_res.getValue("next_page"),Command.OK,2);
	 }
	 
	 protected void display(){
		 System.out.println("display starts");
		 videos_list.addCommand(back);
		 videos_list.addCommand(exit);
		 videos_list.addCommand(go);
		 if(page_pn != 1)
			 videos_list.addCommand(last_page);
		 if(page_pn != pages)
			 videos_list.addCommand(next_page);
		 videos_list.setSelectCommand(go);
		 videos_list.setCommandListener(this);
		 display.setCurrent(videos_list);
	 }
}
