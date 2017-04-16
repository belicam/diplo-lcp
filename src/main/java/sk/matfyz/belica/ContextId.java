/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.matfyz.belica;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;
import sk.matfyz.lcp.api.AgentId;

/**
 *
 * @author martin
 */
public class ContextId implements Serializable {

    private final AgentId agentId;
    private final Long value;
    private final Timestamp created;
    
    public ContextId(AgentId agentId, long value) {
        this.agentId = agentId;
        this.value = value;
        this.created = new Timestamp(System.currentTimeMillis());
    }

    /**
     * @return the value
     */
    public Long getValue() {
        return value;
    }

    /**
     * @return the agentId
     */
    public AgentId getAgentId() {
        return agentId;
    }

    @Override
    public boolean equals(Object contextId) {
        if (contextId == null) {
            return false;
        }

        if (getClass() != contextId.getClass()) {
            return false;
        }

        final ContextId other = (ContextId) contextId;
        return Objects.equals(getValue(), other.getValue()) && Objects.equals(getAgentId(), other.getAgentId()) && Objects.equals(getCreated(), other.getCreated());
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.agentId);
        hash = 37 * hash + Objects.hashCode(this.value);
        hash = 37 * hash + Objects.hashCode(this.created);
        return hash;
    }
    
    /**
     * @return the created
     */
    public Timestamp getCreated() {
        return created;
    }
}
