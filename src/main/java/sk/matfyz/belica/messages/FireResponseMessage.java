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
public class FireResponseMessage extends Message {

    public FireResponseMessage(int id, AgentId senderLabel, int referenceId) {
        super(id, senderLabel);
        this.setReferenceId(referenceId);
    }

    @Override
    public String toString() {
        return "FireResponseMessage: Program#" + getSenderLabel() + " responds to fire";
    }

}
