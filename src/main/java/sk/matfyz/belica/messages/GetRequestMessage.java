/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.matfyz.belica.messages;

import java.util.List;
import java.util.Set;
import sk.matfyz.belica.Literal;
import sk.matfyz.lcp.AbstractMessage;
import sk.matfyz.lcp.api.AgentId;
import sk.matfyz.lcp.api.MessageId;

/**
 *
 * @author martin
 */
public class GetRequestMessage extends AbstractMessage {
    private AgentId initialSender;
    private List<Literal> lits;
    
    public GetRequestMessage(AgentId senderLabel, MessageId id, Set<AgentId> rcpts, AgentId initialSender, List<Literal> lits) {
        super(senderLabel, id, rcpts, null); // TODO doriesit content
        this.initialSender = initialSender;
        this.lits = lits;
    }
    
    @Override
    public String toString() {
        return "GetRequestMessage: Program#" + getSender() + " asks for " + getLits();
    }

    /**
     * @return the lits
     */
    public List<Literal> getLits() {
        return lits;
    }

    /**
     * @param lits the lits to set
     */
    public void setLits(List<Literal> lits) {
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
