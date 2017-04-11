/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.matfyz.belica;

import java.io.Serializable;
import java.util.Objects;
import sk.matfyz.lcp.api.AgentId;

/**
 *
 * @author martin
 */
public class ContextId implements Serializable {

    private final AgentId agentId;
    private final Long value;

    public ContextId(AgentId agentId, long value) {
        this.agentId = agentId;
        this.value = value;
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
        return Objects.equals(getValue(), other.getValue()) && Objects.equals(getAgentId(), other.getAgentId());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.agentId);
        hash = 97 * hash + Objects.hashCode(this.value);
        return hash;
    }
}
