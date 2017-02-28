package sk.matfyz.lcp;

import sk.matfyz.lcp.api.Message;
import sk.matfyz.lcp.api.AgentId;

import java.util.Set;

public class AbstractMessage implements Message
{
	private static final long serialVersionUID = 2056886824795062605L;
	
	AgentId sender = null;
	Set<AgentId> rcpts = null;
	String content;

	public AbstractMessage()
	{
	}

	public AbstractMessage(AgentId sender, Set<AgentId> rcpts, String content)
	{
		setSender(sender);
		setRecepients(rcpts);
		setContent(content);
	}

	public AgentId getSender()
	{
		return sender;
	}

	public void setSender(AgentId sender)
	{
		this.sender = sender;
	}

	public Set<AgentId> getRecepients()
	{
		return rcpts;
	}

	public void addRecepient(AgentId rcpt)
	{
		rcpts.add(rcpt);
	}

	public void setRecepients(Set<AgentId> rcpts)
	{
		// TODO copy?
		this.rcpts = rcpts;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}
}
