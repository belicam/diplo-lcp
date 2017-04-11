/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.matfyz.belica.messages;

import java.util.Set;
import sk.matfyz.belica.ContextId;
import sk.matfyz.lcp.AbstractMessage;
import sk.matfyz.lcp.api.AgentId;
import sk.matfyz.lcp.api.MessageId;

/**
 *
 * @author martin
 */
public class FireResponseMessage extends MessageWithContext {

    private MessageId referenceId;
    
    public FireResponseMessage(AgentId senderLabel, MessageId id, ContextId context, Set<AgentId> rcpts, MessageId referenceId) {
        super(senderLabel, id, context, rcpts, null); 
        this.setReferenceId(referenceId);
    }

    @Override
    public String toString() {
        return "FireResponseMessage: " + getSender() + " responds to fire";
    }

    /**
     * @return the referenceId
     */
    public MessageId getReferenceId() {
        return referenceId;
    }

    /**
     * @param referenceId the referenceId to set
     */
    public void setReferenceId(MessageId referenceId) {
        this.referenceId = referenceId;
    }

}
