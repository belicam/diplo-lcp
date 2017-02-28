package sk.matfyz.lcp.api;

public class MessageReceivedEvent implements Event {

	private final Message message;
	
	public MessageReceivedEvent(Message message) {
		this.message = message;
	}

	public final Message getMessage() {
		return message;
	}
	
}
