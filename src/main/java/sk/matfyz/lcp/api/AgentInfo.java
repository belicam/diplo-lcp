package sk.matfyz.lcp.api;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class AgentInfo implements Serializable {

    private final AgentId id;
    private final Set<String> flags = new HashSet<String>();
    private final Set<String> services = new HashSet<String>();
    private final List<TransportAddress> addresses = new ArrayList<TransportAddress>();

    public AgentInfo(final AgentId id) {
        if (id == null) {
            throw new NullPointerException();
        }

        this.id = id;
    }

    public final AgentId getId() {
        return id;
    }

    public final Set<String> getFlags() {
        return flags;
    }

    public final Set<String> getServices() {
        return services;
    }

    public final List<TransportAddress> getTransportAddresses() {
        return addresses;
    }

    public void update(AgentInfo agent) {
        if (agent == null) {
            throw new NullPointerException("agent cannot be null");
        }

        this.addresses.clear();
        this.addresses.addAll(agent.addresses);

        this.flags.clear();
        this.flags.addAll(agent.flags);

        this.services.clear();
        this.services.addAll(agent.services);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        final AgentInfo other = (AgentInfo) obj;

        return Objects.equals(this.id, other.id);

    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.id);
        return hash;
    }

}
