package top.jiehuan.kilikili.Page;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.lcdui.*;

import top.jiehuan.kilikili.MainMIDlet;
import top.jiehuan.kilikili.PageInfo;
import top.jiehuan.kilikili.VideoInfo;
import top.jiehuan.kilikili.Exception.PageInfoEmptyException;
import top.jiehuan.kilikili.util.FindString;
import top.jiehuan.kilikili.util.GetLangRes;
import top.jiehuan.kilikili.util.URLget;

public class RecommendPage implements CommandListener {
	
	public static short PageID = 2;
	
	static int maxVideosNum = 20;
	
	// 定义需要的变量
	private MainMIDlet ml;
	GetLangRes lang_res;
	List rcmd_list;
	Display display;
	Command back;
	Command exit;
	Command go;
	Command view_cover;
	Form form;
	
	String[] titles;
	String[] bvids;
	
	PageInfo page_info;
	Vector page_info_list;
	
	public RecommendPage(MainMIDlet midlet, Vector page_info_list){
		// 初始化变量和界面
		ml=midlet;
		display = Display.getDisplay(midlet);
		
		this.page_info_list = page_info_list;
		page_info = (PageInfo) page_info_list.lastElement();
		loadMessages();
		
		try{
			initDisplayVars();
			initPageVars();
			
			display();
		}catch(Exception e){
			e.printStackTrace();
			System.out.println(e.getMessage());
			form=new Form("Error");
			back=new Command(lang_res.getValue("back"),Command.BACK,1);
			exit=new Command(lang_res.getValue("exit"),Command.EXIT,0);
			form.append(new StringItem("","获取错误"));
			form.addCommand(back);
			form.addCommand(exit);
			form.setCommandListener(this);
			Alert alert = new Alert("Error", e.getMessage(), null, AlertType.ERROR);
            alert.setTimeout(Alert.FOREVER); // 设置为永远显示，直到用户操作
            display.setCurrent(alert, form);
		}
	}
	
	//命令的执行函数 详细内容请参考MainMIDlet文件
	public void commandAction(Command c, Displayable d) {
        if (c == back) {
            new Thread(new Runnable() {
                public void run() {
                	new MainPage(ml);
                }
            }).start();
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
                	String bvid = bvids[rcmd_list.getSelectedIndex()];
                	PageInfo newpage = new PageInfo(PartVideoListPage.PageID);
                	newpage.setVideoInfo(bvid);
                	page_info_list.addElement(newpage);
                    new PartVideoListPage(ml, page_info_list);
                }
            }).start();
        }else if (d == rcmd_list) {
            // 检查是否是通过选择列表项触发的 OK 键
            int selectedIndex = rcmd_list.getSelectedIndex();
            if (selectedIndex != -1) {
                String bvid = bvids[selectedIndex];
                System.out.println("Selected BVID: " + bvid);
                PageInfo newpage = new PageInfo(PartVideoListPage.PageID);
            	newpage.setVideoInfo(bvid);
            	page_info_list.addElement(newpage);
                new PartVideoListPage(ml, page_info_list); // 创建新的页面以显示视频信息
            }
        }
    }
	private void loadMessages() {
        // 根据系统语言加载相应的资源文件
        try {
        	System.out.println(System.getProperty("microedition.locale"));
			lang_res = new GetLangRes(System.getProperty("microedition.locale"));
			//System.out.println(lang_res.getLangFileContent());
		} catch (IOException e) {
			e.printStackTrace();
		}
    }	
	
	private void initPageVars() throws Exception{
		
		System.out.println("start to get rcmd data");
		String rcmd_data;
		try{
			rcmd_data = page_info.getContent();
		}catch(PageInfoEmptyException e){
			//rcmd_data = URLget.BackWebHttps("https://api.bilibili.com/x/web-interface/wbi/index/top/feed/rcmd");
			rcmd_data = URLget.BackWeb(URLget.RCMD_URL);
		}
		
		page_info.setContent(rcmd_data);
		titles=FindString.extractContents(rcmd_data,"\"title\"");
		bvids=FindString.extractContents(rcmd_data,"\"bvid\"");
		rcmd_list=new List(lang_res.getValue("rcmd_list"),List.IMPLICIT);
		for(int i=0;i<maxVideosNum;i++){//在列表内添加推荐视频的标题
			if(titles[i]==null||bvids[i]==null){
				break;
			}
			System.out.println(titles[i]);
			System.out.println(bvids[i]);
			rcmd_list.append(titles[i], null);
		}
	}
	private void initDisplayVars(){
		System.out.println("start to initDisplayVars");
		view_cover=new Command(lang_res.getValue("view_cover"),Command.ITEM,2);
		exit=new Command(lang_res.getValue("exit"),Command.EXIT,3);
		back=new Command(lang_res.getValue("back"),Command.BACK,0);
		go = new Command(lang_res.getValue("go"),Command.OK,1);
	}
	private void display(){
		System.out.println("start to display");
		rcmd_list.addCommand(back);
		rcmd_list.addCommand(exit);
		rcmd_list.addCommand(go);
		rcmd_list.setSelectCommand(go);
		rcmd_list.addCommand(view_cover);
		rcmd_list.setCommandListener(this);
		display.setCurrent(rcmd_list);
	}
	 
}
