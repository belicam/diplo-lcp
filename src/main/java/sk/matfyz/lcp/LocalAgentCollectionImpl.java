package sk.matfyz.lcp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import sk.matfyz.lcp.api.Agent;
import sk.matfyz.lcp.api.AgentId;
import sk.matfyz.lcp.api.AgentInfo;
import sk.matfyz.lcp.api.DirectoryService;
import sk.matfyz.lcp.api.DiscoveryService;
import sk.matfyz.lcp.api.LocalAgentCollection;

/**
 *
 * @author shanki
 */
public class LocalAgentCollectionImpl implements LocalAgentCollection {

    private Map<AgentId, Agent> agents = new HashMap<AgentId, Agent>();
//    private DirectoryService directoryService = new DirectoryServiceImpl();
    private DiscoveryService discoveryService = new DiscoveryServiceImpl();

    public Set<AgentId> getRegisteredAgentIds() {
        return agents.keySet();
    }

    @Override
    public void register(Agent agent) {
        if (agent == null) {
            throw new NullPointerException("Agent cannot be null");
        }

        agents.put(agent.getName(), agent);
        notifyAdd(agent);
    }

    @Override
    public void deregister(Agent agent) {
        if (agent == null) {
            throw new NullPointerException("Agent cannot be null");
        }

        agents.remove(agent.getName());
        notifyRemove(agent);
    }

    @Override
    public Agent contains(AgentId id) {
        return agents.get(id);
    }

    private void notifyAdd(Agent agent) {
        if (discoveryService == null) {
            throw new NullPointerException("ds cannot be null");            
        }
        discoveryService.registerLocalAgent(agent);
    }

    private void notifyRemove(Agent agent) {
        if (discoveryService == null) {
            throw new NullPointerException("ds cannot be null");            
        }
        discoveryService.deregisterLocalAgent(agent);        
    }

}
