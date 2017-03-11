/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.matfyz.belica.messages;

import java.util.Set;
import sk.matfyz.belica.Literal;
import sk.matfyz.belica.MessageContentSerializer;
import sk.matfyz.lcp.AbstractMessage;
import sk.matfyz.lcp.api.AgentId;
import sk.matfyz.lcp.api.MessageId;

/**
 *
 * @author martin
 */
public class FireRequestMessage extends AbstractMessage {
    
    public FireRequestMessage(AgentId senderLabel, MessageId id, Set<AgentId> rcpts, Set<Literal> lits) {
        super(senderLabel, id, rcpts, MessageContentSerializer.stringifyLiterals(lits)); 
    }

    @Override
    public String toString() {
        return "FireRequestMessage: " + getSender() + " sends " + getContent();
    }
}
