package sk.matfyz.lcp.api;

/**
 *
 * Map<AgentId, Agent> ?????
 *
 */
public interface LocalAgentCollection
{
	public void register(Agent a);
	public void deregister(Agent a);
        
        public void register(DiscoveryService ds);
        public void deregister(DiscoveryService ds);

	public Agent contains(AgentId id); // contains?
}
