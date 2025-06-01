package top.jiehuan.kilikili.Model;

import top.jiehuan.kilikili.MainMIDlet;
import top.jiehuan.kilikili.Exception.PageInfoEmptyException;
public class PageInfo {
	private VideoInfo video_info;
	public short pageID;
	
	private MainMIDlet ml;
	private String BVID = "";
	private boolean isBVIDSet = false;
	private String search_keyword = "";
	private boolean isPageSet = false;
	private int page = -1;
	private boolean isSearchSet = false;
	private String content = "";
	private boolean isContentSet = false;
	private FavFolderInfo favFolderInfo;
	private boolean isFavFolderInfoSet = false;
	private int type = 0;
	private ReplyModel replyModel;
	private boolean isReplyModelSet = false;
	private String mid = "";
	private boolean isMIDSet = false;
	
	public PageInfo(short PageID, MainMIDlet ml){
		this.pageID = PageID;
		this.ml = ml;
		isBVIDSet = false;
		isSearchSet = false;
		isContentSet = false;
		isPageSet = false;
		isFavFolderInfoSet = false;
	}
	
	public MainMIDlet getMainMIDletObject(){
		return ml;
	}
	
	public void setFavFolderInfo(FavFolderInfo favFolderInfo){
		this.favFolderInfo = favFolderInfo;
		isFavFolderInfoSet = true;
	}
	
	public FavFolderInfo getFavFolderInfo() throws PageInfoEmptyException{
		if(isFavFolderInfoSet)
			return favFolderInfo;
		else
			throw new PageInfoEmptyException();
	}
	
	public void setVideoInfo(String bvid) throws Exception{
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
	
	public void setPage(int page){
		isPageSet = true;
		this.page = page;
	}
	
	public int getPage(){
		return page;
	}
	public boolean getIsPageSet(){
		return isPageSet;
	}
	
	public void setType(int type){
		this.type = type;
	}
	
	public int getType(){
		return type;
	}
	
	public String getSearchKeyword() throws PageInfoEmptyException{
		if(isSearchSet)
			return search_keyword;
		else
			throw new PageInfoEmptyException();
	}
	
	public void setContent(String content){
		isContentSet = true;
		this.content = content;
	}
	
	public void emptyContent(){
		isContentSet = false;
		this.content = "";
	}
	
	public String getContent() throws PageInfoEmptyException{
		if(isContentSet)
			return content;
		else
			throw new PageInfoEmptyException();
	}
	public boolean getIsContentSet(){
		return isContentSet;
	}

	public ReplyModel getReplyModel() throws PageInfoEmptyException {
		if(isReplyModelSet)
			return replyModel;
		else
			throw new PageInfoEmptyException();
	}

	public void setReplyModel(ReplyModel replyModel) {
		isReplyModelSet = true;
		this.replyModel = replyModel;
	}
	public void setMID(String mid){
		isMIDSet = true;
		this.mid = mid;
	}
	
	public String getMID() throws PageInfoEmptyException{
		if(isMIDSet)
			return mid;
		else
			throw new PageInfoEmptyException();
	}
	public boolean getIsMIDSet(){
		return isMIDSet;
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
							
                    	} catch (ConnectionNotFoundException e) {
							e.printStackTrace();
						}
                    	
                    }
	            }).start();
            }
        });
        Display.getDisplay(ml).setCurrent(alert, form);
	}*/
	

}
