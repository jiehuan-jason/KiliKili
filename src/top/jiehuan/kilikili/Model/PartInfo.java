package top.jiehuan.kilikili.Model;

import top.jiehuan.kilikili.util.FindString;

public class PartInfo {
	private String cid;
	private String part_title;
	private int page;
	private String bvid;
	
	public PartInfo(String bvid, int page, String content){
		this.bvid = bvid;
		this.page = page;
		getPartInfo(content);
	}
	
	private void getPartInfo(String content){
		String[] cids = FindString.extractContentsInt(content,"\"cid\"");
		String[] p_titles = FindString.extractContents(content,"\"part\"");
		cid = cids[page-1];
		part_title = p_titles[page-1];
	}
	
	public String getBVID(){
		return bvid;
	}
	public long getCID(){
		return Long.parseLong(cid);
	}
	public String getPartTitle(){
		return part_title;
	}
	public int getPage(){
		return page;
	}
	
}
