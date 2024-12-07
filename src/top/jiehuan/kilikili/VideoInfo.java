package top.jiehuan.kilikili;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import top.jiehuan.kilikili.Exception.ErrorVideoStatusException;
import top.jiehuan.kilikili.Exception.WebReturnErrorCodeException;
import top.jiehuan.kilikili.Page.AboutPage;
import top.jiehuan.kilikili.util.FindString;
import top.jiehuan.kilikili.util.URLget;


public class VideoInfo {
	private String bvid;
	private int pn;
	private String cover_url;
	private String user_mid;
	private String user_name;
	private String title;
	private String pubtime;
	private String description;
	private boolean status;
	private String content;
	private String search_keyword;
	private PartInfo part_info;
	
	private int like;
	private int view;
	private int reply;
	private int coin;
	private int share;
	private int favorite;
	private int videos;
	
	public VideoInfo(String bvid){
		this(bvid,1);
	}
	public VideoInfo(String bvid, int pn){
		this.bvid = bvid;
		this.pn = pn;
		initPartInfo(bvid, pn);
		getBasicVideoInfo();
	}
	
	private void initPartInfo(String bvid, int pn){
		try {
			String content = URLget.BackWeb(URLget.GET_VIDEOS_PAGE_LIST_URL+"bvid="+bvid);
			part_info = new PartInfo(bvid, pn, content);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getMessage());
			status = false;
		} 
	}
	
	private void getBasicVideoInfo(){
		content = getVideoContent();
		if(!status){
			System.out.println("VideoInfo content is error");
		}else{
		System.out.println("VideoInfo content:"+content);
		user_mid=FindString.findValueInt(content, "mid");
		cover_url=FindString.findValue(content, "pic");
		title=FindString.findValue(content,"title");
		//cid=FindString.findValueInt(content, "cid");
		user_name=FindString.findValue(content, "name");
		pubtime=FindString.findValueInt(content, "pubdate");
		description=FindString.findValueInt(content, "desc");
		//getCIDFromWeb();
		
		like=Integer.parseInt(FindString.findValueInt(content,"like"));
		view=Integer.parseInt(FindString.findValueInt(content,"view"));
		reply=Integer.parseInt(FindString.findValueInt(content,"reply"));
		coin=Integer.parseInt(FindString.findValueInt(content,"coin"));
		share=Integer.parseInt(FindString.findValueInt(content,"share"));
		favorite = Integer.parseInt(FindString.findValueInt(content,"favorite"));
		videos = Integer.parseInt(FindString.findValueInt(content, "videos"));
		System.out.println("getBasicVideoInfo successfully");
		}
	}
	
	public String getVideoContent(){
		try{
			String s_info = URLget.BackWeb(URLget.GET_INFO_URL+"bvid="+bvid+"&version="+AboutPage.version);
			System.out.println("VideoInfo:getVideoContent successfully");
			status = true;
			return s_info;
		}catch(ErrorVideoStatusException e1){
			System.out.println("VideoInfo:ErrorVideoStatusException");
			status = false;
			return "error api code:"+Integer.toString(e1.getCode());
		}
		catch(Exception e){
			status = false;
			return "error";
		}
	}
	
	public String getVideoURL() throws Exception{
		try{
			return URLget.BackVideoLink(bvid, ""+part_info.getCID());
		}catch(Exception e){
			System.out.println("BackVideoLink is error");
			status = false;
			throw e;
		}
	}
	
	public int getVideoParts(){
		return videos;
	}
	
	public String getBVID(){
		return bvid;
	}
	
	public void setBVID(String bvid){
		setBVID(bvid,1);
	}
	
	public void setBVID(String bvid, int pn){
		this.pn = pn;
		this.bvid = bvid;
		getBasicVideoInfo();
	}
	
	public long getCID(){
		return part_info.getCID();
	}
	
	public long getUserMID(){
		return Long.parseLong(user_mid);
	}
	
	public int getPart(){
		return pn;
	}
	
	public String getCoverURL(){
		return cover_url;
	}
	
	public String getTitle(){
		return title;
	}
	
	public String getPartTitle(){
		return part_info.getPartTitle();
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
		//return true;
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
	
	public String getSearchKeyword(){
		return search_keyword;
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
