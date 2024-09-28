package top.jiehuan.kilikili;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

public class SearchPage implements CommandListener{
	private MainMIDlet ml;
	Display display;
	List search_list;
	Command back;
	Command exit;
	Command go;
	String[] list_bvid;
	
	static int maxVideosNum = 10;
	
	private String searchTitleString;
	private String backString;
	private String exitString;
	private String goString;
	//private String keyword;
	
	public SearchPage(MainMIDlet midlet,String keyword){
		//this.keyword=keyword;
		System.out.println("keyword:"+keyword);
		//初始化变量和界面
		ml=midlet;
		display = Display.getDisplay(midlet);
		
		System.out.println("start loadMessages");
		
		loadMessages();
		
		System.out.println("start get web");
		search_list=new List(searchTitleString,List.IMPLICIT);
		String web=URLget.BackWeb(URLget.SEARCH_URL+keyword);
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
		search_list.addCommand(back);
		search_list.addCommand(go);
		search_list.addCommand(exit);
		search_list.setCommandListener(this);
		display.setCurrent(search_list);
	}
	 public void commandAction(Command c, Displayable d) {
		 	// 返回主界面
	        if (c == back) {
	            new Thread(new Runnable() {
	                public void run() {
	                	ml.display.setCurrent(ml.form);
	                }
	            }).start();
	        }
	        // 退出app
	        else if(c==exit){
	        	ml.exitApp();
	        }else if(c==go){
	        	new Thread(new Runnable() {
	                public void run() {
	                	String bvid = list_bvid[search_list.getSelectedIndex()];
	                	System.out.println(bvid);
	                    new GetVideoInfoPage(ml, bvid);
	                }
	            }).start();
	        }
	    }
	 private void loadMessages() {
	        // 根据系统语言加载相应的资源文件
	        
	        if (System.getProperty("microedition.locale").equals("zh-CN")) {
	        	exitString="退出";
	        	backString="返回";
	        	goString="前往";
	        	searchTitleString="搜索结果";
	        } else {
	        	exitString="Exit";
	        	backString="Back";
	        	goString="Go";
	        	searchTitleString="Results";
	        }
	    }
}
