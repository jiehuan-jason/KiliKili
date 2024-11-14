package top.jiehuan.kilikili;

import top.jiehuan.kilikili.Exception.PageInfoEmptyException;

public class PageInfo {
	private VideoInfo video_info;
	public short pageID;
	public static MainMIDlet ml;
	
	private String BVID;
	private boolean isBVIDSet;
	private String search_keyword;
	private int search_page;
	private boolean isSearchSet;
	private String content;
	private boolean isContentSet;
	
	public PageInfo(short PageID){
		this.pageID = PageID;
		isBVIDSet = false;
		isSearchSet = false;
		isContentSet = false;
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
		search_page = page;
		search_keyword = keyword;
	}
	
	public String getSearchKeyword() throws PageInfoEmptyException{
		if(isSearchSet)
			return search_keyword;
		else
			throw new PageInfoEmptyException();
	}
	
	public int getSearchPage() throws PageInfoEmptyException{
		if(isSearchSet)
			return search_page;
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
	
	
}
