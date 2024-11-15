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
import top.jiehuan.kilikili.Exception.PageInfoEmptyException;
import top.jiehuan.kilikili.util.FindString;
import top.jiehuan.kilikili.util.GetLangRes;
import top.jiehuan.kilikili.util.URLget;

public class UserVideoListPage implements CommandListener {
	public static short PageID = 8;
	public static int maxVideosNum = 10;
	
	private MainMIDlet ml;
	GetLangRes lang_res;
	Display display;
	
	List video_list;
	Command back;
	Command exit;
	Command view_cover;
	Command go;
	
	private VideoInfo video_info;
	private Vector page_info_list;
	private PageInfo page_info;
	
	String[] titles;
	String[] bvids;
	
	public UserVideoListPage(MainMIDlet ml, Vector page_info_list){
		this.ml=ml;
		display = Display.getDisplay(ml);
		this.page_info_list = page_info_list;
		page_info = (PageInfo) page_info_list.lastElement();
		loadMessages();
		
		try {
			video_info = page_info.getVideoInfo();
		} catch (PageInfoEmptyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			displayErrorAlert(e.getMessage());
		}
		
		
		
		try{
			initPageVars();
			initDisplayVars();
			display();
			
		}catch(Exception e){
			displayErrorAlert(e.getMessage());
		}
		
		
	}
	
	private void displayErrorAlert(String error){
		Form form=new Form(lang_res.getValue("findError"));
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
	
	public void commandAction(Command c, Displayable d) {
        if (c == back) {
            new Thread(new Runnable() {
                public void run() {
                	page_info_list.removeElementAt(page_info_list.size()-1);
                	new UserInfoPage(ml, page_info_list);
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
                	String bvid = bvids[video_list.getSelectedIndex()];
                	PageInfo newpage = new PageInfo(GetVideoInfoPage.PageID);
                	newpage.setVideoInfo(bvid);
                	page_info_list.addElement(newpage);
                    new GetVideoInfoPage(ml, page_info_list);
                }
            }).start();
        }else if (d == video_list) {
            // 检查是否是通过选择列表项触发的 OK 键
            int selectedIndex = video_list.getSelectedIndex();
            if (selectedIndex != -1) {
                String bvid = bvids[selectedIndex];
                System.out.println("Selected BVID: " + bvid);
                PageInfo newpage = new PageInfo(GetVideoInfoPage.PageID);
            	newpage.setVideoInfo(bvid);
            	page_info_list.addElement(newpage);
                new GetVideoInfoPage(ml, page_info_list); // 创建新的页面以显示视频信息
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
		
		System.out.println("start to get user video data");
		String video_data;
		video_data = URLget.BackWeb(URLget.GET_USER_VIDEOS_URL+"mid="+video_info.getUserMID());
		
		//page_info.setContent(video_data);
		System.out.println("Video_data is:"+video_data);
		titles=FindString.extractContents(video_data,"\"title\"");
		bvids=FindString.extractContents(video_data,"\"bvid\"");
		video_list=new List(video_info.getUserName()+lang_res.getValue("video_list"),List.IMPLICIT);
		for(int i=0;i<maxVideosNum;i++){	//在列表内添加用户视频的标题
			if(titles[i]!=null){
				System.out.println(titles[i]);
				System.out.println(bvids[i]);
				video_list.append(titles[i], null);
			}else break;
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
		video_list.addCommand(back);
		video_list.addCommand(exit);
		video_list.addCommand(go);
		video_list.setSelectCommand(go);
		video_list.addCommand(view_cover);
		video_list.setCommandListener(this);
		display.setCurrent(video_list);
	}

}
