/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.matfyz.belica.messages;

import java.util.HashSet;
import java.util.Set;
import sk.matfyz.belica.MessageContentSerializer;
import sk.matfyz.lcp.AbstractMessage;
import sk.matfyz.lcp.api.AgentId;
import sk.matfyz.lcp.api.MessageId;

/**
 *
 * @author martin
 */
public class GetResponseMessage extends AbstractMessage {
    
    public GetResponseMessage(AgentId senderLabel, MessageId id, Set<AgentId> rcpts, MessageId referenceId) {
        super(senderLabel, id, rcpts, MessageContentSerializer.stringifyMessageId(referenceId)); // TODO doriesit content
    }
    
    @Override
    public String toString() {
        return "GetResponseMessage: " + getSender() + " responds to " + getRecepients();
    }
}
