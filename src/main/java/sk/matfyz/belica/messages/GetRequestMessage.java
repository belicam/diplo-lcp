/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.matfyz.belica.messages;

import java.util.List;
import sk.matfyz.belica.Literal;
import sk.matfyz.lcp.api.AgentId;

/**
 *
 * @author martin
 */
public class GetRequestMessage extends Message {
    private AgentId initialSender;
    private List<Literal> lits;
    
    public GetRequestMessage(int id, AgentId senderLabel, AgentId initialSender, List<Literal> lits) {
        super(id, senderLabel);
        this.initialSender = initialSender;
        this.lits = lits;
    }
    
    @Override
    public String toString() {
        return "GetRequestMessage: Program#" + getSenderLabel() + " asks for " + getLits();
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
