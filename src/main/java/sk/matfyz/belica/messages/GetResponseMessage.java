/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.matfyz.belica.messages;

import java.util.HashSet;
import java.util.Set;
import sk.matfyz.lcp.AbstractMessage;
import sk.matfyz.lcp.api.AgentId;
import sk.matfyz.lcp.api.MessageId;

/**
 *
 * @author martin
 */
public class GetResponseMessage extends AbstractMessage {
    
    private MessageId referenceId;
    
    public GetResponseMessage(AgentId senderLabel, MessageId id, Set<AgentId> rcpts, MessageId idOfRequest) {
        super(senderLabel, id, rcpts, null); // TODO doriesit content
        setReferenceId(idOfRequest);
    }
    
    @Override
    public String toString() {
        return "GetResponseMessage: " + getSender() + " responds to get";
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
