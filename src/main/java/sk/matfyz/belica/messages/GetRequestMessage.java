/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.matfyz.belica.messages;

import java.util.List;
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
public class GetRequestMessage extends AbstractMessage {
    
    public GetRequestMessage(AgentId senderLabel, MessageId id, Set<AgentId> rcpts, Set<Literal> lits, AgentId initialSender) {
        super(senderLabel, id, rcpts, null);

        String strLits = MessageContentSerializer.stringifyLiterals(lits);
        String strAgentId = MessageContentSerializer.stringifyAgentId(initialSender);
        
        setContent(strLits + MessageContentSerializer.CONTENT_DELIMITER + strAgentId);
    }
    
    @Override
    public String toString() {
        return "GetRequestMessage: " + getSender() + " asks for " + getContent();
    }
}
