package sk.matfyz.lcp.api;

import java.io.Serializable;
import java.util.Set;

public interface Message extends Serializable {

	public AgentId getSender();
	public void setSender(AgentId sender);
	public Set<AgentId> getRecepients();
	public String getContent();
	public void setContent(String content);
	
}
