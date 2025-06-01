package top.jiehuan.kilikili.Page;

import java.util.Vector;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;

import top.jiehuan.kilikili.Model.PageInfo;
import top.jiehuan.kilikili.Model.ReplyModel;
import top.jiehuan.kilikili.Model.WebModel;
import top.jiehuan.kilikili.util.CookiesUtils;
import top.jiehuan.kilikili.util.FindString;
import top.jiehuan.kilikili.util.URLget;


public class ReplyInfoPage extends Page{
	public static final short PageID = 15;
	
	Form form;
	String mid;
	ReplyModel reply;
	int count;
	String[] replyInLines;
	String csrf;
	
	StringItem author;
	StringItem content;
	StringItem repliesCount;
	Command authorInfo;
	Command repliesList;
	Command like;
	
	CookiesUtils cookiesUtils;
	

	public ReplyInfoPage(Vector page_info_list) {
		super(page_info_list);
		form = new Form(lang_res.getValue("reply"));
		initPageVars();
		initDisplayVars();
		display();
	}

	protected void initPageVars() {
		try {
			reply = page_info.getReplyModel();
			mid = reply.getMid();
			count = reply.getCount();
			cookiesUtils = new CookiesUtils();
			csrf = FindString.findValueInCookies(cookiesUtils.loadToken(), "bili_jct");
		} catch (Exception e) {
			e.printStackTrace();
			displayErrorAlert(e.getMessage());
		} 
		
	}

	protected void initDisplayVars() {
		author=new StringItem(null,"@"+reply.getUname());
		replyInLines = FindString.Display_Desc(reply.getContent());
		//content = new StringItem(null, "\n"+reply.getContent());
		repliesCount = new StringItem(null, "\n共有"+count+"条回复");
		
		initBackAndExitCommand();
		authorInfo=new Command(lang_res.getValue("authorInfo"),Command.ITEM,2);
		repliesList = new Command(lang_res.getValue("reply_list"),Command.ITEM,2);
		if(reply.isLike())
			like = new Command(lang_res.getValue("cancel")+lang_res.getValue("like"),Command.ITEM,2);
		else
			like = new Command(lang_res.getValue("like"),Command.ITEM,2);
	}

	protected void display() {
		form.append(author);
		//form.append(content);
		for(int i=0;i<replyInLines.length;i++)
			form.append(new StringItem(null,"\n"+replyInLines[i]));
		if(count != 0)
			form.append(repliesCount);
		form.addCommand(back);
		if(count != 0)
			form.addCommand(repliesList);
		if(cookiesUtils.isTokenStored())
			form.addCommand(like);
		form.addCommand(authorInfo);
		form.addCommand(exit);
		form.setCommandListener(this);
		display.setCurrent(form);
	}

	public void commandAction(Command c, Displayable d) {
		if (c == back) {
			
			goLastPage();
        }
        // 退出app
		else if(c==exit){
        	ml.exitApp();
        }else if (c == authorInfo) {
            new Thread(new Runnable() {
                public void run() {
                	PageInfo newpage = new PageInfo(UserInfoPage.PageID,ml);
                	try {
						newpage.setMID(mid);
						newpage.setType(1);
					} catch (Exception e) {
						displayErrorAlert(e.getMessage());
					}
                	page_info_list.addElement(newpage);
                	new UserInfoPage(page_info_list);
                }
            }).start();
        }else if(c==repliesList){
        	new Thread(new Runnable() {
                public void run() {
                	PageInfo newpage = new PageInfo(ReplyListPage.PageID,ml);
                	try {
						newpage.setReplyModel(reply);
						newpage.setVideoInfo(page_info.getVideoInfo());
						newpage.setType(1);
						newpage.setPage(1);
					} catch (Exception e) {
						displayErrorAlert(e.getMessage());
					}
                	page_info_list.addElement(newpage);
                	new ReplyListPage(page_info_list);
                }
            }).start();
        }else if(c==like){
        	try{
        		WebModel web = new WebModel();
        		if(reply.isLike()){
        			web = URLget.BackWebAndUserCookiesPost(URLget.LIKE_REPLY_URL, "type=1&oid="+reply.getOid()+"&rpid="+reply.getRpid()+"&action=0&csrf="+csrf);
        		}else{
        			web = URLget.BackWebAndUserCookiesPost(URLget.LIKE_REPLY_URL, "type=1&oid="+reply.getOid()+"&rpid="+reply.getRpid()+"&action=1&csrf="+csrf);
        		}
        		int code = URLget.getAPIBackCode(web.content);
        		if(code == 0){
        			reply.setLike(!reply.isLike());
        			form = new Form(lang_res.getValue("reply"));
        			initDisplayVars();
        			display();
        			displayInfoAlert("点赞/取消成功", form);
        		}
        		else
        			displayErrorAlertCanCancel("Error:"+code+" "+FindString.findValue(web.content, "message"),form);
        	}catch(Exception e){
        		displayErrorAlert(e.getMessage());
        		e.printStackTrace();
        	}
        }
	}
	
	public short getPageID() {
        return PageID;
 }
}
