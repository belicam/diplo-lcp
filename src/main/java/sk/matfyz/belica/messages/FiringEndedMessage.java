/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.matfyz.belica.messages;

import java.util.Set;
import sk.matfyz.lcp.AbstractMessage;
import sk.matfyz.lcp.api.AgentId;
import sk.matfyz.lcp.api.MessageId;

/**
 *
 * @author martin
 */
public class FiringEndedMessage extends AbstractMessage {

    public FiringEndedMessage(AgentId senderLabel, MessageId messageId, Set<AgentId> rcpts) {
        super(senderLabel, messageId, rcpts, null);
    }
        
    @Override
    public String toString() {
        return "FiringEndedMessage: Program#" + getSender() + " has ended firing.";
    }
}
