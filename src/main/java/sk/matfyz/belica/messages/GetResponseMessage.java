/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.matfyz.belica.messages;

import java.util.Set;
import sk.matfyz.belica.ContextId;
import sk.matfyz.lcp.api.AgentId;
import sk.matfyz.lcp.api.MessageId;

/**
 *
 * @author martin
 */
public class GetResponseMessage extends MessageWithContext {
    
    private MessageId referenceId;
    
    public GetResponseMessage(AgentId senderLabel, MessageId id, ContextId context, Set<AgentId> rcpts, MessageId refId) {
        super(senderLabel, id, context, rcpts, null); 
        setReferenceId(refId);
    }
    
    @Override
    public String toString() {
        return "GetResponseMessage: " + getSender() + " responds to " + getRecepients();
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
    public final void setReferenceId(MessageId referenceId) {
        this.referenceId = referenceId;
    }
}
