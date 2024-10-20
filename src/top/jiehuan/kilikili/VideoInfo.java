package top.jiehuan.kilikili;

import java.util.Calendar;
import java.util.Date;


public class VideoInfo {
	private String bvid;
	private String cid;
	private String cover_url;
	private String user_mid;
	private String user_name;
	private String video_url;
	private String title;
	private String pubtime;
	private String description;
	private boolean status;
	private String content;
	private String search_keyword;
	
	private int like;
	private int view;
	private int reply;
	private int coin;
	private int share;
	private int favorite;
	
	private int page_num; //index of MainMIDlet.pagelist
	private String[] pagelist;
	
	public VideoInfo(String bvid,int page_num,String[] pagelist){
		this(bvid);
		this.page_num = page_num;
		this.pagelist = pagelist;
		
	}
	
	public VideoInfo(String bvid,int page_num,String search_keyword,String[] pagelist){
		this(bvid,page_num,pagelist);
		this.search_keyword = search_keyword;
	}
	
	public VideoInfo(int page_num,String[] pagelist){
		this.page_num = page_num;
		this.pagelist = pagelist;
	}
	
	public VideoInfo(int page_num,String[] pagelist,String search_keyword){
		this(page_num,pagelist);
		this.search_keyword = search_keyword;
	}
	
	public VideoInfo(String bvid){
		this.bvid = bvid;
		getBasicVideoInfo();
	}
	
	private void getBasicVideoInfo(){
		content = getVideoContent();
		user_mid=FindString.findValueInt(content, "mid");
		cover_url=FindString.findValue(content, "pic");
		title=FindString.findValue(content,"title");
		cid=FindString.findValueInt(content, "cid");
		user_name=FindString.findValue(content, "name");
		pubtime=FindString.findValueInt(content, "pubdate");
		description=FindString.findValueInt(content, "desc");
		
		like=Integer.parseInt(FindString.findValueInt(content,"like"));
		view=Integer.parseInt(FindString.findValueInt(content,"view"));
		reply=Integer.parseInt(FindString.findValueInt(content,"reply"));
		coin=Integer.parseInt(FindString.findValueInt(content,"coin"));
		share=Integer.parseInt(FindString.findValueInt(content,"share"));
		favorite = Integer.parseInt(FindString.findValueInt(content,"favorite"));
		
		video_url=URLget.BackVideoLink(bvid, cid);
	}
	
	public String getVideoContent(){
		String[] s_info= URLget.sendGetRequest(bvid);
		if(s_info[0].equals("ok")){
			status = true;
			return s_info[1];
		}
		status = false;
		return s_info[1];
	}
	
	public String getVideoURL(){
		return video_url;
	}
	
	public String getBVID(){
		return bvid;
	}
	
	public void setBVID(String bvid){
		this.bvid = bvid;
		getBasicVideoInfo();
	}
	
	public long getCID(){
		return Long.parseLong(cid);
	}
	
	public long getUserMID(){
		return Long.parseLong(user_mid);
	}
	
	public String getCoverURL(){
		return cover_url;
	}
	
	public String getTitle(){
		return title;
	}
	
	public String getUserName(){
		return user_name;
	}
	
	public long getPubTime(){
		return Long.parseLong(pubtime);
	}
	
	public String getFormatPubTime(){
		Date date = new Date(getPubTime()*1000);
		return formatDate(date,8);
	}
	
	public boolean getStatus(){
		return status;
	}
	
	public String getDescription(){
		return description.substring(1, description.length() - 1);
	}
	
	public int getView(){
		return view;
	}
	
	public int getLike(){
		return like;
	}
	
	public int getReply(){
		return reply;
	}
	
	public int getCoin(){
		return coin;
	}
	
	public int getShare(){
		return share;
	}
	
	public int getFavorite(){
		return favorite;
	}
	
	public int getPageNum(){
		return page_num;
	}
	
	public void setPageNum(int page_num){
		this.page_num = page_num;
	}
	
	public String getSearchKeyword(){
		return search_keyword;
	}
	
	public String[] getPageList(){
		return pagelist;
	}
	
	private String formatDate(Date date, long utcOffset) {
        // 获取 UTC 时间
        long utcTime = date.getTime() + (utcOffset * 3600 * 1000);

        // 创建一个新的 Date 对象，表示 UTC+8 的时间
        Date localDate = new Date(utcTime);
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(localDate);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        //int sec = calendar.get(Calendar.SECOND);
        
        if(min<10){
	        return year+"-"+month+"-"+day+" "+hour+":0"+min;
        }

        // 格式化为字符串
        return year+"-"+month+"-"+day+" "+hour+":"+min;
    }
	
}
