package top.jiehuan.kilikili.Model;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordStoreException;

import top.jiehuan.kilikili.Exception.ErrorVideoStatusException;
import top.jiehuan.kilikili.Exception.WebReturnErrorCodeException;
import top.jiehuan.kilikili.util.CookiesUtils;
import top.jiehuan.kilikili.util.FindString;
import top.jiehuan.kilikili.util.URLget;


public class VideoInfo {
	private String bvid;
	private String aid;
	private String dynamic_id;
	private int pn;
	private String cover_url;
	private String user_mid;
	private String user_name;
	private String title;
	private String pubtime;
	private String description;
	private boolean status = true;
	private String error_message;
	private String content;
	private String search_keyword;
	private PartInfo part_info;
	
	public boolean isLike = false;
	public boolean isCoin = false;
	
	private int like;
	private int view;
	private int reply;
	private int coin;
	private int share;
	private int favorite;
	private int videos;
	
	
	public VideoInfo(String bvid) throws Exception{
		this(bvid,1);
	}
	public VideoInfo(String bvid, int pn) throws Exception{
		this.bvid = bvid;
		this.pn = pn;
		initPartInfo(bvid, pn);
		getBasicVideoInfo();
	}

	private void initPartInfo(String bvid, int pn) throws Exception{
		try {
			String content = URLget.BackWeb(URLget.GET_VIDEOS_PAGE_LIST_URL+"bvid="+bvid);
			part_info = new PartInfo(bvid, pn, content);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			status = false;
			error_message = "initPartInfo"+e.getClass().toString()+" "+e.getMessage();
			throw e;
		} 
	}
	
	private void getBasicVideoInfo(){
		content = getVideoContent();
		//content = FindString.formatEscapeCharacters(content);
		if(!status){
			System.out.println("VideoInfo content is error");
		}else{
			System.gc();
		aid=FindString.findValueInt(content, "aid");
		user_mid=FindString.findValueInt(content, "mid");
		cover_url=FindString.findValue(content, "pic");
		title=FindString.formatEscapeCharacters(FindString.findValue(content,"title"));
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
		
		
		
		try {
			if(new CookiesUtils().isTokenStored()){
				initUserDataInVideo();
				getVideoDynamicAndInitVars();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			status = false;
		}
		System.out.println("getBasicVideoInfo successfully");
		}
	}
	
	public void initUserDataInVideo(){
		try{
			WebModel web = URLget.BackWebWithMoreInfo(URLget.GET_VIDEO_LIKE_STATUS_URL+"?bvid="+bvid);
			if(FindString.findValueInt(web.content, "data").equals("1")) isLike=true;
			System.out.println(web.content);
			
			web = URLget.BackWebWithMoreInfo(URLget.GET_VIDEO_COIN_STATUS_URL+"?bvid="+bvid);
			if(!FindString.findValueInt(web.content, "data").equals("0")) isCoin=true;
			System.out.println(web.content);
			web = null;
			status = true;
		}catch(Exception e){
			System.out.println(e.getMessage());
			error_message = "initUserDataInVideo"+e.getClass().toString()+" "+e.getMessage();
			status = false;
		}
	}
	
	public void postHeartbeat() throws InvalidRecordIDException, WebReturnErrorCodeException, IOException, RecordStoreException{
		URLget.BackWebAndUserCookiesPost(URLget.HEARTBEAT_URL, "aid="+aid+"&cid="+getCID());
	}
	
	public String getOnlineTotal() throws WebReturnErrorCodeException, IOException{
		String info = URLget.BackWebWithMoreInfo(URLget.GET_ONLINE_TOTAL_URL+"?bvid="+bvid+"&cid="+getCID()).content;
		String num = FindString.findValue(info, "total");
		info = null;
		return num;
	}
	
	public void setLikeStatus(boolean status){
		isLike = status;
	}
	
	public void setCoinStatus(boolean status){
		isCoin = status;
	}
	
	private void getVideoDynamicAndInitVars(){
		try{
			WebModel web = URLget.BackWebWithMoreInfo(URLget.GET_DYNAMIC_INFO_URL+"?rid="+aid+"&type=8");
			System.out.println(web.content);
			dynamic_id = FindString.findValueInt(web.content, "id_str");
			if(dynamic_id.startsWith("\""))
				dynamic_id = dynamic_id.substring(1, dynamic_id.length()-1);
			status = true;
		}catch(Exception e){
			System.out.println(e.getMessage());
			status = false;
			error_message = "getVideoDynamicAndInitVars"+e.getClass().toString()+" "+e.getMessage();
		}
	}
	
	public String getVideoContent(){
		try{
			String s_info = URLget.BackWeb(URLget.GET_INFO_URL+"bvid="+bvid);
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
			error_message = "getVideoContent"+e.getClass().toString()+" "+e.getMessage();
			return "error";
		}
	}
	
	public String getVideoURL() throws Exception{
		try{
			return URLget.BackVideoLink(bvid, ""+part_info.getCID());
		}catch(Exception e){
			System.out.println("BackVideoLink is error");
			status = false;
			error_message = "getVideoURL"+e.getClass().toString()+" "+e.getMessage();
			throw e;
		}
	}
	
	public int getVideoParts(){
		System.out.println("parts:"+videos);
		return videos;
	}
	
	public String getBVID(){
		return bvid;
	}
	
	public String getAID(){
		return aid;
	}
	
	public String getDynamicID(){
		return dynamic_id;
	}
	
	public void setBVID(String bvid) throws Exception{
		setBVID(bvid,1);
	}
	
	public void setBVID(String bvid, int pn) throws Exception{
		this.pn = pn;
		this.bvid = bvid;
		initPartInfo(bvid, pn);
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
		return FindString.formatDate(date,8);
	}
	
	public boolean getStatus(){
		return status;
		//return true;
	}
	
	public String getErrorMessage(){
		return error_message;
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
	
	
	
	

	
	
	// 作者 @8192Bit
	/*private static long power(int a, int b) {
	    long result = 1L;
	    for (int i = 0; i < b; i++)
	        result *= a;
	    return result;
	}*/

	// 修改自 https://www.zhihu.com/question/381784377/answer/1099438784
	// 作者 @8192Bit
	/*public static String avidToBvid(String avid) { //不带av两个字母
	    try {
	        String table = "fZodR9XQDSUm21yCkr6zBqiveYah8bt4xsWpHnJE7jL5VG3guMTKNPAwcF";

	        long xor = 177451812L;
	        long add = 8728348608L;
	        int[] s = { 11, 10, 3, 8, 4, 6 };

	        long av = Long.parseLong(avid);
	        av = (av ^ xor) + add;

	        char[] r = { 'B', 'V', '1', ' ', ' ', '4', ' ', '1', ' ', '7', ' ', ' ' };

	        for (int i = 0; i < 6; i++) {
	            r[s[i]] = table.charAt((int) (Math.floor(av / power(58, i)) % 58));
	        }

	        return String.valueOf(r);
	    } catch (NumberFormatException e) {
	        e.printStackTrace();
	        return "";
	    }
	}*/
	
	

	
}
