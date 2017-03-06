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
import sk.matfyz.lcp.api.LocalAgentCollection;

/**
 *
 * @author shanki
 */
public class LocalAgentCollectionImpl implements LocalAgentCollection {

	private Map<AgentId, Agent> agents = new HashMap<AgentId, Agent>();
	private Collection<DirectoryService> services = new ArrayList<DirectoryService>();
	
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
	public Agent isLocal(AgentId id) {
		return agents.get(id);
	}

	@Override
	public void register(DirectoryService ds) {
		if (ds == null) {
			throw new NullPointerException("ds cannot be null");
		}
		
		services.add(ds);
	}

	@Override
	public void deregister(DirectoryService ds) {
		if (ds == null) {
			throw new NullPointerException("ds cannot be null");
		}
		
		services.remove(ds);
	}

	private void notifyAdd(Agent agent) {
		for (DirectoryService ds : services) {
			ds.updateAgent(new AgentInfo(agent.getName()));
		}
	}

	private void notifyRemove(Agent agent) {
		for (DirectoryService ds : services) {
			ds.removeAgent(agent.getName());
		}
	}
       	
}