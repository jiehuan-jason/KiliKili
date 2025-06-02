package top.jiehuan.kilikili.Page;

import java.util.Vector;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;

public class SettingPage extends Page {
	public static final short PageID = 13;
	
	Form form;
	Command save;
	TextField kiliServerAddressInput;
	
	String kiliServerAddress;
	String bvid;
	String cid;
	
	
	public SettingPage(Vector pageInfoList){
		super(pageInfoList);
	}

	protected void initPageVars() {
		// TODO Auto-generated method stub

	}

	protected void initDisplayVars() {
		// TODO Auto-generated method stub

	}

	protected void display() {
		initBackAndExitCommand();
		
	}

	public short getPageID() {
		return PageID;
	}

	public void commandAction(Command c, Displayable d) {
		

	}

}
