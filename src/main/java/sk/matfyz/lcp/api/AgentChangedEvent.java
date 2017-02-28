package sk.matfyz.lcp.api;

/**
 *
 * @author shanki
 */
public class AgentChangedEvent implements Event {
	
	private AgentInfo agent;
	
	public AgentChangedEvent(AgentInfo agent) {
		this.agent = agent;
	}
	
	public AgentInfo getAgent() {
		return agent;
	}
}
