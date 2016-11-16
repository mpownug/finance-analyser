package pl.pownug.marek.financeAnalyzer.helpers;

public class Message 
{
	String type = "primary";
	
	String content;
	
	String icon = "";
	
	public Message() { }
	
	public Message(String type, String content) {
		this.type = type;
		this.content = content;
	}
	
	public Message(String type, String content, String icon) {
		this.type = type;
		this.content = content;
		this.icon = icon;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
}
