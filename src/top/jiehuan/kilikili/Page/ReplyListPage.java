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
	public static final int REPLIES_NUM = 1;
	
	List replyList;
	Command go;
	Command last_page;
	Command next_page;
	Command displayTest;
	
	private short page_num;
	private int repliesCount;
	private Vector replys; //ReplysList
	private boolean nextPageStatus;
	private int type;
	private ReplyModel reply;
	String testContent = "";
	
	public ReplyListPage(Vector page_info_list){
		super(page_info_list);
		replyList=new List(lang_res.getValue("reply_list"),List.IMPLICIT);
		type = page_info.getType();
		System.out.println("page:"+page_info.getPage());
		
		
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
        	nextPageStatus = true;
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
        }else if(c == displayTest){
        	displayInfoAlert(testContent,replyList);
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
		
		//ReplyModel reply = (ReplyModel) replys.elementAt(replyList.getSelectedIndex());
		ReplyModel reply = (ReplyModel) replys.elementAt(0);
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
	
	private Vector deleteUselessMid(Vector source){
		Vector results = new Vector();
		int i = 0;
		
		while((i+1)<source.size()){
			String start = "\""+ (String) source.elementAt(i);
			String next = (String) source.elementAt(i+1);
			if(next.startsWith(start)){
				results.addElement(source.elementAt(i));
				i+=3;
			}else i++;
		}
		return results;
	}
	
	
	private void refreshPage(short page) throws WebReturnErrorCodeException, IOException, ErrorVideoStatusException, PageInfoEmptyException{
		
		
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
		Vector rpidsAll = FindString.extractContentsVector(jsonContent,"\"rpid_str\"");
		//Vector oidsAll = FindString.extractContentsVector(jsonContent,"\"oid_str\"");
		Vector rootsAll = FindString.extractContentsVector(jsonContent,"\"root_str\"");
		Vector contentsAll = FindString.extractContentsVector(jsonContent,"\"message\"");
		Vector unamesAll = FindString.extractContentsVector(jsonContent,"\"uname\"");
		Vector midsAll = deleteUselessMid(FindString.extractContentsIntVector(jsonContent,"\"mid\""));
		Vector countsAll = FindString.extractContentsIntVector(jsonContent, "\"count\"");
		Vector actionsAll = FindString.extractContentsIntVector(jsonContent, "\"action\"");
		
		System.out.println("free memory is:"+Runtime.getRuntime().freeMemory());
		System.gc();
		System.out.println("free memory is:"+Runtime.getRuntime().freeMemory());
		
		if(rootsAll.size()==0){
			displayErrorAlertCanCancel("已经是最后一页了",replyList);
			page_num--;
        	page_info.setPage(page_num);
			nextPageStatus = false;
			replyList.removeCommand(next_page);
			display();
		}else{
		
		repliesCount = Integer.parseInt((String) countsAll.firstElement());
		replyList=new List(lang_res.getValue("reply_list"),List.IMPLICIT);
		
		replys = new Vector();
		
		
		System.out.println(rootsAll.size());
		/*if(type == 0){
			for(int i=0;i<rootsAll.size();i++){	
					//testContent+=rootsAll.elementAt(i)+" "+rpidsAll.elementAt(i)+" @"+unamesAll.elementAt(i)+" "+midsAll.elementAt(i)+": "+contentsAll.elementAt(i+1)+"\n";
					System.out.println(rootsAll.elementAt(i)+" "+rpidsAll.elementAt(i)+" @"+unamesAll.elementAt(i)+" "+midsAll.elementAt(i)+": "+contentsAll.elementAt(i+1));
					if(rootsAll.elementAt(i).equals("0")){
						if(actionsAll.elementAt(i).equals("1"))
							replys.addElement(new ReplyModel(video_info.getAID(), (String) rootsAll.elementAt(i), (String) rpidsAll.elementAt(i), (String) contentsAll.elementAt(i+1), (String) unamesAll.elementAt(i), (String) midsAll.elementAt(i), Integer.parseInt((String) countsAll.elementAt(i+1)),true));
						else
							replys.addElement(new ReplyModel(video_info.getAID(), (String) rootsAll.elementAt(i), (String) rpidsAll.elementAt(i), (String) contentsAll.elementAt(i+1), (String) unamesAll.elementAt(i), (String) midsAll.elementAt(i), Integer.parseInt((String) countsAll.elementAt(i+1))));
						replyList.append("@"+unamesAll.elementAt(i)+": "+contentsAll.elementAt(i+1), null);
						j++;
					}
			}
		}else{
			for(int i=0;i<rootsAll.size();i++){	
				System.out.println(rootsAll.elementAt(i)+" "+rpidsAll.elementAt(i)+" @"+unamesAll.elementAt(i)+" "+midsAll.elementAt(i)+": "+contentsAll.elementAt(i+1));
					if(!rootsAll.elementAt(i).equals("0")){
						if(actionsAll.elementAt(i).equals("1"))
							replys.addElement(new ReplyModel(video_info.getAID(), (String) rootsAll.elementAt(i), (String) rpidsAll.elementAt(i), (String) contentsAll.elementAt(i+1), (String) unamesAll.elementAt(i), (String) midsAll.elementAt(i), Integer.parseInt((String) countsAll.elementAt(i+1)),true));
						else
							replys.addElement(new ReplyModel(video_info.getAID(), (String) rootsAll.elementAt(i), (String) rpidsAll.elementAt(i), (String) contentsAll.elementAt(i+1), (String) unamesAll.elementAt(i), (String) midsAll.elementAt(i), Integer.parseInt((String) countsAll.elementAt(i+1))));
						replyList.append("@"+unamesAll.elementAt(i)+": "+contentsAll.elementAt(i+1), null);
						j++;
				}
			}
		}*/
		//if(type == 0){
			if(actionsAll.elementAt(0).equals("1"))
				replys.addElement(new ReplyModel(video_info.getAID(), (String) rootsAll.elementAt(0), (String) rpidsAll.elementAt(0), (String) contentsAll.elementAt(1), (String) unamesAll.elementAt(0), (String) midsAll.elementAt(0), Integer.parseInt((String) countsAll.elementAt(1)),true));
			else
				replys.addElement(new ReplyModel(video_info.getAID(), (String) rootsAll.elementAt(0), (String) rpidsAll.elementAt(0), (String) contentsAll.elementAt(1), (String) unamesAll.elementAt(0), (String) midsAll.elementAt(0), Integer.parseInt((String) countsAll.elementAt(1))));
			replyList.append("@"+unamesAll.elementAt(0)+": ", null);
			replyList.append((String) contentsAll.elementAt(1), null);
		/*}else{
			if(actionsAll.elementAt(0).equals("1"))
				replys.addElement(new ReplyModel(video_info.getAID(), (String) rootsAll.elementAt(0), (String) rpidsAll.elementAt(0), (String) contentsAll.elementAt(1), (String) unamesAll.elementAt(0), (String) midsAll.elementAt(0), Integer.parseInt((String) countsAll.elementAt(1)),true));
			else
				replys.addElement(new ReplyModel(video_info.getAID(), (String) rootsAll.elementAt(0), (String) rpidsAll.elementAt(0), (String) contentsAll.elementAt(1), (String) unamesAll.elementAt(0), (String) midsAll.elementAt(0), Integer.parseInt((String) countsAll.elementAt(1))));
			replyList.append("@"+unamesAll.elementAt(0)+": "+contentsAll.elementAt(1), null);
		}*/
		if(page == repliesCount)
			nextPageStatus = false;
		}
		rpidsAll = null;
		rootsAll = null;
		contentsAll = null;
		unamesAll = null;
		//replyList.append(testContent, null);
		
	}
	
	protected void initPageVars(){
		
		nextPageStatus = true;
		repliesCount = 0;
		
		
		try {
			page_num = (short) page_info.getPage();
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
		next_page=new Command(lang_res.getValue("next_page"),Command.BACK,0);
		displayTest = new Command("content",Command.OK,2);
	}
	protected void display(){
		System.out.println("start to display");
		checkVideoPage();
		replyList.addCommand(back);
		replyList.addCommand(go);
		replyList.addCommand(exit);
		//replyList.addCommand(displayTest);
		
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
