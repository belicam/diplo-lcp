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
public class MessageWithContext extends AbstractMessage {

    ContextId contextId = null;

    public MessageWithContext(AgentId senderLabel, MessageId id, ContextId contextId, Set<AgentId> rcpts, String content) {
        super(senderLabel, id, rcpts, content);
        setContextId(contextId);
    }

    /**
     * @return the contextId
     */
    public ContextId getContextId() {
        return contextId;
    }

    /**
     * @param contextId the contextId to set
     */
    public void setContextId(ContextId contextId) {
        this.contextId = contextId;
    }
}
