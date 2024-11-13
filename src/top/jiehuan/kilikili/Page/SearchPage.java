package top.jiehuan.kilikili.Page;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.List;

import top.jiehuan.kilikili.MainMIDlet;
import top.jiehuan.kilikili.PageInfo;
import top.jiehuan.kilikili.VideoInfo;
import top.jiehuan.kilikili.Exception.ErrorVideoStatusException;
import top.jiehuan.kilikili.Exception.PageInfoEmptyException;
import top.jiehuan.kilikili.Exception.WebReturnErrorCodeException;
import top.jiehuan.kilikili.util.FindString;
import top.jiehuan.kilikili.util.GetLangRes;
import top.jiehuan.kilikili.util.URLget;

public class SearchPage implements CommandListener{
	
	public static short PageID = 3;
	
	private MainMIDlet ml;
	GetLangRes lang_res;
	Display display;
	List search_list;
	Command back;
	Command exit;
	Command go;
	Command view_cover;
	Form form;
	String[] list_bvid;
	
	static int maxVideosNum = 10;
	
	private String keyword;
	
	private Vector page_info_list;
	private PageInfo page_info;
	
	public SearchPage(MainMIDlet midlet,Vector page_info_list){
		//初始化变量和界面
		ml=midlet;
		display = Display.getDisplay(midlet);
		
		//this.keyword=keyword;
		page_info = (PageInfo) page_info_list.lastElement();
		this.page_info_list = page_info_list;
		try {
			keyword = URLget.urlEncode(page_info.getSearchKeyword());
		} catch (PageInfoEmptyException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			displayErrorAlert("获取错误");
		}
		System.out.println("keyword:"+keyword);
		System.out.println("start loadMessages");
		
		loadMessages();
		
		System.out.println("start get web");
		try{
			initPageVars();
			initDisplayVars();
			display();

		}catch(Exception e){
			e.printStackTrace();
			displayErrorAlert("获取错误");
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
	                	PageInfo newpage = new PageInfo(GetVideoInfoPage.PageID);
	                	newpage.setVideoInfo(bvid);
	                	page_info_list.addElement(newpage);
	                    new GetVideoInfoPage(ml, page_info_list);
	                }
	            }).start();
	        }else if (d == search_list) {
	            // 检查是否是通过选择列表项触发的 OK 键
	            int selectedIndex = search_list.getSelectedIndex();
	            if (selectedIndex != -1) {
	                String bvid = list_bvid[selectedIndex];
	                System.out.println("Selected BVID: " + bvid);
	                PageInfo newpage = new PageInfo(GetVideoInfoPage.PageID);
	            	newpage.setVideoInfo(bvid);
	            	page_info_list.addElement(newpage);
	                new GetVideoInfoPage(ml, page_info_list);
	                }
	        }
	    }
	 private void loadMessages() {
	        // 根据系统语言加载相应的资源文件
	        try {
				lang_res = new GetLangRes(System.getProperty("microedition.locale"));
				//System.out.println(lang_res.getLangFileContent());
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
	 private void initPageVars() throws IOException,WebReturnErrorCodeException, ErrorVideoStatusException{
		search_list=new List(lang_res.getValue("search_list"),List.IMPLICIT);
		String web;
		try {
			web = page_info.getContent();
		} catch (PageInfoEmptyException e) {
			web = URLget.BackWeb(URLget.SEARCH_URL+keyword);
		}
		
		String[] list_str=FindString.FindTitle(web);
	    list_bvid=FindString.FindBVID(web);
		
		for(int i=0;i<maxVideosNum;i++){	//在列表内添加搜索到的视频的标题
			System.out.println(list_str[i]);
			System.out.println(list_bvid[i]);
			search_list.append(list_str[i], null);
		}

	 }
	 private void initDisplayVars(){
		back=new Command(lang_res.getValue("back"),Command.BACK,1);
		exit=new Command(lang_res.getValue("exit"),Command.EXIT,0);
		go=new Command(lang_res.getValue("go"),Command.OK,1);
		view_cover=new Command(lang_res.getValue("view_cover"),Command.ITEM,2);
	 }
	 private void display(){
		search_list.addCommand(back);
		search_list.addCommand(go);
		search_list.addCommand(exit);
		search_list.addCommand(view_cover);
		search_list.setCommandListener(this);
		display.setCurrent(search_list);

	 }
	 private void displayErrorAlert(String error){
			form=new Form(lang_res.getValue("findError"));
			back=new Command(lang_res.getValue("back"),Command.BACK,1);
			exit=new Command(lang_res.getValue("exit"),Command.EXIT,0);
			form.addCommand(back);
			form.addCommand(exit);
			form.setCommandListener(this);
			Alert alert = new Alert("Error", error, null, AlertType.ERROR);
         alert.setTimeout(Alert.FOREVER); // 设置为永远显示，直到用户操作
         display.setCurrent(alert, form);
         //ml.display.setCurrent(ml.form);
		}

}
