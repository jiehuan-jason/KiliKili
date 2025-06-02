package top.jiehuan.kilikili.Page;

import java.io.IOException;
import java.util.Vector;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.List;

import top.jiehuan.kilikili.MainMIDlet;
import top.jiehuan.kilikili.Model.PageInfo;
import top.jiehuan.kilikili.Model.VideoInfo;
import top.jiehuan.kilikili.util.GetLangRes;

abstract public class Page implements CommandListener{
    MainMIDlet ml;
	GetLangRes lang_res;
	Display display;
	
	PageInfo page_info;
	Vector page_info_list;
	VideoInfo video_info;
	
	Command back;
	Command exit;
	
	public Page(MainMIDlet ml){
		this.ml=ml;
		display = Display.getDisplay(ml);
		
		loadMessages();
	}
	
	public Page(Vector page_info_list){
		page_info = (PageInfo) page_info_list.lastElement();
		ml=page_info.getMainMIDletObject();
		display = Display.getDisplay(ml);
		this.page_info_list = page_info_list;
		
		loadMessages();
	}
	protected void displayErrorAlert(String error){
		 Alert alert = new Alert("Error", error, null, AlertType.ERROR);
	     alert.setTimeout(Alert.FOREVER); // 设置为永远显示，直到用户操作
	     back=new Command(lang_res.getValue("back"),Command.BACK,1);
	     alert.addCommand(back);
	     alert.setCommandListener(new CommandListener() {
	    	    public void commandAction(Command c, Displayable d) {
	    	        goLastPage();
	    	    }
	    	});
	     display.setCurrent(alert);	
	 }
	
	protected void displayErrorAlertCanCancel(String error, final Form form){
		 Alert alert = new Alert("Error", error, null, AlertType.ERROR);
	     alert.setTimeout(Alert.FOREVER); // 设置为永远显示，直到用户操作
	     Command ok=new Command("OK",Command.OK,1);
	     alert.addCommand(ok);
	     alert.setCommandListener(new CommandListener() {
	    	    public void commandAction(Command c, Displayable d) {
	    	    	display.setCurrent(form);	
	    	    }
	    	});
	     display.setCurrent(alert);	
	 }
	
	protected void displayErrorAlertCanCancel(String error, final List list){
		 Alert alert = new Alert("Error", error, null, AlertType.ERROR);
	     alert.setTimeout(Alert.FOREVER); // 设置为永远显示，直到用户操作
	     Command ok=new Command("OK",Command.OK,1);
	     alert.addCommand(ok);
	     alert.setCommandListener(new CommandListener() {
	    	    public void commandAction(Command c, Displayable d) {
	    	    	display.setCurrent(list);	
	    	    }
	    	});
	     display.setCurrent(alert);	
	 }
	
	protected void displayInfoAlert(String info, final Form form){
		 Alert alert = new Alert("Info", info, null, AlertType.INFO);
	     alert.setTimeout(Alert.FOREVER); // 设置为永远显示，直到用户操作
	     Command ok=new Command("OK",Command.OK,1);
	     alert.addCommand(ok);
	     alert.setCommandListener(new CommandListener() {
	    	    public void commandAction(Command c, Displayable d) {
	    	    	display.setCurrent(form);	
	    	    }
	    	});
	     display.setCurrent(alert);	
	 }
	
	protected void displayInfoAlert(String info, final List list){
		 Alert alert = new Alert("Info", info, null, AlertType.INFO);
	     alert.setTimeout(Alert.FOREVER); // 设置为永远显示，直到用户操作
	     Command ok=new Command("OK",Command.OK,1);
	     alert.addCommand(ok);
	     alert.setCommandListener(new CommandListener() {
	    	    public void commandAction(Command c, Displayable d) {
	    	    	display.setCurrent(list);	
	    	    }
	    	});
	     display.setCurrent(alert);	
	 }
	abstract protected void initPageVars();
	abstract protected void initDisplayVars();
	abstract protected void display();
	public abstract short getPageID();
	
	public abstract void commandAction(Command c, Displayable d);
	
	protected void loadMessages() {
	        // 根据系统语言加载相应的资源文件
	        try {
				lang_res = new GetLangRes(System.getProperty("microedition.locale"));
				//System.out.println(lang_res.getLangFileContent());
				System.out.println("GetVideoInfoPage:Get lang_res OK");
			} catch (IOException e) {
				e.printStackTrace();
			}
     }
	
	public void goLastPage(){
		if(page_info_list.size()==1)
			backMainPage();
		else{
			page_info_list.removeElementAt(page_info_list.size()-1);
			PageInfo last_page = (PageInfo) page_info_list.lastElement();
			System.out.println("call goLastPage.Page now is:"+last_page.pageID);
		
			short page = last_page.pageID;
			back(page, page_info_list);
		}
	}
	
	protected void initBackAndExitCommand(){
		back=new Command(lang_res.getValue("back"),Command.BACK,1);
		exit=new Command(lang_res.getValue("exit"),Command.EXIT,0);
	}
	
	/* page - 上一页的ID
	 * 新Page请在此处添加case
	 * */
	private void back(short page, final Vector page_info_list){
		switch (page) {
	    case RecommendPage.PageID:
	        new Thread(new Runnable() {
	            public void run() {
	                new RecommendPage(page_info_list);
	            }
	        }).start();
	        break;

	    case SearchPage.PageID:
	        new Thread(new Runnable() {
	            public void run() {
	                new SearchPage(page_info_list);
	            }
	        }).start();
	        break;

	    case UserVideoListPage.PageID:
	        new Thread(new Runnable() {
	            public void run() {
	                new UserVideoListPage(page_info_list);
	            }
	        }).start();
	        break;

	    case PartVideoListPage.PageID:
	        new Thread(new Runnable() {
	            public void run() {
	                new PartVideoListPage(page_info_list);
	            }
	        }).start();
	        break;
	        
	    case GetVideoInfoPage.PageID:
	        new Thread(new Runnable() {
	            public void run() {
	                new GetVideoInfoPage(page_info_list);
	            }
	        }).start();
	        break;
	        
	    case AboutPage.PageID:
	        new Thread(new Runnable() {
	            public void run() {
	                new AboutPage(page_info_list);
	            }
	        }).start();
	        break;
	        
	    case DownloadPage.PageID:
	        new Thread(new Runnable() {
	            public void run() {
	                new DownloadPage(page_info_list);
	            }
	        }).start();
	        break;
	        
	    case UserInfoPage.PageID:
	        new Thread(new Runnable() {
	            public void run() {
	                new UserInfoPage(page_info_list);
	            }
	        }).start();
	        break;
	        
	    case FavFolderListPage.PageID:
	    	new Thread(new Runnable() {
	            public void run() {
	                new FavFolderListPage(page_info_list);
	            }
	        }).start();
	        break;
	    
	    case MyInfoPage.PageID:
	    	new Thread(new Runnable() {
	            public void run() {
	                new MyInfoPage(page_info_list);
	            }
	        }).start();
	        break;
	    
	    case FavListPage.PageID:
	    	new Thread(new Runnable() {
	    		public void run() {
	    			new FavListPage(page_info_list);
	    		}
	    	}).start();
	    	break;
        
	    case ReplyListPage.PageID:
	    	new Thread(new Runnable() {
	            public void run() {
	                new ReplyListPage(page_info_list);
	            }
	        }).start();
	        break;
	    
	    case ReplyInfoPage.PageID:
	    	new Thread(new Runnable() {
	            public void run() {
	                new ReplyInfoPage(page_info_list);
	            }
	        }).start();
	        break;
	        
	        
	    default:
	        new Thread(new Runnable() {
	            public void run() {
	                backMainPage();
	            }
	        }).start();
	        break;
	}
	}
	public void back(final Vector page_info_list){
		PageInfo page = (PageInfo) page_info_list.lastElement();
		back(page.pageID, page_info_list);
	}
	public void backMainPage(){
		new Thread(new Runnable() {
            public void run() {
            	new MainPage(ml);
            }
        }).start();
	}
	
	public void goToVideoListPage(String bvid){
     	try {
     		VideoInfo newVideo = new VideoInfo(bvid);
     		int parts = newVideo.getVideoParts();
			//newpage.setVideoInfo(newVideo);
     		PageInfo newpage;
         	if(parts == 1)
         		newpage = new PageInfo(GetVideoInfoPage.PageID,ml);
         	else
         		newpage = new PageInfo(PartVideoListPage.PageID,ml);
         	newpage.setVideoInfo(newVideo);
         	page_info_list.addElement(newpage);
         	System.out.println("SearchPage call GetVideoInfoPage/PartVideoListPage");
         	if(parts == 1)
         		new GetVideoInfoPage(page_info_list);
         	else
         		new PartVideoListPage(page_info_list);
		} catch (Exception e) {
			e.printStackTrace();
			displayErrorAlert(e.getMessage());
		}
     	
	 }
}
