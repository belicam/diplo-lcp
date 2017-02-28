package sk.matfyz.lcp;

import java.util.HashSet;
import java.util.Set;

import sk.matfyz.lcp.api.AgentId;
import sk.matfyz.lcp.api.Message;

public class MessageImpl implements Message {

	private static final long serialVersionUID = -3730634148846537363L;
	
	private AgentId sender;
	private String content;
	private Set<AgentId> recepients = new HashSet<AgentId>();
	
	public MessageImpl(AgentId sender, String content) {
		setSender(sender);
		setContent(content);
	}
	
	@Override
	public final AgentId getSender() {
		return sender;
	}

	@Override
	public void setSender(AgentId sender) {
		if (sender == null) {
			throw new NullPointerException();
		}
		
		this.sender = sender;
	}

	@Override
	public final Set<AgentId> getRecepients() {
		return recepients;
	}

	@Override
	public final String getContent() {
		return content;
	}

	@Override
	public void setContent(String content) {
		if (content == null) {
			throw new NullPointerException();
		}
		
		this.content = content;
	}

}