package top.jiehuan.kilikili;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.lcdui.*;

public class RecommendPage implements CommandListener {
	
	public static String PageID = "2";
	
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
	
	VideoInfo video_info;
	
	public RecommendPage(MainMIDlet midlet,VideoInfo video_info){
		// 初始化变量和界面
		this.video_info = video_info;
		ml=midlet;
		display = Display.getDisplay(midlet);
		this.video_info.setPageNum(MainPage.addPageNum(PageID,video_info));
		loadMessages();
		
		try{
			initPageVars();
			initDisplayVars();
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
			Alert alert = new Alert("Error", "获取错误", null, AlertType.ERROR);
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
                    new GetVideoInfoPage(ml, new VideoInfo(bvid,video_info.getPageNum(),video_info.getPageList()));
                }
            }).start();
        }else if (d == rcmd_list) {
            // 检查是否是通过选择列表项触发的 OK 键
            int selectedIndex = rcmd_list.getSelectedIndex();
            if (selectedIndex != -1) {
                String bvid = bvids[selectedIndex];
                System.out.println("Selected BVID: " + bvid);
                new GetVideoInfoPage(ml, new VideoInfo(bvid,video_info.getPageNum(),video_info.getPageList())); // 创建新的页面以显示视频信息
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
		String rcmd_data=URLget.BackWeb(URLget.RCMD_URL);
		titles=FindString.extractContents(rcmd_data,"\"title\"");
		bvids=FindString.extractContents(rcmd_data,"\"bvid\"");
		rcmd_list=new List(lang_res.getValue("rcmd_list"),List.IMPLICIT);
		for(int i=0;i<maxVideosNum;i++){	//在列表内添加推荐视频的标题
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
