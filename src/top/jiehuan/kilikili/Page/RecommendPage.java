package top.jiehuan.kilikili.Page;

import java.util.Vector;

import javax.microedition.lcdui.*;

import top.jiehuan.kilikili.PageInfo;
import top.jiehuan.kilikili.VideoInfo;
import top.jiehuan.kilikili.Exception.PageInfoEmptyException;
import top.jiehuan.kilikili.util.*;

public class RecommendPage extends Page implements CommandListener {
	
	public static final short PageID = 2;
	
	static int maxVideosNum = 20;
	
	List rcmd_list;
	Command go;
	Command view_cover;
	Form form;
	
	String[] titles;
	String[] bvids;
	
	public RecommendPage(Vector page_info_list){
		// 初始化变量和界面
		super(page_info_list);

		rcmd_list=new List(lang_res.getValue("rcmd_list"),List.IMPLICIT);
		try{
			initDisplayVars();
			initPageVars();
			display();
		}catch(Exception e){
			displayErrorAlert(e.getMessage());
		}
	}
	
	//命令的执行函数 详细内容请参考MainMIDlet文件
	public void commandAction(Command c, Displayable d) {
        if (c == back) {
        	backMainPage();
        }
        else if(c==exit){
        	ml.exitApp();
        }else if(c==view_cover){
        	new Thread(new Runnable() {
                public void run() {
                	try {
                		VideoInfo cover_info= new VideoInfo(bvids[rcmd_list.getSelectedIndex()]);
                		System.out.println("cover_url is:"+cover_info.getCoverURL());
						ml.platformRequest(new String(cover_info.getCoverURL().getBytes("UTF-8"),"UTF-8"));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						displayErrorAlert(e.getMessage());
					}
                	
                }
            }).start();
        }else if(c==go){
        	new Thread(new Runnable() {
                public void run() {
                	String bvid = bvids[rcmd_list.getSelectedIndex()];
                	PageInfo newpage = new PageInfo(PartVideoListPage.PageID,ml);
                	try {
						newpage.setVideoInfo(bvid);
					} catch (Exception e) {
						displayErrorAlert(e.getMessage());
					}
                	page_info_list.addElement(newpage);
                    new PartVideoListPage(page_info_list);
                }
            }).start();
        }else if (d == rcmd_list) {
            // 检查是否是通过选择列表项触发的 OK 键
            int selectedIndex = rcmd_list.getSelectedIndex();
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
	private void goInfoPage(){
		String bvid = bvids[rcmd_list.getSelectedIndex()];
    	PageInfo newpage = new PageInfo(PartVideoListPage.PageID,ml);
    	try {
			newpage.setVideoInfo(bvid);
		} catch (Exception e) {
			displayErrorAlert(e.getMessage());
		}
    	page_info_list.addElement(newpage);
        new PartVideoListPage(page_info_list);
	}
	
	protected void initPageVars(){
		
		System.out.println("start to get rcmd data");
		String rcmd_data = "";
		if(page_info.getIsContentSet()){
			try {
				rcmd_data = page_info.getContent();
			} catch (PageInfoEmptyException e) {
				//TODO 这里绝对不为空 之后把定义改一下
			}
		}else{
			try {
				rcmd_data = URLget.BackWeb(URLget.RCMD_URL);
			} catch(Exception e){
				displayErrorAlert("RcmdPage Error:"+e.getMessage());
			}
		}
		
		page_info.setContent(rcmd_data);
		titles=FindString.extractContents(rcmd_data,"\"title\"");
		bvids=FindString.extractContents(rcmd_data,"\"bvid\"");
		for(int i=0;i<maxVideosNum;i++){//在列表内添加推荐视频的标题
			if(titles[i]==null||bvids[i]==null){
				break;
			}
			System.out.println(titles[i]);
			System.out.println(bvids[i]);
			rcmd_list.append(titles[i], null);
		}
	}
	protected void initDisplayVars(){
		System.out.println("start to initDisplayVars");
		view_cover=new Command(lang_res.getValue("view_cover"),Command.ITEM,2);
		initBackAndExitCommand();
		go = new Command(lang_res.getValue("go"),Command.OK,1);
	}
	protected void display(){
		System.out.println("start to display");
		rcmd_list.addCommand(back);
		rcmd_list.addCommand(go);
		rcmd_list.setSelectCommand(go);
		rcmd_list.addCommand(view_cover);
		rcmd_list.addCommand(exit);
		rcmd_list.setCommandListener(this);
		display.setCurrent(rcmd_list);
	}
	 
}
