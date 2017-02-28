package sk.matfyz.lcp.api;

/**
 *
 * @author shanki
 */
public class AgentAddedEvent implements Event {
	
	private AgentInfo agent;
	
	public AgentAddedEvent(AgentInfo agent) {
		this.agent = agent;
	}
	
	public AgentInfo getAgent() {
		return agent;
	}
}