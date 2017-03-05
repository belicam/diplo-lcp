/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.matfyz.belica.messages;

import java.util.Set;
import sk.matfyz.belica.Literal;
import sk.matfyz.lcp.api.AgentId;

/**
 *
 * @author martin
 */
public class FireRequestMessage extends Message {
    private Set<Literal> lits;
    
    public FireRequestMessage(int id, AgentId senderLabel, Set<Literal> lits) {
        super(id, senderLabel);
        this.lits = lits;
    }

    @Override
    public String toString() {
        return "FireRequestMessage: Program#" + getSenderLabel() + " sends " + getLits();
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
}
