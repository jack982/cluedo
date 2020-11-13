package gamedata;

import java.io.Serializable;

public class ChatMessage implements Serializable {
	

	public static final int CHAT_MESSAGE_TYPE = 0;
	public static final int SYSTEM_MESSAGE_TYPE = 1;
	
	private Player sender;
	private String message;
	
	public ChatMessage(Player sender, String message) {
		this.sender = sender;
		this.message = message;
	}
	
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Player getSender() {
		return sender;
	}
	public void setSender(Player sender) {
		this.sender = sender;
	}
	
		

}
