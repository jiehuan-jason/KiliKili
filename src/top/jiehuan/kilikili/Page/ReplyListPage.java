package top.jiehuan.kilikili.Page;

import java.io.IOException;
import java.util.Vector;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

import top.jiehuan.kilikili.Exception.ErrorVideoStatusException;
import top.jiehuan.kilikili.Exception.PageInfoEmptyException;
import top.jiehuan.kilikili.Exception.WebReturnErrorCodeException;
import top.jiehuan.kilikili.Model.FavFolderInfo;
import top.jiehuan.kilikili.Model.PageInfo;
import top.jiehuan.kilikili.Model.ReplyModel;
import top.jiehuan.kilikili.Model.VideoInfo;
import top.jiehuan.kilikili.Model.WebModel;
import top.jiehuan.kilikili.util.FindString;
import top.jiehuan.kilikili.util.URLget;

public class ReplyListPage extends Page {
	
	public static final short PageID = 14;
	public static final int REPLIES_NUM = 10;
	
	List replyList;
	Command go;
	Command last_page;
	Command next_page;
	
	private short page_num;
	private int repliesCount;
	private Vector replys; //ReplysList
	private boolean nextPageStatus;
	private int type;
	private ReplyModel reply;
	
	public ReplyListPage(Vector page_info_list){
		super(page_info_list);
		replyList=new List(lang_res.getValue("reply_list"),List.IMPLICIT);
		type = page_info.getType();
		
		initPageVars();
		initDisplayVars();
		display();
	}
	
	public short getPageID() {
        return PageID;
 }
	
	public void commandAction(Command c, Displayable d) {
        if (c == back) {
            new Thread(new Runnable() {
                public void run() {
                	page_info_list.removeElementAt(page_info_list.size()-1);
                	back(page_info_list);
                }
            }).start();
        }
        else if(c==exit){
        	ml.exitApp();
        }else if(c==go){
        	new Thread(new Runnable() {
                public void run() {
                	initInfoPage();
                }
            }).start();
        }else if(c == last_page){
        	page_num--;
        	page_info.setPage(page_num);
        	try {
        		refreshPage(page_num);
				display();
			} catch (Exception e) {
				e.printStackTrace();
				displayErrorAlert(e.getMessage());
			} 
        }else if(c == next_page){
        	page_num++;
        	page_info.setPage(page_num);
        	try {
				refreshPage(page_num);
				display();
			} catch (Exception e) {
				e.printStackTrace();
				displayErrorAlert(e.getMessage());
			} 
        }else if (d == replyList) {
            // 检查是否是通过选择列表项触发的 OK 键
            int selectedIndex = replyList.getSelectedIndex();
            if (selectedIndex != -1) {
            	new Thread(new Runnable() {
                    public void run() {
                    	initInfoPage();
                    }
                }).start();
            }else{
            	displayErrorAlert("未选择！");
            }
        }
    }
	
	private void initInfoPage(){
		
		ReplyModel reply = (ReplyModel) replys.elementAt(replyList.getSelectedIndex());
    	try {
    		goToReplyInfoPage(reply);
		} catch (Exception e) {
			e.printStackTrace();
			displayErrorAlert(reply.getRpid()+" "+e.getClass().toString()+" "+e.getMessage());
		}
		
	}
	
	private void goToReplyInfoPage(ReplyModel reply){
     	PageInfo newpage = new PageInfo(ReplyInfoPage.PageID,ml);
     	newpage.setReplyModel(reply);
     	newpage.setMID(reply.getMid());
     	newpage.setVideoInfo(video_info);
     	page_info_list.addElement(newpage);
     	System.out.println("ReplyListPage call ReplyInfoPage");
        new ReplyInfoPage(page_info_list);
	 }
	
	private String[] deleteUselessMid(String[] source){
		String[] results = new String[100];
		int j = 0;
		int i = 0;
		while(source[i+1]!=null&&(i+1)<source.length){
			if(source[i+1].startsWith("\""+source[i])){
				results[j] = source[i];
				j++;
				i+=3;
			}else i++;
		}
		return results;
	}
	
	
	private void refreshPage(short page) throws WebReturnErrorCodeException, IOException, ErrorVideoStatusException, PageInfoEmptyException{
		replyList=new List(lang_res.getValue("reply_list"),List.IMPLICIT);
		
		WebModel jsonData;
		if(type == 0){
			jsonData = URLget.BackWebWithMoreInfo(URLget.GET_REPLY_LIST_URL+"?type=1&oid="+video_info.getAID()+"&sort=1&ps="+REPLIES_NUM+"&pn="+page);
		}else{
			reply = page_info.getReplyModel();
			jsonData = URLget.BackWebWithMoreInfo(URLget.GET_SUB_REPLY_LIST_URL+"?type=1&oid="+video_info.getAID()+"&root="+reply.getRpid()+"&ps="+REPLIES_NUM+"&pn="+page);
		}
		String jsonContent = FindString.extractArraysAndDelete(jsonData.content,"\"members\"");
		jsonData = null;
		
		System.out.println("jsonContent is:"+jsonContent);
		String[] rpidsAll = FindString.extractContents(jsonContent,"\"rpid_str\"");
		String[] oidsAll = FindString.extractContents(jsonContent,"\"oid_str\"");
		String[] rootsAll = FindString.extractContents(jsonContent,"\"root_str\"");
		String[] contentsAll = FindString.extractContents(jsonContent,"\"message\"");
		String[] unamesAll = FindString.extractContents(jsonContent,"\"uname\"");
		String[] midsAll = deleteUselessMid(FindString.extractContentsInt(jsonContent,"\"mid\""));
		String[] countsAll = FindString.extractContentsInt(jsonContent, "\"count\"");
		String[] actionsAll = FindString.extractContentsInt(jsonContent, "\"action\"");
		
		repliesCount = Integer.parseInt(countsAll[0]);
		
		replys = new Vector();
		
		int j = 0;
		System.out.println(rootsAll.length);
		if(type == 0){
			for(int i=0;i<rootsAll.length;i++){	
				if(rootsAll[i]!=null){
					System.out.println(rootsAll[i]+" "+rpidsAll[i]+" @"+unamesAll[i]+" "+midsAll[i]+": "+contentsAll[i+1]);
					if(rootsAll[i].equals("0")){
						if(actionsAll[i].equals("1"))
							replys.addElement(new ReplyModel(oidsAll[i], rootsAll[i], rpidsAll[i], contentsAll[i+1], unamesAll[i], midsAll[i], Integer.parseInt(countsAll[i+1]),true));
						else
							replys.addElement(new ReplyModel(oidsAll[i], rootsAll[i], rpidsAll[i], contentsAll[i+1], unamesAll[i], midsAll[i], Integer.parseInt(countsAll[i+1])));
						replyList.append("@"+unamesAll[i]+": "+contentsAll[i+1], null);
						j++;
					}
				}else break;
			}
		}else{
			for(int i=0;i<rootsAll.length;i++){	
				if(rootsAll[i]!=null){
					System.out.println(rootsAll[i]+" "+rpidsAll[i]+" @"+unamesAll[i]+" "+midsAll[i]+": "+contentsAll[i+1]);
					if(!rootsAll[i].equals("0")){
						if(actionsAll[i].equals("1"))
							replys.addElement(new ReplyModel(oidsAll[i], rootsAll[i], rpidsAll[i], contentsAll[i+1], unamesAll[i], midsAll[i], Integer.parseInt(countsAll[i+1]),true));
						else
							replys.addElement(new ReplyModel(oidsAll[i], rootsAll[i], rpidsAll[i], contentsAll[i+1], unamesAll[i], midsAll[i], Integer.parseInt(countsAll[i+1])));
						replyList.append("@"+unamesAll[i]+": "+contentsAll[i+1], null);
						j++;
					}
				}else break;
			}
		}
		if(j<REPLIES_NUM)
			nextPageStatus = false;
		
		rpidsAll = null;
		oidsAll = null;
		rootsAll = null;
		contentsAll = null;
		unamesAll = null;
			
			
	}
	
	protected void initPageVars(){
		page_num = 1;
		nextPageStatus = true;
		repliesCount = 0;
		
		
		try {
			video_info = page_info.getVideoInfo();
			refreshPage(page_num);
		} catch (Exception e) {
			displayErrorAlert("ReplyListPage initPageVars Error:"+e.getMessage());
			System.out.println(e.getMessage());
			e.printStackTrace();
		} 
	}
	
	protected void initDisplayVars(){
		System.out.println("start to initDisplayVars");
		initBackAndExitCommand();
		go = new Command(lang_res.getValue("go"),Command.OK,1);
		last_page=new Command(lang_res.getValue("last_page"),Command.OK,2);
		next_page=new Command(lang_res.getValue("next_page"),Command.OK,2);
	}
	protected void display(){
		System.out.println("start to display");
		replyList.addCommand(back);
		replyList.addCommand(go);
		replyList.addCommand(exit);
		checkVideoPage();
		
		replyList.setSelectCommand(go);
		replyList.setCommandListener(this);
		display.setCurrent(replyList);
	}
	
	private void checkVideoPage(){
		if(!(page_num==1))
			replyList.addCommand(last_page);
		if(nextPageStatus){
			replyList.addCommand(next_page);
		}
		
	}

}
