package sk.matfyz.lcp.api;

public interface DirectoryService {
	
	public EventSource<AgentAddedEvent> getAgentAddedSource();
	public EventSource<AgentRemovedEvent> getAgentRemovedSource();
	public EventSource<AgentChangedEvent> getAgentChangedSource();

	public void registerDS(DiscoveryService ds);
	public void deregisterDS(DiscoveryService ds);

	public AgentInfo lookup(AgentId agentId);
	
	public void updateAgent(AgentInfo agent);
	public void removeAgent(AgentId agentId);
	
}
