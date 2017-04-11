/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.matfyz.belica.messages;

import java.util.List;
import java.util.Set;
import sk.matfyz.belica.ContextId;
import sk.matfyz.belica.Literal;
import sk.matfyz.lcp.AbstractMessage;
import sk.matfyz.lcp.api.AgentId;
import sk.matfyz.lcp.api.MessageId;

/**
 *
 * @author martin
 */
public class GetRequestMessage extends MessageWithContext {
    private AgentId initialSender;
    private Set<Literal> lits;
    
    public GetRequestMessage(AgentId senderLabel, MessageId id, ContextId context, Set<AgentId> rcpts, Set<Literal> lits, AgentId initialSender) {
        super(senderLabel, id, context, rcpts, null); 
        setInitialSender(initialSender);
        setLits(lits);
    }
    
    @Override
    public String toString() {
        return "GetRequestMessage: " + getSender() + " asks for " + getLits();
    }

    /**
     * @return the lits
     */
    public Set<Literal> getLits() {
        return lits;
    }

    /**
     * @param lits the lits to set
     */
    public void setLits(Set<Literal> lits) {
        this.lits = lits;
    }

    /**
     * @return the initialSender
     */
    public AgentId getInitialSender() {
        return initialSender;
    }

    /**
     * @param initialSender the initialSender to set
     */
    public void setInitialSender(AgentId initialSender) {
        this.initialSender = initialSender;
    }

}
