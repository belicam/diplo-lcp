package sk.matfyz.lcp.api;

/**
 *
 * Map<AgentId, Agent> ?????
 *
 */
public interface LocalAgentCollection extends DiscoveryService
{
	public void register(Agent a);
	public void deregister(Agent a);

	public Agent isLocal(AgentId id); // contains?
}
