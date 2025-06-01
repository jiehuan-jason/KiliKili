package top.jiehuan.kilikili.Page;

import java.util.Vector;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;

import javax.microedition.lcdui.StringItem;


import top.jiehuan.kilikili.Model.PageInfo;
import top.jiehuan.kilikili.Model.ReplyModel;


public class ReplyInfoPage extends Page{
	public static final short PageID = 15;
	
	Form form;
	String mid;
	ReplyModel reply;
	int count;
	
	StringItem author;
	StringItem content;
	StringItem repliesCount;
	Command authorInfo;
	Command repliesList;
	

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
		} catch (Exception e) {
			e.printStackTrace();
			displayErrorAlert(e.getMessage());
		} 
		
	}

	protected void initDisplayVars() {
		author=new StringItem(null,"@"+reply.getUname());
		content = new StringItem(null, "\n"+reply.getContent());
		repliesCount = new StringItem(null, "\n共有"+count+"条回复");
		
		initBackAndExitCommand();
		authorInfo=new Command(lang_res.getValue("authorInfo"),Command.ITEM,2);
		repliesList = new Command(lang_res.getValue("reply_list"),Command.ITEM,2);
	}

	protected void display() {
		form.append(author);
		form.append(content);
		if(count != 0)
			form.append(repliesCount);
		form.addCommand(back);
		if(count != 0)
			form.addCommand(repliesList);
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
        	
        }
	}
	
	public short getPageID() {
        return PageID;
 }
}
