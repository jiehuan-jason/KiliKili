package top.jiehuan.kilikili.Model;

import top.jiehuan.kilikili.Exception.WebReturnErrorCodeException;
import top.jiehuan.kilikili.util.FindString;
import top.jiehuan.kilikili.util.URLget;

public class FavFolderInfo {
	
	private String id;
	private String title;
	private String intro;
	private String media_count;
	private String collect;
	private boolean status = false;
	private String error_message;
	
	
	public FavFolderInfo(String id){
		setID(id);
	}
	
	private void getFavFolderBasicInfo(String id){
		try{
			WebModel content = URLget.BackWebWithMoreInfo(URLget.GET_FAV_FOLDER_INFO_URL+"?media_id="+id);
			if(URLget.getAPIBackCode(content.content)==0){
				status = true;
				title = FindString.findValue(content.content, "title");
				intro = FindString.findValue(content.content, "intro");
				media_count = FindString.findValueInt(content.content, "media_count");
				collect = FindString.findValueInt(content.content, "collect");
			}else
				throw new WebReturnErrorCodeException(URLget.getAPIBackCode(content.content), FindString.findValue(content.content, "message"));
		}catch(Exception e){
			status = false;
			error_message = e.getMessage();
		}
	}
	
	public void setID(String id){
		this.id = id;
		getFavFolderBasicInfo(id);
	}
	
	public String getID(){
		return id;
	}
	
	public String getTitle(){
		return title;
	}

	public String getIntro() {
		return intro;
	}

	public String getMediaCount() {
		return media_count;
	}

	public String getCollect() {
		return collect;
	}

	public boolean isStatus() {
		return status;
	}

	public String getErrorMessage() {
		if(!status)
			return error_message;
		else
			return "";
	}

}
