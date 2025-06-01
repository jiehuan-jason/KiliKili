package top.jiehuan.kilikili.Model;

public class ReplyModel {
	private String oid;
	private String root;
	private String rpid;
	private String content;
	private String uname;
	private String mid;
	private int count;
	
	public ReplyModel(String oid, String root, String rpid, String content, String uname, String mid, int count){
		this.setOid(oid);
		this.setRoot(root);
		this.setRpid(rpid);
		this.setContent(content);
		this.setUname(uname);
		this.setMid(mid);
		this.setCount(count);
	}
	public ReplyModel() {
		
	}
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	public String getRoot() {
		return root;
	}
	public void setRoot(String root) {
		this.root = root;
	}
	public String getRpid() {
		return rpid;
	}
	public void setRpid(String rpid) {
		this.rpid = rpid;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}

}
