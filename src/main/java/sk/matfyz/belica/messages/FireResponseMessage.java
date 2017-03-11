/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.matfyz.belica.messages;

import java.util.Set;
import sk.matfyz.belica.MessageContentSerializer;
import sk.matfyz.lcp.AbstractMessage;
import sk.matfyz.lcp.api.AgentId;
import sk.matfyz.lcp.api.MessageId;

/**
 *
 * @author martin
 */
public class FireResponseMessage extends AbstractMessage {

    public FireResponseMessage(AgentId senderLabel, MessageId id, Set<AgentId> rcpts, MessageId referenceId) {
        super(senderLabel, id, rcpts, MessageContentSerializer.stringifyMessageId(referenceId)); 
    }

    @Override
    public String toString() {
        return "FireResponseMessage: " + getSender() + " responds to fire";
    }
}
