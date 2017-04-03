package sk.matfyz.lcp;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import sk.matfyz.lcp.api.AgentAddedEvent;
import sk.matfyz.lcp.api.AgentChangedEvent;
import sk.matfyz.lcp.api.AgentId;
import sk.matfyz.lcp.api.AgentInfo;
import sk.matfyz.lcp.api.AgentRemovedEvent;
import sk.matfyz.lcp.api.DirectoryService;
import sk.matfyz.lcp.api.DiscoveryService;
import sk.matfyz.lcp.api.EventSource;
import sk.matfyz.lcp.api.Timestamped;

/**
 *
 * TODO: There is no merging implemented now. One Discovery service can rewrite
 * info from another.
 *
 * @author shanki
 */
public class DirectoryServiceImpl implements DirectoryService {

    private Thread externalAgentsChecker;
    private final Map<AgentId, Timestamped<AgentInfo>> agents = new ConcurrentHashMap<>();

    private final EventSource<AgentAddedEvent> agentAddedEventSource = new EventSourceImpl<AgentAddedEvent>();
    private final EventSource<AgentRemovedEvent> agentRemovedEventSource = new EventSourceImpl<AgentRemovedEvent>();
    private final EventSource<AgentChangedEvent> agentChangedEventSource = new EventSourceImpl<AgentChangedEvent>();

    public DirectoryServiceImpl() {
        setupThread();
        start();
    }
    
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
        Timestamped<AgentInfo> agent = agents.get(agentId);
        if (agent == null) {
            return null;
        }
        return agent.getValue();
    }

    @Override
    public void updateAgent(AgentInfo agent) {
        if (agent == null) {
            throw new NullPointerException("agent cannot be null");
        }

        Timestamped<AgentInfo> old = agents.get(agent.getId());
        
        if (old != null) {
            agentChangedEventSource.postEvent(new AgentChangedEvent(agent));
        } else {
            agentAddedEventSource.postEvent(new AgentAddedEvent(agent));
        }

        agents.put(agent.getId(), new Timestamped<>(agent));

    }

    @Override
    public void removeAgent(AgentId agentId) {
        if (agentId == null) {
            throw new NullPointerException("AgentId cannot be null");
        }

        agents.remove(agentId);
        agentRemovedEventSource.postEvent(new AgentRemovedEvent(agentId));
    }
    
    private void setupThread() {
        externalAgentsChecker = new Thread(() -> {
            while (true) {
                try {
//                    System.out.println("DirectoryServiceImpl externalAgents: " + agents.size());
                    agents.entrySet().removeIf((agentEntry) -> !agentEntry.getValue().isValid());
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(DirectoryServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    private void start() {
        externalAgentsChecker.start();
    }

    private void stop() {
        externalAgentsChecker.interrupt();        
    }
}
