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
public class ActivationMessage extends AbstractMessage {

    public ActivationMessage(AgentId senderLabel, MessageId id, Set<AgentId> rcpts) {
        super(senderLabel, id, rcpts, null);
    }

    @Override
    public String toString() {
        return "ActivationMessage sent from Program#" + getSender() + ".";
    }
}
