package sk.matfyz.lcp.api;

import java.util.Collection;

public interface Discovery {
    public void start();
    public void stop();
    void broadcast(Collection<AgentInfo> agentsToBroadcast);
    void registerLocalAgent(AgentInfo a);
    void deregisterLocalAgent(AgentId agentId);
    TransportAddress getTransportAddress();
}
