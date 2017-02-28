package sk.matfyz.lcp.api;

/**
 *
 * @author shanki
 */
public class AgentRemovedEvent implements Event {
	
	private AgentId agentId;
	
	public AgentRemovedEvent(AgentId agentId) {
		this.agentId = agentId;
	}
	
	public AgentId getAgentId() {
		return agentId;
	}

}