package sk.matfyz.lcp;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import sk.matfyz.lcp.api.AgentAddedEvent;
import sk.matfyz.lcp.api.AgentChangedEvent;
import sk.matfyz.lcp.api.AgentId;
import sk.matfyz.lcp.api.AgentInfo;
import sk.matfyz.lcp.api.AgentRemovedEvent;
import sk.matfyz.lcp.api.DirectoryService;
import sk.matfyz.lcp.api.DiscoveryService;
import sk.matfyz.lcp.api.EventSource;

/**
 * 
 * TODO: There is no merging implemented now. One Discovery service can rewrite info from another.
 *
 * @author shanki
 */
public class DirectoryServiceImpl implements DirectoryService {
	
	private final Map<AgentId, AgentInfo> agents = new ConcurrentHashMap<>();

	private final EventSource<AgentAddedEvent> agentAddedEventSource = new EventSourceImpl<AgentAddedEvent>();
	private final EventSource<AgentRemovedEvent> agentRemovedEventSource = new EventSourceImpl<AgentRemovedEvent>();
	private final EventSource<AgentChangedEvent> agentChangedEventSource = new EventSourceImpl<AgentChangedEvent>();

	@Override
	public EventSource<AgentAddedEvent> getAgentAddedSource() {
		return agentAddedEventSource;
	}

	@Override
	public EventSource<AgentRemovedEvent> getAgentRemovedSource() {
		return agentRemovedEventSource;
	}

	@Override
	public EventSource<AgentChangedEvent> getAgentChangedSource() {
		return agentChangedEventSource;
	}

	@Override
	public void registerDS(DiscoveryService ds) {
		if (ds == null) {
			throw new NullPointerException("ds cannot be null");
		}
		
		ds.register(this);
	}

	@Override
	public void deregisterDS(DiscoveryService ds) {
		if (ds == null) {
			throw new NullPointerException("ds cannot be null");
		}
		
		ds.deregister(this);
	}

	@Override
	public AgentInfo lookup(AgentId agentId) {
		return agents.get(agentId);
	}
	
	@Override
	public void updateAgent(AgentInfo agent) {
		if (agent == null) {
			throw new NullPointerException("agent cannot be null");
		}
		
		AgentInfo old = agents.get(agent.getId());
		
		if (old != null) {
			old.update(agent);
			agentChangedEventSource.postEvent(new AgentChangedEvent(agent));
		} else {
			agents.put(agent.getId(), agent);
			agentAddedEventSource.postEvent(new AgentAddedEvent(agent));
		}
	}

	@Override
	public void removeAgent(AgentId agentId) {
		if (agentId == null) {
			throw new NullPointerException("AgentId cannot be null");
		}

		agents.remove(agentId);
		agentRemovedEventSource.postEvent(new AgentRemovedEvent(agentId));
	}
}