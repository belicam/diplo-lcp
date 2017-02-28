package sk.matfyz.lcp.api;

public interface Agent {

	public AgentId getName();
	public void sendMessage(Message msg);
	public EventSource<MessageReceivedEvent> getMessageReceivedSource();

}
