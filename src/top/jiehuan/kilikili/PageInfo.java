package top.jiehuan.kilikili;

import java.util.Vector;

import top.jiehuan.kilikili.Exception.PageInfoEmptyException;
import top.jiehuan.kilikili.Page.*;
public class PageInfo {
	private VideoInfo video_info;
	public short pageID;
	
	private MainMIDlet ml;
	private String BVID;
	private boolean isBVIDSet = false;
	private String search_keyword;
	private boolean isPageSet = false;
	private int page;
	private boolean isSearchSet = false;
	private String content;
	private boolean isContentSet = false;
	
	public PageInfo(short PageID, MainMIDlet ml){
		this.pageID = PageID;
		this.ml = ml;
		isBVIDSet = false;
		isSearchSet = false;
		isContentSet = false;
		isPageSet = false;
	}
	
	public MainMIDlet getMainMIDletObject(){
		return ml;
	}
	
	public void setVideoInfo(String bvid){
		isBVIDSet = true;
		BVID = bvid;
		video_info = new VideoInfo(bvid);
	}
	
	public void setVideoInfo(VideoInfo video_info){
		isBVIDSet = true;
		BVID = video_info.getBVID();
		this.video_info = video_info;
	}
	
	public VideoInfo getVideoInfo() throws PageInfoEmptyException{
		if(isBVIDSet)
			return video_info;
		else
			throw new PageInfoEmptyException();
	}
	
	public String getBVID() throws PageInfoEmptyException{
		if(isBVIDSet)
			return BVID;
		else
			throw new PageInfoEmptyException();
	}
	
	public void setSearchInfo(String keyword, int page){
		isSearchSet = true;
		this.page = page;
		search_keyword = keyword;
	}
	
	public void setPageInfo(int page){
		isPageSet = true;
		this.page = page;
	}
	
	public int getPageInfo() throws PageInfoEmptyException{
		if(isPageSet)
			return page;
		else
			throw new PageInfoEmptyException();
	}
	
	public String getSearchKeyword() throws PageInfoEmptyException{
		if(isSearchSet)
			return search_keyword;
		else
			throw new PageInfoEmptyException();
	}
	
	public int getSearchPage() throws PageInfoEmptyException{
		if(isSearchSet)
			return page;
		else
			throw new PageInfoEmptyException();
	}
	
	public void setContent(String content){
		isContentSet = true;
		this.content = content;
	}
	
	public String getContent() throws PageInfoEmptyException{
		if(isContentSet)
			return content;
		else
			throw new PageInfoEmptyException();
	}
	
	public void backMainPage(){
		new Thread(new Runnable() {
            public void run() {
            	new MainPage(ml);
            }
        }).start();
	}
	
	/*public void displayErrorAlert(String error, Form form){
		Alert alert = new Alert("Error", error, null, AlertType.INFO);
        alert.setTimeout(Alert.FOREVER); // 设置为永远显示，直到用户操作
        alert.setCommandListener(new CommandListener() {
            public void commandAction(Command c, Displayable d) {
                // 此处可以处理 Alert 的关闭事件
            	new Thread(new Runnable() {
                    public void run() {
                    	try {
							//TODO 增加Page父类 统一调用back()方法
                    	} catch (ConnectionNotFoundException e) {
							e.printStackTrace();
						}
                    	
                    }
	            }).start();
            }
        });
        Display.getDisplay(ml).setCurrent(alert, form);
	}*/
	
	/* page - 上一页的ID
	 * 返回逻辑请先在类中处理list
	 * 示例请见GetVideoInfoPage中的goLastPage()
	 * 新Page也请在此处添加case
	 * */
	public void back(short page, final Vector page_info_list){
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
	        
	        
	    default:
	        new Thread(new Runnable() {
	            public void run() {
	                backMainPage();
	            }
	        }).start();
	        break;
	}
	}
}
