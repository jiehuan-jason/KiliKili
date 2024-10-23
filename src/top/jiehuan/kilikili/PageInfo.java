package top.jiehuan.kilikili;

public class PageInfo {
	private VideoInfo video_info;
	private static short[] Page_list = new short[100];
	private static int page_num = 0;
	private static MainMIDlet ml;
	
	public PageInfo(String bvid, short PageID){
		video_info = new VideoInfo(bvid);
		Page_list[page_num] = PageID;
		page_num++;
	}
	
	public PageInfo(short PageID){
		Page_list[page_num] = PageID;
		page_num++;
	}
	
	public PageInfo(MainMIDlet ml){
		this((short)0);
		this.ml = ml;
	}
	
	public PageInfo(MainMIDlet ml, String bvid){
		this(bvid, (short)0);
		this.ml = ml;
	}
	
	public void setNewBVID(String bvid){
		video_info.setBVID(bvid);
	}
	
	public void backToLastPage(){
		short pageID = getLastPageID();
		//TODO 返回逻辑由本函数处理
	}
	
	private short getLastPageID(){
		page_num -= 2;
		return Page_list[page_num];
	}
}
