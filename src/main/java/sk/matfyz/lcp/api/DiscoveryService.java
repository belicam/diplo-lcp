package sk.matfyz.lcp.api;

public interface DiscoveryService {

	public void register(DirectoryService ds);
	public void deregister(DirectoryService ds);
        
        public void registerLocalAgent(Agent agent);
        public void deregisterLocalAgent(Agent agent);
        
        public void registerExternalAgent(AgentInfo agent);

        public void registerDiscovery(Discovery d);

}
