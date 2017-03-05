/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.matfyz.belica.messages;

import sk.matfyz.lcp.api.AgentId;

/**
 *
 * @author martin
 */
public class NotifyParticipationRequestMessage extends Message {
    public NotifyParticipationRequestMessage(int id, AgentId senderLabel) {
        super(id, senderLabel);
    }
    
    @Override
    public String toString() {
        return "NotifyParticipationRequestMessage: Program#" + getSenderLabel() + " is participating.";
    }
}
