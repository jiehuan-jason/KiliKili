package top.jiehuan.kilikili;

import java.io.UnsupportedEncodingException;

import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.StringItem;

public class SearchPage implements CommandListener{
	
	public static String PageID = "3";
	
	private MainMIDlet ml;
	Display display;
	List search_list;
	Command back;
	Command exit;
	Command go;
	Command view_cover;
	Form form;
	String[] list_bvid;
	
	static int maxVideosNum = 10;
	
	private String searchTitleString;
	private String backString;
	private String exitString;
	private String goString;
	private String viewCoverString;
	//private String keyword;
	private VideoInfo video_info;
	
	public SearchPage(MainMIDlet midlet,VideoInfo video_info){
		//this.keyword=keyword;
		String keyword = URLget.urlEncode(video_info.getSearchKeyword());
		System.out.println("keyword:"+keyword);
		//初始化变量和界面
		ml=midlet;
		display = Display.getDisplay(midlet);
		this.video_info=video_info;
		this.video_info.setPageNum(MainPage.addPageNum(PageID,video_info));
		
		System.out.println("start loadMessages");
		
		loadMessages();
		
		System.out.println("start get web");
		search_list=new List(searchTitleString,List.IMPLICIT);
		String web=URLget.BackWeb(URLget.SEARCH_URL+keyword);
		if(web.startsWith("error")){
			form=new Form("Error");
			back=new Command(backString,Command.BACK,1);
			exit=new Command(exitString,Command.EXIT,0);
			form.append(new StringItem("","获取错误"));
			form.addCommand(back);
			form.addCommand(exit);
			form.setCommandListener(this);
			Alert alert = new Alert("Error", "获取错误", null, AlertType.ERROR);
            alert.setTimeout(Alert.FOREVER); // 设置为永远显示，直到用户操作
            display.setCurrent(alert, form);
            //ml.display.setCurrent(ml.form);
		}else{
		String[] list_str=FindString.FindTitle(web);
	    list_bvid=FindString.FindBVID(web);
		
		for(int i=0;i<maxVideosNum;i++){	//在列表内添加搜索到的视频的标题
			System.out.println(list_str[i]);
			System.out.println(list_bvid[i]);
			search_list.append(list_str[i], null);
		}
		
		back=new Command(backString,Command.BACK,1);
		exit=new Command(exitString,Command.EXIT,0);
		go=new Command(goString,Command.OK,1);
		view_cover=new Command(viewCoverString,Command.ITEM,2);
		search_list.addCommand(back);
		search_list.addCommand(go);
		search_list.addCommand(exit);
		search_list.addCommand(view_cover);
		search_list.setCommandListener(this);
		display.setCurrent(search_list);
		}
	}
	 public void commandAction(Command c, Displayable d) {
		 	// 返回主界面
	        if (c == back) {
	            new Thread(new Runnable() {
	                public void run() {
	                	new MainPage(ml);
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
	                		VideoInfo cover_info= new VideoInfo(list_bvid[search_list.getSelectedIndex()]);
	                		System.out.println("cover_url is:"+cover_info.getCoverURL());
							ml.platformRequest(new String(cover_info.getCoverURL().getBytes("UTF-8"),"UTF-8"));
						} catch (ConnectionNotFoundException e) {
							e.printStackTrace();
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
	                	
	                }
	            }).start();
	        }else if(c==go){
	        	new Thread(new Runnable() {
	                public void run() {
	                	String bvid = list_bvid[search_list.getSelectedIndex()];
	                	System.out.println(bvid);
	                	video_info.setBVID(bvid);
	                    new GetVideoInfoPage(ml, video_info);
	                }
	            }).start();
	        }else if (d == search_list) {
	            // 检查是否是通过选择列表项触发的 OK 键
	            int selectedIndex = search_list.getSelectedIndex();
	            if (selectedIndex != -1) {
	                String bvid = list_bvid[selectedIndex];
	                System.out.println("Selected BVID: " + bvid);
	                video_info.setBVID(bvid);
	                new GetVideoInfoPage(ml, video_info);
	                }
	        }
	    }
	 private void loadMessages() {
	        // 根据系统语言加载相应的资源文件
	        
	        if (System.getProperty("microedition.locale").equals("zh-CN")) {
	        	exitString="退出";
	        	backString="返回";
	        	goString="前往";
	        	searchTitleString="搜索结果";
	        	viewCoverString="显示封面";
	        } else {
	        	exitString="Exit";
	        	backString="Back";
	        	goString="Go";
	        	searchTitleString="Results";
	        	viewCoverString="View the Cover";
	        }
	    }
}
